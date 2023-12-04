package jamopp.parser.jdt.converter.helper;

import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToArrayDimensionConverter;

public class ToArrayDimensionAfterAndSetConverter {

	private final ToArrayDimensionConverter toArrayDimensionConverter;

	@Inject
	ToArrayDimensionAfterAndSetConverter(ToArrayDimensionConverter toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	public void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(toArrayDimensionConverter.convert(dim));
	}

}
