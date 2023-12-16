package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToArrayInitialisierConverter
		implements Converter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> {

	private final ArraysFactory arraysFactory;
	private final IUtilLayout utilLayout;
	private Converter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter;
	private Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToArrayInitialisierConverter(IUtilLayout utilLayout, ArraysFactory arraysFactory) {
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
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@Inject
	public void setUtilExpressionConverter(
			Converter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter) {
		this.utilExpressionConverter = utilExpressionConverter;
	}

}
