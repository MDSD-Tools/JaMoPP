package jamopp.parser.jdt.converter.interfaces.helper;

import org.eclipse.jdt.core.dom.Dimension;
import org.emftext.language.java.arrays.ArrayTypeable;

public interface IUtilToArrayDimensionAfterAndSetConverter {

	void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer);

}