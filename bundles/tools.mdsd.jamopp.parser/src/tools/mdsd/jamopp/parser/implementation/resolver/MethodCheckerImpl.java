package tools.mdsd.jamopp.parser.implementation.resolver;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodChecker;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class MethodCheckerImpl implements MethodChecker {

	private final ToStringConverter<ITypeBinding> toTypeNameConverterFromBinding;
	private final ToStringConverter<TypeReference> toTypeNameConverterFromReference;

	@Inject
	public MethodCheckerImpl(final ToStringConverter<TypeReference> toTypeNameConverterFromReference,
			@Named("ToTypeNameConverterFromBinding") final ToStringConverter<ITypeBinding> toTypeNameConverterFromBinding) {
		this.toTypeNameConverterFromBinding = toTypeNameConverterFromBinding;
		this.toTypeNameConverterFromReference = toTypeNameConverterFromReference;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Method> T checkMethod(final Method mem, final IMethodBinding binding) {
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
		return !toTypeNameConverterFromBinding.convert(currentParamType)
				.equals(toTypeNameConverterFromReference.convert(currentParam.getTypeReference()))
				|| currentParamType.getDimensions() != currentParam.getArrayDimension();
	}

	private <T extends Method> boolean predicateOne(final IMethodBinding binding, final T meth,
			final int receiveOffset) {
		return receiveOffset == 1
				&& (!(meth.getParameters().get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
						|| !toTypeNameConverterFromBinding.convert(binding.getDeclaredReceiverType())
								.equals(toTypeNameConverterFromReference
										.convert(meth.getParameters().get(0).getTypeReference())))
				|| !toTypeNameConverterFromBinding.convert(binding.getReturnType())
						.equals(toTypeNameConverterFromReference.convert(meth.getTypeReference()));
	}

}
