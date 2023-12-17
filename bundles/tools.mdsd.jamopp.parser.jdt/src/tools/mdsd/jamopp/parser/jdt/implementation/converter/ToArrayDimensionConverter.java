package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToArrayDimensionConverter implements Converter<Dimension, ArrayDimension> {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToArrayDimensionConverter(UtilLayout utilLayout,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter, ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilLayout = utilLayout;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@SuppressWarnings("unchecked")
	public ArrayDimension convert(Dimension dim) {
		ArrayDimension result = arraysFactory.createArrayDimension();
		dim.annotations().forEach(
				annot -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) annot)));
		utilLayout.convertToMinimalLayoutInformation(result, dim);
		return result;
	}

}