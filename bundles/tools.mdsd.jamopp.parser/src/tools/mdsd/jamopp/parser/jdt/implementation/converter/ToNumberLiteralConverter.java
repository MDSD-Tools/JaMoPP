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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.NumberLiteral;

import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToNumberLiteralConverter implements Converter<NumberLiteral, Literal> {

	private static final char DOUBLE_BACKSLASH = '\\';
	private static final String ZERO_INT = "0";
	private static final String ZERO_LONG = "0l";
	private final LiteralsFactory literalsFactory;
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

	private final Map<Predicate<String>, Function<String, Literal>> mappings;

	@Inject
	public ToNumberLiteralConverter(final UtilLayout layoutInformationConverter,
			final LiteralsFactory literalsFactory) {
		this.literalsFactory = literalsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		mappings = new LinkedHashMap<>();
	}

	@Override
	public Literal convert(final NumberLiteral literal) {
		if (mappings.isEmpty()) {
			mappings.put(string -> string.startsWith(BIN_PREFIX) && string.endsWith(LONG_SUFFIX),
					this::handleBinaryLong);
			mappings.put(string -> string.startsWith(BIN_PREFIX), this::handleBinaryInteger);
			mappings.put(string -> string.contains(HEX_EXPONENT) && string.endsWith(FLOAT_SUFFIX),
					this::handleHexFloat);
			mappings.put(string -> string.contains(HEX_EXPONENT), this::handleHexDouble);
			mappings.put(string -> string.startsWith(HEX_PREFIX) && string.endsWith(LONG_SUFFIX), this::handleHexLong);
			mappings.put(string -> string.startsWith(HEX_PREFIX), this::handleHexInteger);
			mappings.put(string -> string.endsWith(FLOAT_SUFFIX), this::handleDecimalFloat);
			mappings.put(string -> string.contains(".") || string.contains(DECIMAL_EXPONENT)
					|| string.endsWith(DOUBLE_SUFFIX), this::handleDecimalDouble);
			mappings.put(string -> ZERO_LONG.equals(string)
					|| !string.startsWith(OCT_PREFIX) && string.endsWith(LONG_SUFFIX), this::handleDecimalLong);
			mappings.put(string -> ZERO_INT.equals(string) || !string.startsWith(OCT_PREFIX),
					this::handleDecimalInteger);
			mappings.put(string -> string.endsWith(LONG_SUFFIX), this::handleOctalLong);
		}

		Literal result = null;
		final String string = buildString(literal);

		for (final Entry<Predicate<String>, Function<String, Literal>> entry : mappings.entrySet()) {
			if (entry.getKey().test(string)) {
				result = entry.getValue().apply(string);
				break;
			}
		}

		if (result == null) {
			result = handleOctalInteger(string);
		}

		layoutInformationConverter.convertToMinimalLayoutInformation(result, literal);
		return result;
	}

	private Literal handleOctalInteger(final String string) {
		final tools.mdsd.jamopp.model.java.literals.OctalIntegerLiteral lit = literalsFactory
				.createOctalIntegerLiteral();
		lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length()), OCT_BASE));
		return lit;
	}

	private Literal handleOctalLong(final String string) {
		final tools.mdsd.jamopp.model.java.literals.OctalLongLiteral lit = literalsFactory.createOctalLongLiteral();
		lit.setOctalValue(new BigInteger(string.substring(OCT_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				OCT_BASE));
		return lit;
	}

	private Literal handleDecimalInteger(final String string) {
		final tools.mdsd.jamopp.model.java.literals.DecimalIntegerLiteral lit = literalsFactory
				.createDecimalIntegerLiteral();
		lit.setDecimalValue(new BigInteger(string, DEC_BASE));
		return lit;
	}

	private Literal handleDecimalLong(final String string) {
		final tools.mdsd.jamopp.model.java.literals.DecimalLongLiteral lit = literalsFactory.createDecimalLongLiteral();
		lit.setDecimalValue(new BigInteger(string.substring(0, string.length() - LONG_SUFFIX.length()), DEC_BASE));
		return lit;
	}

	private Literal handleDecimalDouble(final String string) {
		String newString = string;
		if (newString.endsWith(DOUBLE_SUFFIX)) {
			newString = newString.substring(0, newString.length() - DOUBLE_SUFFIX.length());
		}
		final tools.mdsd.jamopp.model.java.literals.DecimalDoubleLiteral lit = literalsFactory
				.createDecimalDoubleLiteral();
		lit.setDecimalValue(Double.parseDouble(newString));
		return lit;
	}

	private Literal handleDecimalFloat(final String string) {
		final tools.mdsd.jamopp.model.java.literals.DecimalFloatLiteral lit = literalsFactory
				.createDecimalFloatLiteral();
		lit.setDecimalValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
		return lit;
	}

	private Literal handleHexInteger(final String string) {
		final tools.mdsd.jamopp.model.java.literals.HexIntegerLiteral lit = literalsFactory.createHexIntegerLiteral();
		lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length()), HEX_BASE));
		return lit;
	}

	private Literal handleHexLong(final String string) {
		final tools.mdsd.jamopp.model.java.literals.HexLongLiteral lit = literalsFactory.createHexLongLiteral();
		lit.setHexValue(new BigInteger(string.substring(HEX_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				HEX_BASE));
		return lit;
	}

	private Literal handleHexDouble(final String string) {
		String newString = string;
		if (newString.endsWith(DOUBLE_SUFFIX)) {
			newString = newString.substring(0, newString.length() - DOUBLE_SUFFIX.length());
		}
		final tools.mdsd.jamopp.model.java.literals.HexDoubleLiteral lit = literalsFactory.createHexDoubleLiteral();
		lit.setHexValue(Double.parseDouble(newString));
		return lit;
	}

	private Literal handleHexFloat(final String string) {
		final tools.mdsd.jamopp.model.java.literals.HexFloatLiteral lit = literalsFactory.createHexFloatLiteral();
		lit.setHexValue(Float.parseFloat(string.substring(0, string.length() - FLOAT_SUFFIX.length())));
		return lit;
	}

	private Literal handleBinaryInteger(final String string) {
		final tools.mdsd.jamopp.model.java.literals.BinaryIntegerLiteral lit = literalsFactory
				.createBinaryIntegerLiteral();
		lit.setBinaryValue(new BigInteger(string.substring(BIN_PREFIX.length()), BIN_BASE));
		return lit;
	}

	private Literal handleBinaryLong(final String string) {
		final tools.mdsd.jamopp.model.java.literals.BinaryLongLiteral lit = literalsFactory.createBinaryLongLiteral();
		lit.setBinaryValue(new BigInteger(string.substring(BIN_PREFIX.length(), string.length() - LONG_SUFFIX.length()),
				BIN_BASE));
		return lit;
	}

	private String buildString(final NumberLiteral literal) {
		String string = literal.getToken();
		if (string.contains("\\u")) {
			final StringBuilder actualLiteral = new StringBuilder();
			int index = 0;
			while (index < string.length()) {
				final char currentChar = string.charAt(index);
				if (currentChar == DOUBLE_BACKSLASH) {
					final int codePoint = Integer.parseInt(string.substring(index + 2, index + 6), 16);
					actualLiteral.append(Character.toString(codePoint));
					index += 6;
				} else {
					actualLiteral.append(currentChar);
					index += 1;
				}
			}
			string = actualLiteral.toString();
		}
		string = string.replace(UNDER_SCORE, "");
		return string.toLowerCase(Locale.US);
	}
}
