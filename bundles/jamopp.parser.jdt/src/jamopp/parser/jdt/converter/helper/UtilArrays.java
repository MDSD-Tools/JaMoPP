package jamopp.parser.jdt.converter.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.arrays.ArraysFactory;
import com.google.inject.Inject;

public class UtilArrays {

	private final ArraysFactory arraysFactory;

	@Inject
	UtilArrays(ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
	}

	public void convertToArrayDimensionsAndSet(ITypeBinding binding,
			org.emftext.language.java.arrays.ArrayTypeable arrDimContainer) {
		if (binding.isArray()) {
			for (int i = 0; i < binding.getDimensions(); i++) {
				arrDimContainer.getArrayDimensionsBefore().add(arraysFactory.createArrayDimension());
			}
		}
	}

}
