package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class ToArrayInitialisierConverter
		implements ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToArrayInitialisierConverter(
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter,
			UtilLayout utilLayout, ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilExpressionConverter = utilExpressionConverter;
		this.utilLayout = utilLayout;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.arrays.ArrayInitializer convert(ArrayInitializer arr) {
		org.emftext.language.java.arrays.ArrayInitializer result = arraysFactory.createArrayInitializer();
		arr.expressions().forEach(obj -> {
			org.emftext.language.java.arrays.ArrayInitializationValue value = null;
			Expression expr = (Expression) obj;
			if (expr instanceof ArrayInitializer) {
				value = convert((ArrayInitializer) expr);
			} else if (expr instanceof Annotation) {
				value = toAnnotationInstanceConverter.convert((Annotation) expr);
			} else {
				value = utilExpressionConverter.convert(expr);
			}
			result.getInitialValues().add(value);
		});
		utilLayout.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

}
