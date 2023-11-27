package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.arrays.ArraysFactory;

public class ToArrayInitialisierConverter {

	private final UtilBaseConverter utilBaseConverter;
	private final UtilExpressionConverter utilExpressionConverter;
	private final UtilLayout utilLayout;

	public ToArrayInitialisierConverter(UtilExpressionConverter utilExpressionConverter,
			UtilBaseConverter utilBaseConverter, UtilLayout utilLayout) {
		this.utilBaseConverter = utilBaseConverter;
		this.utilExpressionConverter = utilExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.arrays.ArrayInitializer convertToArrayInitializer(ArrayInitializer arr) {
		org.emftext.language.java.arrays.ArrayInitializer result = ArraysFactory.eINSTANCE.createArrayInitializer();
		arr.expressions().forEach(obj -> {
			org.emftext.language.java.arrays.ArrayInitializationValue value = null;
			Expression expr = (Expression) obj;
			if (expr instanceof ArrayInitializer) {
				value = convertToArrayInitializer((ArrayInitializer) expr);
			} else if (expr instanceof Annotation) {
				value = utilBaseConverter.convertToAnnotationInstance((Annotation) expr);
			} else {
				value = utilExpressionConverter.convertToExpression(expr);
			}
			result.getInitialValues().add(value);
		});
		utilLayout.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

}
