package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;

public class MethodChecker {

	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public MethodChecker(ToTypeNameConverter toTypeNameConverter) {
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@SuppressWarnings("unchecked")
	protected <T extends Method> T checkMethod(Method mem, IMethodBinding binding) {
		T result = null;
		if (mem.getName().equals(binding.getName())) {
			T meth = (T) mem;
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

	private boolean predicateThree(IMethodBinding binding, Parametrizable meth, int receiveOffset) {
		boolean result = false;
		for (int i = 0; i < binding.getParameterTypes().length; i++) {
			ITypeBinding currentParamType = binding.getParameterTypes()[i];
			Parameter currentParam = meth.getParameters().get(i + receiveOffset);
			if (predicateTwo(currentParamType, currentParam)) {
				result = true;
			}
		}
		return result;
	}

	private boolean predicateTwo(ITypeBinding currentParamType, Parameter currentParam) {
		return !toTypeNameConverter.convertToTypeName(currentParamType)
				.equals(toTypeNameConverter.convertToTypeName(currentParam.getTypeReference()))
				|| currentParamType.getDimensions() != currentParam.getArrayDimension();
	}

	private <T extends Method> boolean predicateOne(IMethodBinding binding, T meth, int receiveOffset) {
		return receiveOffset == 1
				&& (!(meth.getParameters().get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
						|| !toTypeNameConverter.convertToTypeName(binding.getDeclaredReceiverType()).equals(
								toTypeNameConverter.convertToTypeName(meth.getParameters().get(0).getTypeReference())))
				|| !toTypeNameConverter.convertToTypeName(binding.getReturnType())
						.equals(toTypeNameConverter.convertToTypeName(meth.getTypeReference()));
	}

}
