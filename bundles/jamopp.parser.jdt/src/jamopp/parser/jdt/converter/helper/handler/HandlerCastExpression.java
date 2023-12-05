package jamopp.parser.jdt.converter.helper.handler;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.implementation.ToTypeReferenceConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class HandlerCastExpression implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final ToExpressionConverter toExpressionConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;

	@Inject
	HandlerCastExpression(UtilLayout utilLayout, ToTypeReferenceConverter toTypeReferenceConverter,
			ToExpressionConverter toExpressionConverter, ExpressionsFactory expressionsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet((Type) interType.types().get(0), result);
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
