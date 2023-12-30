package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

import org.eclipse.jdt.core.dom.Dimension;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;

public interface ToArrayDimensionAfterAndSetConverter {

	void convert(Dimension dim, ArrayTypeable arrDimContainer);

}