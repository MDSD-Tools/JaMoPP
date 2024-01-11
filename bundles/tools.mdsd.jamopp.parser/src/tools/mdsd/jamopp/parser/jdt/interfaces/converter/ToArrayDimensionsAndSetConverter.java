package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

import org.eclipse.jdt.core.dom.Type;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;

public interface ToArrayDimensionsAndSetConverter {

	void convert(Type type, ArrayTypeable arrDimContainer);

	void convert(Type type, ArrayTypeable arrDimContainer, int ignoreDimensions);

}