package tools.mdsd.jamopp.parser.implementation.resolver;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;

public class MethodResolverImpl implements MethodResolver {

	private final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver;
	private final ResolverWithCache<ClassMethod, IMethodBinding> classMethodResolver;

	@Inject
	public MethodResolverImpl(final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver,
			final ResolverWithCache<ClassMethod, IMethodBinding> classMethodResolver) {
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
