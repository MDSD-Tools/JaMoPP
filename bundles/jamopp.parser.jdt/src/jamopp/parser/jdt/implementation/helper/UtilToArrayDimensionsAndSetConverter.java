package jamopp.parser.jdt.implementation.helper;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayTypeable;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

public class UtilToArrayDimensionsAndSetConverter implements IUtilToArrayDimensionsAndSetConverter {

	private final Converter<Dimension, ArrayDimension> toArrayDimensionConverter;

	@Inject
	public UtilToArrayDimensionsAndSetConverter(Converter<Dimension, ArrayDimension> toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	@Override
	public void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer) {
		convertToArrayDimensionsAndSet(t, arrDimContainer, 0);
	}

	@Override
	public void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer, int ignoreDimensions) {
		if (t.isArrayType()) {
			ArrayType arrT = (ArrayType) t;
			for (int i = ignoreDimensions; i < arrT.dimensions().size(); i++) {
				arrDimContainer.getArrayDimensionsBefore()
						.add(toArrayDimensionConverter.convert((Dimension) arrT.dimensions().get(i)));
			}
		}
	}

}
