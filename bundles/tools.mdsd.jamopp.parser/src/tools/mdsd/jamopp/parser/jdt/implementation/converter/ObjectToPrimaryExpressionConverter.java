package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.literals.BooleanLiteral;
import tools.mdsd.jamopp.model.java.literals.CharacterLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalDoubleLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalFloatLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalIntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalLongLiteral;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.references.StringReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ObjectToPrimaryExpressionConverter implements Converter<Object, PrimaryExpression> {

	private final ReferencesFactory referencesFactory;
	private final LiteralsFactory literalsFactory;
	private final Map<Class<?>, Function<Object, PrimaryExpression>> mappings;

	@Inject
	public ObjectToPrimaryExpressionConverter(final ReferencesFactory referencesFactory,
			final LiteralsFactory literalsFactory) {
		this.referencesFactory = referencesFactory;
		this.literalsFactory = literalsFactory;
		mappings = new HashMap<>();

		mappings.put(String.class, this::handleString);
		mappings.put(Boolean.class, this::handleBoolean);
		mappings.put(Character.class, this::handleCharacter);
		mappings.put(Byte.class, this::handleByte);
		mappings.put(Short.class, this::handleShort);
		mappings.put(Integer.class, this::handleInteger);
		mappings.put(Long.class, this::handleLong);
		mappings.put(Float.class, this::handleFloat);
		mappings.put(Double.class, this::handleDouble);
	}

	@Override
	public PrimaryExpression convert(final Object value) {
		PrimaryExpression result = null;

		for (final Entry<Class<?>, Function<Object, PrimaryExpression>> entry : mappings.entrySet()) {
			final Class<?> key = entry.getKey();
			final Function<Object, PrimaryExpression> val = entry.getValue();
			if (key.isInstance(value)) {
				result = val.apply(value);
				break;
			}
		}

		if (result == null) {
			result = literalsFactory.createNullLiteral();
		}

		return result;
	}

	private PrimaryExpression handleDouble(final Object value) {
		final DecimalDoubleLiteral literal = literalsFactory.createDecimalDoubleLiteral();
		literal.setDecimalValue((double) value);
		return literal;
	}

	private PrimaryExpression handleFloat(final Object value) {
		final DecimalFloatLiteral literal = literalsFactory.createDecimalFloatLiteral();
		literal.setDecimalValue((float) value);
		return literal;
	}

	private PrimaryExpression handleLong(final Object value) {
		final DecimalLongLiteral literal = literalsFactory.createDecimalLongLiteral();
		literal.setDecimalValue(BigInteger.valueOf((long) value));
		return literal;
	}

	private PrimaryExpression handleInteger(final Object value) {
		final DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
		literal.setDecimalValue(BigInteger.valueOf((int) value));
		return literal;
	}

	private PrimaryExpression handleShort(final Object value) {
		final DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
		literal.setDecimalValue(BigInteger.valueOf((short) value));
		return literal;
	}

	private PrimaryExpression handleByte(final Object value) {
		final DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
		literal.setDecimalValue(BigInteger.valueOf((byte) value));
		return literal;
	}

	private PrimaryExpression handleCharacter(final Object value) {
		final CharacterLiteral literal = literalsFactory.createCharacterLiteral();
		literal.setValue("\\u" + Integer.toHexString((Character) value));
		return literal;
	}

	private PrimaryExpression handleBoolean(final Object value) {
		final BooleanLiteral literal = literalsFactory.createBooleanLiteral();
		literal.setValue((boolean) value);
		return literal;
	}

	private PrimaryExpression handleString(final Object value) {
		final StringReference ref = referencesFactory.createStringReference();
		ref.setValue((String) value);
		return ref;
	}

}