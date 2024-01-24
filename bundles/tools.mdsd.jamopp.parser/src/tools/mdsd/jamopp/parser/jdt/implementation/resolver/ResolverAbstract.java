package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;

import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.IResolver;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public abstract class ResolverAbstract<C, B extends IBinding> implements IResolver<C, B> {

	private final Map<String, C> bindings;

	@Inject
	public ResolverAbstract(final Map<String, C> bindings) {
		this.bindings = bindings;
	}

	@Override
	public final Map<String, C> getBindings() {
		return bindings;
	}

}
