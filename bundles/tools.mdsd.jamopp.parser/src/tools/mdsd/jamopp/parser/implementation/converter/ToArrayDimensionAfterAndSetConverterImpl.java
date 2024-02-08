package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;

public class ToArrayDimensionAfterAndSetConverterImpl implements ToArrayDimensionAfterAndSetConverter {

	private final Converter<Dimension, ArrayDimension> toArrayDimensionConverter;

	@Inject
	public ToArrayDimensionAfterAndSetConverterImpl(
			final Converter<Dimension, ArrayDimension> toArrayDimensionConverter) {
		this.toArrayDimensionConverter = toArrayDimensionConverter;
	}

	@Override
	public void convert(final Dimension dim, final ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(toArrayDimensionConverter.convert(dim));
	}

}
