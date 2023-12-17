package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;

public class ToPrimaryExpressionConverter
		implements Converter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> {

	private final LiteralsFactory literalsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final Converter<NumberLiteral, tools.mdsd.jamopp.model.java.literals.Literal> toNumberLiteralConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression;

	@Inject
	ToPrimaryExpressionConverter(LiteralsFactory literalsFactory, ToNumberLiteralConverter toNumberLiteralConverter,
			UtilLayout layoutInformationConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression,
			UtilReferenceWalker utilReferenceWalker) {
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
		tools.mdsd.jamopp.model.java.literals.CharacterLiteral result = literalsFactory.createCharacterLiteral();
		result.setValue(lit.getEscapedValue().substring(1, lit.getEscapedValue().length() - 1));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

	private PrimaryExpression createNullLiteral(Expression expr) {
		tools.mdsd.jamopp.model.java.literals.NullLiteral result = literalsFactory.createNullLiteral();
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.literals.BooleanLiteral createBooleanLiteral(Expression expr) {
		BooleanLiteral lit = (BooleanLiteral) expr;
		tools.mdsd.jamopp.model.java.literals.BooleanLiteral result = literalsFactory.createBooleanLiteral();
		result.setValue(lit.booleanValue());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

}
