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
	public ObjectToPrimaryExpressionConverter(ReferencesFactory referencesFactory, LiteralsFactory literalsFactory) {
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
	public PrimaryExpression convert(Object value) {
		PrimaryExpression expression = literalsFactory.createNullLiteral();
		for (Entry<Class<?>, Function<Object, PrimaryExpression>> entry : mappings.entrySet()) {
			if (entry.getKey().isInstance(value)) {
				expression = entry.getValue().apply(entry);
				break;
			}
		}
		return expression;
	}

	private PrimaryExpression handleDouble(Object value) {
		DecimalDoubleLiteral literal = literalsFactory.createDecimalDoubleLiteral();
		literal.setDecimalValue((double) value);
		return literal;
	}

	private PrimaryExpression handleFloat(Object value) {
		DecimalFloatLiteral literal = literalsFactory.createDecimalFloatLiteral();
		literal.setDecimalValue((float) value);
		return literal;
	}

	private PrimaryExpression handleLong(Object value) {
		DecimalLongLiteral literal = literalsFactory.createDecimalLongLiteral();
		literal.setDecimalValue(BigInteger.valueOf((long) value));
		return literal;
	}

	private PrimaryExpression handleInteger(Object value) {
		DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
		literal.setDecimalValue(BigInteger.valueOf((int) value));
		return literal;
	}

	private PrimaryExpression handleShort(Object value) {
		DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
		literal.setDecimalValue(BigInteger.valueOf((short) value));
		return literal;
	}

	private PrimaryExpression handleByte(Object value) {
		DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
		literal.setDecimalValue(BigInteger.valueOf((byte) value));
		return literal;
	}

	private PrimaryExpression handleCharacter(Object value) {
		CharacterLiteral literal = literalsFactory.createCharacterLiteral();
		literal.setValue("\\u" + Integer.toHexString((Character) value));
		return literal;
	}

	private PrimaryExpression handleBoolean(Object value) {
		BooleanLiteral literal = literalsFactory.createBooleanLiteral();
		literal.setValue((boolean) value);
		return literal;
	}

	private PrimaryExpression handleString(Object value) {
		StringReference ref = referencesFactory.createStringReference();
		ref.setValue((String) value);
		return ref;
	}

}
