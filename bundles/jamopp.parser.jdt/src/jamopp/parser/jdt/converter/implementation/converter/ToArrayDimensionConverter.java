package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToArrayDimensionConverter implements ToConverter<Dimension, ArrayDimension> {

	private final ArraysFactory arraysFactory;
	private final IUtilLayout utilLayout;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToArrayDimensionConverter(IUtilLayout utilLayout,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter, ArraysFactory arraysFactory) {
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
