package tools.mdsd.jamopp.parser.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilReferenceWalker;

public class ToPrimaryExpressionConverter implements Converter<Expression, PrimaryExpression> {

	private final LiteralsFactory literalsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final Converter<NumberLiteral, tools.mdsd.jamopp.model.java.literals.Literal> toNumberLiteralConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression;

	@Inject
	public ToPrimaryExpressionConverter(final LiteralsFactory literalsFactory,
			final Converter<NumberLiteral, tools.mdsd.jamopp.model.java.literals.Literal> toNumberLiteralConverter,
			final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression,
			final UtilReferenceWalker utilReferenceWalker) {
		this.utilReferenceWalker = utilReferenceWalker;
		this.toNumberLiteralConverter = toNumberLiteralConverter;
		this.layoutInformationConverter = layoutInformationConverter;
		this.literalsFactory = literalsFactory;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	@Override
	public PrimaryExpression convert(final Expression expr) {
		PrimaryExpression result;
		if (expr.getNodeType() == ASTNode.BOOLEAN_LITERAL) {
			result = createBooleanLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NULL_LITERAL) {
			result = createNullLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.CHARACTER_LITERAL) {
			result = createCharacterLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.NUMBER_LITERAL) {
			result = toNumberLiteralConverter.convert((NumberLiteral) expr);
		} else {
			result = utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(expr));
		}
		return result;
	}

	private PrimaryExpression createCharacterLiteral(final Expression expr) {
		final CharacterLiteral lit = (CharacterLiteral) expr;
		final tools.mdsd.jamopp.model.java.literals.CharacterLiteral result = literalsFactory.createCharacterLiteral();
		result.setValue(lit.getEscapedValue().substring(1, lit.getEscapedValue().length() - 1));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

	private PrimaryExpression createNullLiteral(final Expression expr) {
		final tools.mdsd.jamopp.model.java.literals.NullLiteral result = literalsFactory.createNullLiteral();
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.literals.BooleanLiteral createBooleanLiteral(final Expression expr) {
		final BooleanLiteral lit = (BooleanLiteral) expr;
		final tools.mdsd.jamopp.model.java.literals.BooleanLiteral result = literalsFactory.createBooleanLiteral();
		result.setValue(lit.booleanValue());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lit);
		return result;
	}

}
