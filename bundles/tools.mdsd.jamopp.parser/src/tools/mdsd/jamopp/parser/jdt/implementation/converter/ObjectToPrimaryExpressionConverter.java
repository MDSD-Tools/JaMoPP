package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.math.BigInteger;

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

	@Inject
	public ObjectToPrimaryExpressionConverter(ReferencesFactory referencesFactory, LiteralsFactory literalsFactory) {
		this.referencesFactory = referencesFactory;
		this.literalsFactory = literalsFactory;
	}

	@Override
	public PrimaryExpression convert(Object value) {
		PrimaryExpression result;
		if (value instanceof String) {
			StringReference ref = referencesFactory.createStringReference();
			ref.setValue((String) value);
			result = ref;
		} else if (value instanceof Boolean) {
			BooleanLiteral literal = literalsFactory.createBooleanLiteral();
			literal.setValue((boolean) value);
			result = literal;
		} else if (value instanceof Character) {
			CharacterLiteral literal = literalsFactory.createCharacterLiteral();
			literal.setValue("\\u" + Integer.toHexString((Character) value));
			result = literal;
		} else if (value instanceof Byte) {
			DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((byte) value));
			result = literal;
		} else if (value instanceof Short) {
			DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((short) value));
			result = literal;
		} else if (value instanceof Integer) {
			DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((int) value));
			result = literal;
		} else if (value instanceof Long) {
			DecimalLongLiteral literal = literalsFactory.createDecimalLongLiteral();
			literal.setDecimalValue(BigInteger.valueOf((long) value));
			result = literal;
		} else if (value instanceof Float) {
			DecimalFloatLiteral literal = literalsFactory.createDecimalFloatLiteral();
			literal.setDecimalValue((float) value);
			result = literal;
		} else if (value instanceof Double) {
			DecimalDoubleLiteral literal = literalsFactory.createDecimalDoubleLiteral();
			literal.setDecimalValue((double) value);
			result = literal;
		} else {
			result = literalsFactory.createNullLiteral();
		}
		return result;
	}

}