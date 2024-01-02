package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;

import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToArrayInitialisierConverter
		implements Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> utilExpressionConverter;
	private Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToArrayInitialisierConverter(UtilLayout utilLayout, ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public tools.mdsd.jamopp.model.java.arrays.ArrayInitializer convert(ArrayInitializer arr) {
		tools.mdsd.jamopp.model.java.arrays.ArrayInitializer result = arraysFactory.createArrayInitializer();
		arr.expressions().forEach(obj -> {
			tools.mdsd.jamopp.model.java.arrays.ArrayInitializationValue value = null;
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
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> utilExpressionConverter) {
		this.utilExpressionConverter = utilExpressionConverter;
	}

}
