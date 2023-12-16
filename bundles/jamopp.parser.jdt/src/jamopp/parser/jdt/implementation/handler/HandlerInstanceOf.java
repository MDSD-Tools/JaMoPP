package jamopp.parser.jdt.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.handler.ExpressionHandler;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;

public class HandlerInstanceOf implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	HandlerInstanceOf(UtilLayout utilLayout, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		InstanceofExpression castedExpr = (InstanceofExpression) expr;
		org.emftext.language.java.expressions.InstanceOfExpression result = expressionsFactory
				.createInstanceOfExpression();
		result.setChild((InstanceOfExpressionChild) toExpressionConverter.convert(castedExpr.getLeftOperand()));
		result.setTypeReference(toTypeReferenceConverter.convert(castedExpr.getRightOperand()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(castedExpr.getRightOperand(), result);
		utilLayout.convertToMinimalLayoutInformation(result, castedExpr);
		return result;
	}

}
