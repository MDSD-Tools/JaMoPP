package tools.mdsd.jamopp.parser.implementation.resolver;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.Method;

public class MethodResolver {

	private final InterfaceMethodResolver interfaceMethodResolver;
	private final ClassMethodResolver classMethodResolver;

	@Inject
	public MethodResolver(final InterfaceMethodResolver interfaceMethodResolver,
			final ClassMethodResolver classMethodResolver) {
		this.interfaceMethodResolver = interfaceMethodResolver;
		this.classMethodResolver = classMethodResolver;
	}

	public Method getMethod(final IMethodBinding binding) {
		Method method;
		if (binding.getDeclaringClass().isInterface()) {
			method = interfaceMethodResolver.getByBinding(binding);
		} else {
			method = classMethodResolver.getByBinding(binding);
		}
		return method;
	}

}
