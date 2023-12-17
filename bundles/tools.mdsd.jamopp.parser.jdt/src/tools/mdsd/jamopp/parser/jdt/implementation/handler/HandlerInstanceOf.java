package tools.mdsd.jamopp.parser.jdt.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Type;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.handler.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;

public class HandlerInstanceOf implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	HandlerInstanceOf(UtilLayout utilLayout, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		InstanceofExpression castedExpr = (InstanceofExpression) expr;
		tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression result = expressionsFactory
				.createInstanceOfExpression();
		result.setChild((InstanceOfExpressionChild) toExpressionConverter.convert(castedExpr.getLeftOperand()));
		result.setTypeReference(toTypeReferenceConverter.convert(castedExpr.getRightOperand()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(castedExpr.getRightOperand(), result);
		utilLayout.convertToMinimalLayoutInformation(result, castedExpr);
		return result;
	}

}
