package jamopp.parser.jdt.converter.implementation.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.arrays.ArraysFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.helper.IUtilArrays;

public class UtilArrays implements IUtilArrays {

	private final ArraysFactory arraysFactory;

	@Inject
	UtilArrays(ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
	}

	@Override
	public void convertToArrayDimensionsAndSet(ITypeBinding binding,
			org.emftext.language.java.arrays.ArrayTypeable arrDimContainer) {
		if (binding.isArray()) {
			for (int i = 0; i < binding.getDimensions(); i++) {
				arrDimContainer.getArrayDimensionsBefore().add(arraysFactory.createArrayDimension());
			}
		}
	}

}
