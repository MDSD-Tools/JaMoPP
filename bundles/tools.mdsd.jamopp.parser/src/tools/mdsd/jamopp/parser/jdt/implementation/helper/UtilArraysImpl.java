package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;

public class UtilArraysImpl implements UtilArrays {

	private final ArraysFactory arraysFactory;

	@Inject
	public UtilArraysImpl(ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
	}

	@Override
	public void convertToArrayDimensionsAndSet(ITypeBinding binding,
			tools.mdsd.jamopp.model.java.arrays.ArrayTypeable arrDimContainer) {
		if (binding.isArray()) {
			for (int i = 0; i < binding.getDimensions(); i++) {
				arrDimContainer.getArrayDimensionsBefore().add(arraysFactory.createArrayDimension());
			}
		}
	}

}
