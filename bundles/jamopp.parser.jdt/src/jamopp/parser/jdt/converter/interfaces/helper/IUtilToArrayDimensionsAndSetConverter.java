package jamopp.parser.jdt.converter.interfaces.helper;

import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.arrays.ArrayTypeable;

public interface IUtilToArrayDimensionsAndSetConverter {

	void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer);

	void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer, int ignoreDimensions);

}