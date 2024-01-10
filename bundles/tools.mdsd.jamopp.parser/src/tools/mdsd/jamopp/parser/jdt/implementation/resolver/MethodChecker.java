package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import javax.inject.Inject;

public class MethodChecker {

	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public MethodChecker(ToTypeNameConverter toTypeNameConverter) {
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@SuppressWarnings("unchecked")
	protected <T extends tools.mdsd.jamopp.model.java.members.Method> T checkMethod(
			tools.mdsd.jamopp.model.java.members.Method mem, IMethodBinding binding) {
		if (mem.getName().equals(binding.getName())) {
			T meth = (T) mem;
			if ("clone".equals(meth.getName())) {
				return meth;
			}
			int receiveOffset = 0;
			if (binding.getDeclaredReceiverType() != null) {
				receiveOffset = 1;
			}
			if (binding.getParameterTypes().length + receiveOffset == meth.getParameters().size()) {
				if (receiveOffset == 1
						&& (!(meth.getParameters()
								.get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
								|| !toTypeNameConverter.convertToTypeName(binding.getDeclaredReceiverType())
										.equals(toTypeNameConverter
												.convertToTypeName(meth.getParameters().get(0).getTypeReference())))
						|| !toTypeNameConverter.convertToTypeName(binding.getReturnType())
								.equals(toTypeNameConverter.convertToTypeName(meth.getTypeReference()))) {
					return null;
				}
				for (int i = 0; i < binding.getParameterTypes().length; i++) {
					ITypeBinding currentParamType = binding.getParameterTypes()[i];
					tools.mdsd.jamopp.model.java.parameters.Parameter currentParam = meth.getParameters()
							.get(i + receiveOffset);
					if (!toTypeNameConverter.convertToTypeName(currentParamType)
							.equals(toTypeNameConverter.convertToTypeName(currentParam.getTypeReference()))
							|| currentParamType.getDimensions() != currentParam.getArrayDimension()) {
						return null;
					}
				}
				return meth;
			}
		}
		return null;
	}

}
