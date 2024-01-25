package tools.mdsd.jamopp.parser.implementation.resolver;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;

public class MethodResolver {

	private final InterfaceMethodResolver interfaceMethodResolver;
	private final ClassMethodResolver classMethodResolver;

	@Inject
	public MethodResolver(final InterfaceMethodResolver interfaceMethodResolver,
			final ClassMethodResolver classMethodResolver) {
		this.interfaceMethodResolver = interfaceMethodResolver;
		this.classMethodResolver = classMethodResolver;
	}

	public tools.mdsd.jamopp.model.java.members.Method getMethod(final IMethodBinding binding) {
		tools.mdsd.jamopp.model.java.members.Method method;
		if (binding.getDeclaringClass().isInterface()) {
			method = interfaceMethodResolver.getByBinding(binding);
		} else {
			method = classMethodResolver.getByBinding(binding);
		}
		return method;
	}

}
