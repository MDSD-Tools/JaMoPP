package jamopp.parser.jdt.implementation.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.arrays.ArraysFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.helper.UtilArrays;

public class UtilArraysImpl implements UtilArrays {

	private final ArraysFactory arraysFactory;

	@Inject
	UtilArraysImpl(ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
	}

	@Override
	public void convertToArrayDimensionsAndSet(ITypeBinding binding,
			org.emftext.language.java.arrays.ArrayTypeable arrDimContainer) {
		if (binding.isArray()) {
			for (var i = 0; i < binding.getDimensions(); i++) {
				arrDimContainer.getArrayDimensionsBefore().add(this.arraysFactory.createArrayDimension());
			}
		}
	}

}
