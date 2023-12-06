package jamopp.parser.jdt.converter.implementation.helper;

import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;

public class UtilToArrayDimensionAfterAndSetConverter implements IUtilToArrayDimensionAfterAndSetConverter {

	private final ToConverter<Dimension, ArrayDimension> toArrayDimensionConverter;

	@Inject
	UtilToArrayDimensionAfterAndSetConverter(ToConverter<Dimension, ArrayDimension> toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	@Override
	public void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(toArrayDimensionConverter.convert(dim));
	}

}
