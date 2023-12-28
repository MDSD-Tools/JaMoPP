package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import org.eclipse.jdt.core.dom.IMethodBinding;

public class MethodResolver {

	private final InterfaceMethodResolver interfaceMethodResolver;
	private final ClassMethodResolver classMethodResolver;

	public MethodResolver(InterfaceMethodResolver interfaceMethodResolver, ClassMethodResolver classMethodResolver) {
		this.interfaceMethodResolver = interfaceMethodResolver;
		this.classMethodResolver = classMethodResolver;
	}

	public tools.mdsd.jamopp.model.java.members.Method getMethod(IMethodBinding binding) {
		if (binding.getDeclaringClass().isInterface()) {
			return interfaceMethodResolver.getByBinding(binding);
		}
		return classMethodResolver.getByBinding(binding);
	}

}
