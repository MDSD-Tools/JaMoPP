package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.emftext.language.java.expressions.PrimaryExpression;
import org.emftext.language.java.literals.LiteralsFactory;

class ToPrimaryExpressionConverter {

	private final ToNumberLiteralConverter toNumberLiteralConverter;
	private final UtilReferenceConverter referenceConverterUtility ;
	private final UtilLayout layoutInformationConverter;
	private final LiteralsFactory literalsFactory ;
	
	ToPrimaryExpressionConverter(LiteralsFactory literalsFactory, ToNumberLiteralConverter toNumberLiteralConverter, UtilReferenceConverter referenceConverterUtility, UtilLayout layoutInformationConverter) {
		this.toNumberLiteralConverter = toNumberLiteralConverter;
		this.referenceConverterUtility = referenceConverterUtility;
		this.layoutInformationConverter = layoutInformationConverter;
		this.literalsFactory = literalsFactory;
	}

	PrimaryExpression convertToPrimaryExpression(Expression expr) {
		if (expr.getNodeType() == ASTNode.BOOLEAN_LITERAL) {
			return createBooleanLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NULL_LITERAL) {
			return createNullLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.CHARACTER_LITERAL) {
			return createCharacterLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NUMBER_LITERAL) {
			return toNumberLiteralConverter.convert((NumberLiteral) expr);
		}
		return referenceConverterUtility.convertToReference(expr);
	}

	private PrimaryExpression createCharacterLiteral(Expression expr) {
		CharacterLiteral lit = (CharacterLiteral) expr;
		org.emftext.language.java.literals.CharacterLiteral result = literalsFactory.createCharacterLiteral();
		result.setValue(lit.getEscapedValue().substring(1, lit.getEscapedValue().length() - 1));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

	private PrimaryExpression createNullLiteral(Expression expr) {
		org.emftext.language.java.literals.NullLiteral result = literalsFactory.createNullLiteral();
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private org.emftext.language.java.literals.BooleanLiteral createBooleanLiteral(Expression expr) {
		BooleanLiteral lit = (BooleanLiteral) expr;
		org.emftext.language.java.literals.BooleanLiteral result = literalsFactory.createBooleanLiteral();
		result.setValue(lit.booleanValue());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

}
