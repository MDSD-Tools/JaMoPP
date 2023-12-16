package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

public class HandlerCastExpression implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout utilLayout;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	HandlerCastExpression(IUtilLayout utilLayout, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		CastExpression castExpr = (CastExpression) expr;
		org.emftext.language.java.expressions.CastExpression result = expressionsFactory.createCastExpression();
		if (castExpr.getType().isIntersectionType()) {
			IntersectionType interType = (IntersectionType) castExpr.getType();
			result.setTypeReference(toTypeReferenceConverter.convert((Type) interType.types().get(0)));
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet((Type) interType.types().get(0),
					result);
			for (int index = 1; index < interType.types().size(); index++) {
				result.getAdditionalBounds().add(toTypeReferenceConverter.convert((Type) interType.types().get(index)));
			}
		} else {
			result.setTypeReference(toTypeReferenceConverter.convert(castExpr.getType()));
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(castExpr.getType(), result);
		}
		result.setGeneralChild(toExpressionConverter.convert(castExpr.getExpression()));
		utilLayout.convertToMinimalLayoutInformation(result, castExpr);
		return result;
	}

}
