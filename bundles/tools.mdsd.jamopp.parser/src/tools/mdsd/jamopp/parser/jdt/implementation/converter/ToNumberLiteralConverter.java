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

package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.NumberLiteral;

import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToNumberLiteralConverter
		implements Converter<NumberLiteral, tools.mdsd.jamopp.model.java.literals.Literal> {

	private static final String ZERO_INT = "0";
	private static final String ZERO_LONG = "0l";
	private final LiteralsFactory lieteralsFactory;
	private final UtilLayout layoutInformationConverter;

	private static final String HEX_PREFIX = "0x";
	private static final String BIN_PREFIX = "0b";
	private static final String OCT_PREFIX = ZERO_INT;
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
	public ToNumberLiteralConverter(UtilLayout layoutInformationConverter, LiteralsFactory lieteralsFactory) {
		this.lieteralsFactory = lieteralsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.literals.Literal convert(NumberLiteral literal) {
		tools.mdsd.jamopp.model.java.literals.Literal result;
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
		} else if (ZERO_LONG.equals(string) || !string.startsWith(OCT_PREFIX) && string.endsWith(LONG_SUFFIX)) {
			result = handleDecimalLong(string);
		} else if (ZERO_INT.equals(string) || !string.startsWith(OCT_PREFIX)) {
			result = handleDecimalInteger(string);
		} else if (string.endsWith(LONG_SUFFIX)) {
			result = handleOctalLong(string);
		} else {
			result = handleOctalInteger(string);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, literal);
		return result;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleOctalInteger(String string) {
		tools.mdsd.jamopp.model.java.literals.OctalIntegerLiteral lit = lieteralsFactory.createOctalIntegerLiteral();
		lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length()), OCT_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleOctalLong(String string) {
		tools.mdsd.jamopp.model.java.literals.OctalLongLiteral lit = lieteralsFactory.createOctalLongLiteral();
		lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				OCT_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleDecimalInteger(String string) {
		tools.mdsd.jamopp.model.java.literals.DecimalIntegerLiteral lit = lieteralsFactory
				.createDecimalIntegerLiteral();
		lit.setDecimalValue(new BigInteger(string, DEC_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleDecimalLong(String string) {
		tools.mdsd.jamopp.model.java.literals.DecimalLongLiteral lit = lieteralsFactory.createDecimalLongLiteral();
		lit.setDecimalValue(new BigInteger(string.substring(0, string.length() - LONG_SUFFIX.length()), DEC_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleDecimalDouble(String string) {
		String newString = string;
		if (newString.endsWith(DOUBLE_SUFFIX)) {
			newString = newString.substring(0, newString.length() - DOUBLE_SUFFIX.length());
		}
		tools.mdsd.jamopp.model.java.literals.DecimalDoubleLiteral lit = lieteralsFactory.createDecimalDoubleLiteral();
		lit.setDecimalValue(Double.parseDouble(newString));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleDecimalFloat(String string) {
		tools.mdsd.jamopp.model.java.literals.DecimalFloatLiteral lit = lieteralsFactory.createDecimalFloatLiteral();
		lit.setDecimalValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleHexInteger(String string) {
		tools.mdsd.jamopp.model.java.literals.HexIntegerLiteral lit = lieteralsFactory.createHexIntegerLiteral();
		lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length()), HEX_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleHexLong(String string) {
		tools.mdsd.jamopp.model.java.literals.HexLongLiteral lit = lieteralsFactory.createHexLongLiteral();
		lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				HEX_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleHexDouble(String string) {
		String newString = string;
		if (newString.endsWith(DOUBLE_SUFFIX)) {
			newString = newString.substring(0, newString.length() - DOUBLE_SUFFIX.length());
		}
		tools.mdsd.jamopp.model.java.literals.HexDoubleLiteral lit = lieteralsFactory.createHexDoubleLiteral();
		lit.setHexValue(Double.parseDouble(newString));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleHexFloat(String string) {
		tools.mdsd.jamopp.model.java.literals.HexFloatLiteral lit = lieteralsFactory.createHexFloatLiteral();
		lit.setHexValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleBinaryInteger(String string) {
		tools.mdsd.jamopp.model.java.literals.BinaryIntegerLiteral lit = lieteralsFactory.createBinaryIntegerLiteral();
		lit.setBinaryValue(new BigInteger(string.substring(BIN_PREFIX.length()), BIN_BASE));
		return lit;
	}

	private tools.mdsd.jamopp.model.java.literals.Literal handleBinaryLong(String string) {
		tools.mdsd.jamopp.model.java.literals.BinaryLongLiteral lit = lieteralsFactory.createBinaryLongLiteral();
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
		return string.toLowerCase(Locale.US);
	}
}
