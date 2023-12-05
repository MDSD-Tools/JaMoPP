package jamopp.parser.jdt.converter.helper;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.arrays.ArrayTypeable;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToArrayDimensionConverter;

public class UtilToArrayDimensionsAndSetConverter {

	private final ToArrayDimensionConverter toArrayDimensionConverter;

	@Inject
	public UtilToArrayDimensionsAndSetConverter(ToArrayDimensionConverter toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	public void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer) {
		convertToArrayDimensionsAndSet(t, arrDimContainer, 0);
	}

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
