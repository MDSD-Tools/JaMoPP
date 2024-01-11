package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionAfterAndSetConverter;

public class ToArrayDimensionAfterAndSetConverterImpl implements ToArrayDimensionAfterAndSetConverter {

	private final Converter<Dimension, ArrayDimension> toArrayDimensionConverter;

	@Inject
	public ToArrayDimensionAfterAndSetConverterImpl(Converter<Dimension, ArrayDimension> toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	@Override
	public void convert(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(toArrayDimensionConverter.convert(dim));
	}

}
