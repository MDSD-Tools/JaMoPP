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

package jamopp.parser.jdt.implementation.converter;

import java.math.BigInteger;

import org.eclipse.jdt.core.dom.NumberLiteral;
import org.emftext.language.java.literals.LiteralsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToNumberLiteralConverter implements Converter<NumberLiteral, org.emftext.language.java.literals.Literal> {

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
		String string = buildString(literal);
		if (string.startsWith(BIN_PREFIX) && string.endsWith(LONG_SUFFIX)) {
			result = handleBinaryLong(string);
		} else if (string.startsWith(BIN_PREFIX)) {
			result = handleBinaryInteger(string);
		} else if (string.contains(HEX_EXPONENT) && string.endsWith(FLOAT_SUFFIX)) {
			result = handleHexFloat(string);
		} else if (string.contains(HEX_EXPONENT)) {
			result = handleHexDouble(string);
		} else if (string.startsWith(HEX_PREFIX) && string.endsWith(LONG_SUFFIX)) {
			result = handleHexLong(string);
		} else if (string.startsWith(HEX_PREFIX)) {
			result = handleHexInteger(string);
		} else if (string.endsWith(FLOAT_SUFFIX)) {
			result = handleDecimalFloat(string);
		} else if (string.contains(".") || string.contains(DECIMAL_EXPONENT) || string.endsWith(DOUBLE_SUFFIX)) {
			result = handleDecimalDouble(string);
		} else if ("0l".equals(string) || !string.startsWith(OCT_PREFIX) && string.endsWith(LONG_SUFFIX)) {
			result = handleDecimalLong(string);
		} else if ("0".equals(string) || !string.startsWith(OCT_PREFIX)) {
			result = handleDecimalInteger(string);
		} else if (string.endsWith(LONG_SUFFIX)) {
			result = handleOctalLong(string);
		} else {
			result = handleOctalInteger(string);
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, literal);
		return result;
	}

	private org.emftext.language.java.literals.Literal handleOctalInteger(String string) {
		org.emftext.language.java.literals.OctalIntegerLiteral lit = this.lieteralsFactory.createOctalIntegerLiteral();
		lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length()), OCT_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleOctalLong(String string) {
		org.emftext.language.java.literals.OctalLongLiteral lit = this.lieteralsFactory.createOctalLongLiteral();
		lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				OCT_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleDecimalInteger(String string) {
		org.emftext.language.java.literals.DecimalIntegerLiteral lit = this.lieteralsFactory
				.createDecimalIntegerLiteral();
		lit.setDecimalValue(new BigInteger(string, DEC_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleDecimalLong(String string) {
		org.emftext.language.java.literals.DecimalLongLiteral lit = this.lieteralsFactory.createDecimalLongLiteral();
		lit.setDecimalValue(new BigInteger(string.substring(0, string.length() - LONG_SUFFIX.length()), DEC_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleDecimalDouble(String string) {
		if (string.endsWith(DOUBLE_SUFFIX)) {
			string = string.substring(0, string.length() - DOUBLE_SUFFIX.length());
		}
		org.emftext.language.java.literals.DecimalDoubleLiteral lit = this.lieteralsFactory
				.createDecimalDoubleLiteral();
		lit.setDecimalValue(Double.parseDouble(string));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleDecimalFloat(String string) {
		org.emftext.language.java.literals.DecimalFloatLiteral lit = this.lieteralsFactory.createDecimalFloatLiteral();
		lit.setDecimalValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleHexInteger(String string) {
		org.emftext.language.java.literals.HexIntegerLiteral lit = this.lieteralsFactory.createHexIntegerLiteral();
		lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length()), HEX_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleHexLong(String string) {
		org.emftext.language.java.literals.HexLongLiteral lit = this.lieteralsFactory.createHexLongLiteral();
		lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				HEX_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleHexDouble(String string) {
		if (string.endsWith(DOUBLE_SUFFIX)) {
			string = string.substring(0, string.length() - DOUBLE_SUFFIX.length());
		}
		org.emftext.language.java.literals.HexDoubleLiteral lit = this.lieteralsFactory.createHexDoubleLiteral();
		lit.setHexValue(Double.parseDouble(string));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleHexFloat(String string) {
		org.emftext.language.java.literals.HexFloatLiteral lit = this.lieteralsFactory.createHexFloatLiteral();
		lit.setHexValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleBinaryInteger(String string) {
		org.emftext.language.java.literals.BinaryIntegerLiteral lit = this.lieteralsFactory
				.createBinaryIntegerLiteral();
		lit.setBinaryValue(new BigInteger(string.substring(BIN_PREFIX.length()), BIN_BASE));
		return lit;
	}

	private org.emftext.language.java.literals.Literal handleBinaryLong(String string) {
		org.emftext.language.java.literals.BinaryLongLiteral lit = this.lieteralsFactory.createBinaryLongLiteral();
		lit.setBinaryValue(new BigInteger(string.substring(BIN_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				BIN_BASE));
		return lit;
	}

	private String buildString(NumberLiteral literal) {
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
		return string.toLowerCase();
	}
}
