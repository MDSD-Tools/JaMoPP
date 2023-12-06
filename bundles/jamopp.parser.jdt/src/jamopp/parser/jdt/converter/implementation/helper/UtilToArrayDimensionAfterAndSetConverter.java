package jamopp.parser.jdt.converter.implementation.helper;

import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.converter.ToArrayDimensionConverter;

public class UtilToArrayDimensionAfterAndSetConverter {

	private final ToArrayDimensionConverter toArrayDimensionConverter;

	@Inject
	UtilToArrayDimensionAfterAndSetConverter(ToArrayDimensionConverter toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	public void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(toArrayDimensionConverter.convert(dim));
	}

}
