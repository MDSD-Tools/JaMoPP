/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package jamopp.parser.jdt.converter.implementation;

import java.math.BigInteger;

import org.eclipse.jdt.core.dom.NumberLiteral;
import org.emftext.language.java.literals.LiteralsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToNumberLiteralConverter implements ToConverter<NumberLiteral, org.emftext.language.java.literals.Literal> {

	private final LiteralsFactory lieteralsFactory;
	private final UtilLayout layoutInformationConverter;

	private static final String HEX_PREFIX = "0x";
	private static final String BIN_PREFIX = "0b";
	private static final String OCT_PREFIX = "0";
	private static final String LONG_SUFFIX = "l";
	private static final String FLOAT_SUFFIX = "f";
	private static final String DOUBLE_SUFFIX = "d";
	private static final String DECIMAL_EXPONENT = "e";
	private static final String HEX_EXPONENT = "p";
	private static final int BIN_BASE = 2;
	private static final int HEX_BASE = 16;
	private static final int DEC_BASE = 10;
	private static final int OCT_BASE = 8;
	private static final String UNDER_SCORE = "_";

	@Inject
	ToNumberLiteralConverter(UtilLayout layoutInformationConverter, LiteralsFactory lieteralsFactory) {
		this.lieteralsFactory = lieteralsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	@Override
	public org.emftext.language.java.literals.Literal convert(NumberLiteral literal) {
		org.emftext.language.java.literals.Literal result = null;
		String string = literal.getToken();
		if (string.contains("\\u")) {
			StringBuilder actualLiteral = new StringBuilder();
			for (int index = 0; index < string.length(); index++) {
				char currentChar = string.charAt(index);
				if (currentChar == '\\') {
					int codePoint = Integer.parseInt(string.substring(index + 2, index + 6), 16);
					actualLiteral.append(Character.toString(codePoint));
					index += 5;
				} else {
					actualLiteral.append(currentChar);
				}
			}
			string = actualLiteral.toString();
		}
		string = string.replace(UNDER_SCORE, "");
		string = string.toLowerCase();
		if (string.startsWith(BIN_PREFIX) && string.endsWith(LONG_SUFFIX)) {
			org.emftext.language.java.literals.BinaryLongLiteral lit = lieteralsFactory.createBinaryLongLiteral();
			lit.setBinaryValue(new BigInteger(
					string.substring(BIN_PREFIX.length(), string.length() - LONG_SUFFIX.length()), BIN_BASE));
			result = lit;
		} else if (string.startsWith(BIN_PREFIX)) {
			org.emftext.language.java.literals.BinaryIntegerLiteral lit = lieteralsFactory.createBinaryIntegerLiteral();
			lit.setBinaryValue(new BigInteger(string.substring(BIN_PREFIX.length()), BIN_BASE));
			result = lit;
		} else if (string.contains(HEX_EXPONENT) && string.endsWith(FLOAT_SUFFIX)) {
			org.emftext.language.java.literals.HexFloatLiteral lit = lieteralsFactory.createHexFloatLiteral();
			lit.setHexValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
			result = lit;
		} else if (string.contains(HEX_EXPONENT)) {
			if (string.endsWith(DOUBLE_SUFFIX)) {
				string = string.substring(0, string.length() - DOUBLE_SUFFIX.length());
			}
			org.emftext.language.java.literals.HexDoubleLiteral lit = lieteralsFactory.createHexDoubleLiteral();
			lit.setHexValue(Double.parseDouble(string));
			result = lit;
		} else if (string.startsWith(HEX_PREFIX) && string.endsWith(LONG_SUFFIX)) {
			org.emftext.language.java.literals.HexLongLiteral lit = lieteralsFactory.createHexLongLiteral();
			lit.setHexValue(new BigInteger(
					string.substring(HEX_PREFIX.length(), string.length() - LONG_SUFFIX.length()), HEX_BASE));
			result = lit;
		} else if (string.startsWith(HEX_PREFIX)) {
			org.emftext.language.java.literals.HexIntegerLiteral lit = lieteralsFactory.createHexIntegerLiteral();
			lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length()), HEX_BASE));
			result = lit;
		} else if (string.endsWith(FLOAT_SUFFIX)) {
			org.emftext.language.java.literals.DecimalFloatLiteral lit = lieteralsFactory.createDecimalFloatLiteral();
			lit.setDecimalValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
			result = lit;
		} else if (string.contains(".") || string.contains(DECIMAL_EXPONENT) || string.endsWith(DOUBLE_SUFFIX)) {
			if (string.endsWith(DOUBLE_SUFFIX)) {
				string = string.substring(0, string.length() - DOUBLE_SUFFIX.length());
			}
			org.emftext.language.java.literals.DecimalDoubleLiteral lit = lieteralsFactory.createDecimalDoubleLiteral();
			lit.setDecimalValue(Double.parseDouble(string));
			result = lit;
		} else if ("0l".equals(string) || (!string.startsWith(OCT_PREFIX) && string.endsWith(LONG_SUFFIX))) {
			org.emftext.language.java.literals.DecimalLongLiteral lit = lieteralsFactory.createDecimalLongLiteral();
			lit.setDecimalValue(new BigInteger(string.substring(0, string.length() - LONG_SUFFIX.length()), DEC_BASE));
			result = lit;
		} else if ("0".equals(string) || !string.startsWith(OCT_PREFIX)) {
			org.emftext.language.java.literals.DecimalIntegerLiteral lit = lieteralsFactory
					.createDecimalIntegerLiteral();
			lit.setDecimalValue(new BigInteger(string, DEC_BASE));
			result = lit;
		} else if (string.endsWith(LONG_SUFFIX)) {
			org.emftext.language.java.literals.OctalLongLiteral lit = lieteralsFactory.createOctalLongLiteral();
			lit.setOctalValue(new BigInteger(
					string.substring(OCT_PREFIX.length(), string.length() - LONG_SUFFIX.length()), OCT_BASE));
			result = lit;
		} else {
			org.emftext.language.java.literals.OctalIntegerLiteral lit = lieteralsFactory.createOctalIntegerLiteral();
			lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length()), OCT_BASE));
			result = lit;
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, literal);
		return result;
	}
}
