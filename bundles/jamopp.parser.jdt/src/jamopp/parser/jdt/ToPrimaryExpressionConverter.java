package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.emftext.language.java.expressions.PrimaryExpression;
import org.emftext.language.java.literals.LiteralsFactory;

public class ToPrimaryExpressionConverter {

	private static final LiteralsFactory LITERALS_FACTORY = LiteralsFactory.eINSTANCE;

	PrimaryExpression convertToPrimaryExpression(Expression expr) {
		if (expr.getNodeType() == ASTNode.BOOLEAN_LITERAL) {
			return createBooleanLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NULL_LITERAL) {
			return createNullLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.CHARACTER_LITERAL) {
			return createCharacterLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NUMBER_LITERAL) {
			return ToNumberLiteralConverter.convert((NumberLiteral) expr);
		}
		return ReferenceConverterUtility.convertToReference(expr);
	}

	private PrimaryExpression createCharacterLiteral(Expression expr) {
		CharacterLiteral lit = (CharacterLiteral) expr;
		org.emftext.language.java.literals.CharacterLiteral result = LITERALS_FACTORY.createCharacterLiteral();
		result.setValue(lit.getEscapedValue().substring(1, lit.getEscapedValue().length() - 1));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

	private PrimaryExpression createNullLiteral(Expression expr) {
		org.emftext.language.java.literals.NullLiteral result = LITERALS_FACTORY.createNullLiteral();
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private org.emftext.language.java.literals.BooleanLiteral createBooleanLiteral(Expression expr) {
		BooleanLiteral lit = (BooleanLiteral) expr;
		org.emftext.language.java.literals.BooleanLiteral result = LITERALS_FACTORY.createBooleanLiteral();
		result.setValue(lit.booleanValue());
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

}
