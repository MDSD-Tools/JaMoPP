package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

public class HandlerInstanceOf implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	HandlerInstanceOf(UtilLayout utilLayout, ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
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
