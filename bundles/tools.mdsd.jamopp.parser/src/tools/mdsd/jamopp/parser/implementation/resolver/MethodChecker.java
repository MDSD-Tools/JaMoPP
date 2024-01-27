package tools.mdsd.jamopp.parser.implementation.resolver;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;

public class MethodChecker {

	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public MethodChecker(final ToTypeNameConverter toTypeNameConverter) {
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@SuppressWarnings("unchecked")
	protected <T extends Method> T checkMethod(final Method mem, final IMethodBinding binding) {
		T result = null;
		if (mem.getName().equals(binding.getName())) {
			final T meth = (T) mem;
			if ("clone".equals(meth.getName())) {
				result = meth;
			} else {
				int receiveOffset = 0;
				if (binding.getDeclaredReceiverType() != null) {
					receiveOffset = 1;
				}
				if (binding.getParameterTypes().length + receiveOffset == meth.getParameters().size()
						&& !predicateOne(binding, meth, receiveOffset)
						&& !predicateThree(binding, mem, receiveOffset)) {
					result = meth;
				}
			}
		}
		return result;
	}

	private boolean predicateThree(final IMethodBinding binding, final Parametrizable meth, final int receiveOffset) {
		boolean result = false;
		for (int i = 0; i < binding.getParameterTypes().length; i++) {
			final ITypeBinding currentParamType = binding.getParameterTypes()[i];
			final Parameter currentParam = meth.getParameters().get(i + receiveOffset);
			if (predicateTwo(currentParamType, currentParam)) {
				result = true;
			}
		}
		return result;
	}

	private boolean predicateTwo(final ITypeBinding currentParamType, final Parameter currentParam) {
		return !toTypeNameConverter.convertToTypeName(currentParamType)
				.equals(toTypeNameConverter.convertToTypeName(currentParam.getTypeReference()))
				|| currentParamType.getDimensions() != currentParam.getArrayDimension();
	}

	private <T extends Method> boolean predicateOne(final IMethodBinding binding, final T meth,
			final int receiveOffset) {
		return receiveOffset == 1
				&& (!(meth.getParameters().get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
						|| !toTypeNameConverter.convertToTypeName(binding.getDeclaredReceiverType()).equals(
								toTypeNameConverter.convertToTypeName(meth.getParameters().get(0).getTypeReference())))
				|| !toTypeNameConverter.convertToTypeName(binding.getReturnType())
						.equals(toTypeNameConverter.convertToTypeName(meth.getTypeReference()));
	}

}
