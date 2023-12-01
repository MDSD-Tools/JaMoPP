package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

class ToArrayDimensionAfterAndSetConverter {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;

	@Inject
	ToArrayDimensionAfterAndSetConverter(UtilLayout utilLayout,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter, ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.utilLayout = utilLayout;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(convertToArrayDimension(dim));
	}

	@SuppressWarnings("unchecked")
	ArrayDimension convertToArrayDimension(Dimension dim) {
		ArrayDimension result = arraysFactory.createArrayDimension();
		dim.annotations().forEach(annot -> result.getAnnotations()
				.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) annot)));
		utilLayout.convertToMinimalLayoutInformation(result, dim);
		return result;
	}

}
