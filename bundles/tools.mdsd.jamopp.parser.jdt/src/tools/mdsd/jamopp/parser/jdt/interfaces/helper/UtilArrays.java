package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;

public interface UtilArrays {

	void convertToArrayDimensionsAndSet(ITypeBinding binding,
			org.emftext.language.java.arrays.ArrayTypeable arrDimContainer);

}