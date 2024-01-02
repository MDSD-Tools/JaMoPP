package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;

import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.Resolver;

public abstract class ResolverAbstract<Clazz, BindingType extends IBinding> implements Resolver<Clazz, BindingType> {

	private final HashMap<String, Clazz> bindings;

	@Inject
	public ResolverAbstract(HashMap<String, Clazz> bindings) {
		this.bindings = bindings;
	}

	@Override
	public final HashMap<String, Clazz> getBindings() {
		return bindings;
	}

	@Override
	public abstract Clazz getByBinding(BindingType binding);

	@Override
	public abstract Clazz getByName(String name);

}
