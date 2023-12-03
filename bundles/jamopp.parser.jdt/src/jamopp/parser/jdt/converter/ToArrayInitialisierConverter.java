package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.util.UtilLayout;

public class ToArrayInitialisierConverter {

	private final ArraysFactory arraysFactory;
	private final ToExpressionConverter utilExpressionConverter;
	private final UtilLayout utilLayout;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;

	@Inject
	ToArrayInitialisierConverter(ToExpressionConverter utilExpressionConverter, UtilLayout utilLayout,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter, ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilExpressionConverter = utilExpressionConverter;
		this.utilLayout = utilLayout;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@SuppressWarnings("unchecked")
	public org.emftext.language.java.arrays.ArrayInitializer convertToArrayInitializer(ArrayInitializer arr) {
		org.emftext.language.java.arrays.ArrayInitializer result = arraysFactory.createArrayInitializer();
		arr.expressions().forEach(obj -> {
			org.emftext.language.java.arrays.ArrayInitializationValue value = null;
			Expression expr = (Expression) obj;
			if (expr instanceof ArrayInitializer) {
				value = convertToArrayInitializer((ArrayInitializer) expr);
			} else if (expr instanceof Annotation) {
				value = toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) expr);
			} else {
				value = utilExpressionConverter.convert(expr);
			}
			result.getInitialValues().add(value);
		});
		utilLayout.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

}
