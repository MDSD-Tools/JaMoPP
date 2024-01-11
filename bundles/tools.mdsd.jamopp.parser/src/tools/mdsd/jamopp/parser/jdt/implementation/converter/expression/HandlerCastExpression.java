package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class HandlerCastExpression implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	public HandlerCastExpression(UtilLayout utilLayout, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory,
			ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		CastExpression castExpr = (CastExpression) expr;
		tools.mdsd.jamopp.model.java.expressions.CastExpression result = expressionsFactory.createCastExpression();
		if (castExpr.getType().isIntersectionType()) {
			IntersectionType interType = (IntersectionType) castExpr.getType();
			result.setTypeReference(toTypeReferenceConverter.convert((Type) interType.types().get(0)));
			utilToArrayDimensionsAndSetConverter.convert((Type) interType.types().get(0), result);
			for (int index = 1; index < interType.types().size(); index++) {
				result.getAdditionalBounds().add(toTypeReferenceConverter.convert((Type) interType.types().get(index)));
			}
		} else {
			result.setTypeReference(toTypeReferenceConverter.convert(castExpr.getType()));
			utilToArrayDimensionsAndSetConverter.convert(castExpr.getType(), result);
		}
		result.setGeneralChild(toExpressionConverter.convert(castExpr.getExpression()));
		utilLayout.convertToMinimalLayoutInformation(result, castExpr);
		return result;
	}

}
