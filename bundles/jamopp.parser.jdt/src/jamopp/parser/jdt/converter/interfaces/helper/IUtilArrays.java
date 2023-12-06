package jamopp.parser.jdt.converter.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;

public interface IUtilArrays {

	void convertToArrayDimensionsAndSet(ITypeBinding binding,
			org.emftext.language.java.arrays.ArrayTypeable arrDimContainer);

}