package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import org.emftext.language.java.arrays.ArraysFactory;

class ToArrayDimensionAfterAndSetConverter {

	private final UtilBaseConverter utilBaseConverter;
	private final UtilLayout utilLayout;

	ToArrayDimensionAfterAndSetConverter(UtilBaseConverter utilBaseConverter, UtilLayout utilLayout) {
		this.utilBaseConverter = utilBaseConverter;
		this.utilLayout = utilLayout;
	}

	void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(convertToArrayDimension(dim));
	}

	@SuppressWarnings("unchecked") ArrayDimension convertToArrayDimension(Dimension dim) {
		ArrayDimension result = ArraysFactory.eINSTANCE.createArrayDimension();
		dim.annotations().forEach(annot -> result.getAnnotations()
				.add(utilBaseConverter.convertToAnnotationInstance((Annotation) annot)));
		utilLayout.convertToMinimalLayoutInformation(result, dim);
		return result;
	}

}
