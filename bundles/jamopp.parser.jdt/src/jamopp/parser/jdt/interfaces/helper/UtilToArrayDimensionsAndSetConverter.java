package jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.arrays.ArrayTypeable;

public interface UtilToArrayDimensionsAndSetConverter {

	void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer);

	void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer, int ignoreDimensions);

}