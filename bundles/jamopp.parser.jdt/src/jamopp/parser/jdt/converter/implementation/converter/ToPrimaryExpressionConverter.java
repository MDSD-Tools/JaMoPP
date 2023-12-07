package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.emftext.language.java.expressions.PrimaryExpression;
import org.emftext.language.java.literals.LiteralsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilReferenceWalker;

public class ToPrimaryExpressionConverter
		implements ToConverter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> {

	private final LiteralsFactory literalsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilReferenceWalker utilReferenceWalker;
	private final ToConverter<NumberLiteral, org.emftext.language.java.literals.Literal> toNumberLiteralConverter;
	private final ToConverter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression;

	@Inject
	ToPrimaryExpressionConverter(LiteralsFactory literalsFactory, ToNumberLiteralConverter toNumberLiteralConverter,
			IUtilLayout layoutInformationConverter,
			ToConverter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression,
			IUtilReferenceWalker utilReferenceWalker) {
		this.utilReferenceWalker = utilReferenceWalker;
		this.toNumberLiteralConverter = toNumberLiteralConverter;
		this.layoutInformationConverter = layoutInformationConverter;
		this.literalsFactory = literalsFactory;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	public PrimaryExpression convert(Expression expr) {
		if (expr.getNodeType() == ASTNode.BOOLEAN_LITERAL) {
			return createBooleanLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NULL_LITERAL) {
			return createNullLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.CHARACTER_LITERAL) {
			return createCharacterLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NUMBER_LITERAL) {
			return toNumberLiteralConverter.convert((NumberLiteral) expr);
		}
		return utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(expr));
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
