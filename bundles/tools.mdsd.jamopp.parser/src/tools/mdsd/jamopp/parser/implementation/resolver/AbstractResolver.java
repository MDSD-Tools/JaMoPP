package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;

import tools.mdsd.jamopp.parser.interfaces.resolver.IResolver;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public abstract class AbstractResolver<C, B extends IBinding> implements IResolver<C, B> {

	private final Map<String, C> bindings;

	@Inject
	public AbstractResolver(final Map<String, C> bindings) {
		this.bindings = bindings;
	}

	@Override
	public final Map<String, C> getBindings() {
		return bindings;
	}

}
