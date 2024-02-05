package tools.mdsd.jamopp.parser.implementation.resolver;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodResolver;

public class MethodResolverImpl implements MethodResolver {

	private final InterfaceMethodResolver interfaceMethodResolver;
	private final ClassMethodResolver classMethodResolver;

	@Inject
	public MethodResolverImpl(final InterfaceMethodResolver interfaceMethodResolver,
			final ClassMethodResolver classMethodResolver) {
		this.interfaceMethodResolver = interfaceMethodResolver;
		this.classMethodResolver = classMethodResolver;
	}

	@Override
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
