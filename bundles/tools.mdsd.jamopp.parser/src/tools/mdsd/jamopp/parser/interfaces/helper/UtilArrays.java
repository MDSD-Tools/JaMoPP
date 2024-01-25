package tools.mdsd.jamopp.parser.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;

public interface UtilArrays {

	void convertToArrayDimensionsAndSet(ITypeBinding binding,
			tools.mdsd.jamopp.model.java.arrays.ArrayTypeable arrDimContainer);

}