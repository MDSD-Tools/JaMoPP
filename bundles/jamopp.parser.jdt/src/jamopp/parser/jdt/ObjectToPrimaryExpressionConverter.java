package jamopp.parser.jdt;

import java.math.BigInteger;

import org.emftext.language.java.literals.BooleanLiteral;
import org.emftext.language.java.literals.CharacterLiteral;
import org.emftext.language.java.literals.DecimalDoubleLiteral;
import org.emftext.language.java.literals.DecimalFloatLiteral;
import org.emftext.language.java.literals.DecimalIntegerLiteral;
import org.emftext.language.java.literals.DecimalLongLiteral;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.references.StringReference;
import com.google.inject.Inject;

class ObjectToPrimaryExpressionConverter {

	private final ReferencesFactory referencesFactory;
	private final LiteralsFactory literalsFactory;

	@Inject
	ObjectToPrimaryExpressionConverter(ReferencesFactory referencesFactory, LiteralsFactory literalsFactory) {
		this.referencesFactory = referencesFactory;
		this.literalsFactory = literalsFactory;
	}

	org.emftext.language.java.expressions.PrimaryExpression convertToPrimaryExpression(Object value) {
		if (value instanceof String) {
			StringReference ref = referencesFactory.createStringReference();
			ref.setValue((String) value);
			return ref;
		}
		if (value instanceof Boolean) {
			BooleanLiteral literal = literalsFactory.createBooleanLiteral();
			literal.setValue((boolean) value);
			return literal;
		}
		if (value instanceof Character) {
			CharacterLiteral literal = literalsFactory.createCharacterLiteral();
			literal.setValue("\\u" + Integer.toHexString((Character) value));
			return literal;
		}
		if (value instanceof Byte) {
			DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((byte) value));
			return literal;
		}
		if (value instanceof Short) {
			DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((short) value));
			return literal;
		}
		if (value instanceof Integer) {
			DecimalIntegerLiteral literal = literalsFactory.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((int) value));
			return literal;
		}
		if (value instanceof Long) {
			DecimalLongLiteral literal = literalsFactory.createDecimalLongLiteral();
			literal.setDecimalValue(BigInteger.valueOf((long) value));
			return literal;
		}
		if (value instanceof Float) {
			DecimalFloatLiteral literal = literalsFactory.createDecimalFloatLiteral();
			literal.setDecimalValue((float) value);
			return literal;
		}
		if (value instanceof Double) {
			DecimalDoubleLiteral literal = literalsFactory.createDecimalDoubleLiteral();
			literal.setDecimalValue((double) value);
			return literal;
		}
		return literalsFactory.createNullLiteral();
	}

}
