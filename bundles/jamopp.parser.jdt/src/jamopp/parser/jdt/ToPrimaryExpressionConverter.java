package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;

public class ToPrimaryExpressionConverter {

	org.emftext.language.java.expressions.PrimaryExpression convertToPrimaryExpression(Expression expr) {
		if (expr.getNodeType() == ASTNode.BOOLEAN_LITERAL) {
			BooleanLiteral lit = (BooleanLiteral) expr;
			org.emftext.language.java.literals.BooleanLiteral result = org.emftext.language.java.literals.LiteralsFactory.eINSTANCE.createBooleanLiteral();
			result.setValue(lit.booleanValue());
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
			return result;
		}
		if (expr.getNodeType() == ASTNode.NULL_LITERAL) {
			org.emftext.language.java.literals.NullLiteral result = org.emftext.language.java.literals.LiteralsFactory.eINSTANCE.createNullLiteral();
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.CHARACTER_LITERAL) {
			CharacterLiteral lit = (CharacterLiteral) expr;
			org.emftext.language.java.literals.CharacterLiteral result = org.emftext.language.java.literals.LiteralsFactory.eINSTANCE.createCharacterLiteral();
			result.setValue(lit.getEscapedValue().substring(1, lit.getEscapedValue().length() - 1));
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
			return result;
		}
		if (expr.getNodeType() == ASTNode.NUMBER_LITERAL) {
			return NumberLiteralConverterUtility.convertToLiteral((NumberLiteral) expr);
		}
		return ReferenceConverterUtility.convertToReference(expr);
	}
	
}
