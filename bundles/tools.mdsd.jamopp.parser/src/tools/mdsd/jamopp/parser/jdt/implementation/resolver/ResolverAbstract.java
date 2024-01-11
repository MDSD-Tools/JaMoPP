package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;

import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.Resolver;

public abstract class ResolverAbstract<Clazz, BindingType extends IBinding> implements Resolver<Clazz, BindingType> {

	private final Map<String, Clazz> bindings;

	@Inject
	public ResolverAbstract(Map<String, Clazz> bindings) {
		this.bindings = bindings;
	}

	@Override
	public final Map<String, Clazz> getBindings() {
		return bindings;
	}

	@Override
	public abstract Clazz getByBinding(BindingType binding);

	@Override
	public abstract Clazz getByName(String name);

}
