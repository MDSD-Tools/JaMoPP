package jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.arrays.ArrayTypeable;

public interface UtilArrays {

	void convertToArrayDimensionsAndSet(ITypeBinding binding, ArrayTypeable arrDimContainer);

}