package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

import org.eclipse.jdt.core.dom.Type;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;

public interface ToArrayDimensionsAndSetConverter {

	void convert(Type t, ArrayTypeable arrDimContainer);

	void convert(Type t, ArrayTypeable arrDimContainer, int ignoreDimensions);

}