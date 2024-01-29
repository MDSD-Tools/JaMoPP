package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.eclipse.jdt.core.dom.IBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.interfaces.resolver.IResolver;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public abstract class ResolverWithCache<C, B extends IBinding> implements IResolver<C, B> {

	private final Map<String, C> bindings;

	@Inject
	public ResolverWithCache(final Map<String, C> bindings) {
		this.bindings = bindings;
	}

	protected boolean containsKey(final String varName) {
		return bindings.containsKey(varName);
	}

	protected C get(final String varName) {
		return bindings.get(varName);
	}

	protected void putBinding(final String name, final C value) {
		bindings.put(name, value);
	}

	public void clearBindings() {
		bindings.clear();
	}

	public void forEachBinding(final BiConsumer<? super String, ? super C> biConsumer) {
		bindings.forEach(biConsumer);
	}

	public void forEachBindingOnCopy(final BiConsumer<? super String, ? super C> biConsumer) {
		new HashMap<>(bindings).forEach(biConsumer);
	}

	public int bindingsSize() {
		return bindings.size();
	}

	public Collection<C> getBindings() {
		return bindings.values();
	}

}
