package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ToArrayDimensionConverter implements Converter<Dimension, ArrayDimension> {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	public ToArrayDimensionConverter(final UtilLayout utilLayout,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			final ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilLayout = utilLayout;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayDimension convert(final Dimension dim) {
		final ArrayDimension result = arraysFactory.createArrayDimension();
		dim.annotations().forEach(
				annot -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) annot)));
		utilLayout.convertToMinimalLayoutInformation(result, dim);
		return result;
	}

}
