package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.arrays.ArraysFactory;

public class ToArrayInitialisierConverter {

	private final UtilExpressionConverter utilExpressionConverter;
	private final UtilLayout utilLayout;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;

	public ToArrayInitialisierConverter(UtilExpressionConverter utilExpressionConverter, UtilLayout utilLayout,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.utilExpressionConverter = utilExpressionConverter;
		this.utilLayout = utilLayout;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
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
				value = toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) expr);
			} else {
				value = utilExpressionConverter.convertToExpression(expr);
			}
			result.getInitialValues().add(value);
		});
		utilLayout.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

}
