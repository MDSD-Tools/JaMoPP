package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToArrayInitialisierConverter
		implements ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter;
	private ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToArrayInitialisierConverter(UtilLayout utilLayout, ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilLayout = utilLayout;
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

	@Inject
	public void setToAnnotationInstanceConverter(
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@Inject
	public void setUtilExpressionConverter(
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter) {
		this.utilExpressionConverter = utilExpressionConverter;
	}

}
