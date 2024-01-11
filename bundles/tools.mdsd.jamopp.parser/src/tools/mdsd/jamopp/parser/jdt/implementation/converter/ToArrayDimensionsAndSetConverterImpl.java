package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;

public class ToArrayDimensionsAndSetConverterImpl implements ToArrayDimensionsAndSetConverter {

	private final Converter<Dimension, ArrayDimension> toArrayDimensionConverter;

	@Inject
	public ToArrayDimensionsAndSetConverterImpl(Converter<Dimension, ArrayDimension> toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	@Override
	public void convert(Type type, ArrayTypeable arrDimContainer) {
		convert(type, arrDimContainer, 0);
	}

	@Override
	public void convert(Type type, ArrayTypeable arrDimContainer, int ignoreDimensions) {
		if (type.isArrayType()) {
			ArrayType arrT = (ArrayType) type;
			for (int i = ignoreDimensions; i < arrT.dimensions().size(); i++) {
				arrDimContainer.getArrayDimensionsBefore()
						.add(toArrayDimensionConverter.convert((Dimension) arrT.dimensions().get(i)));
			}
		}
	}

}
