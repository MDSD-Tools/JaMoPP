package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.eclipse.jdt.core.dom.IBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public abstract class AbstractResolverWithCache<C, B extends IBinding> implements ResolverWithCache<C, B> {

	private final Map<String, C> bindings;

	@Inject
	public AbstractResolverWithCache(final Map<String, C> bindings) {
		this.bindings = bindings;
	}

	@Override
	public void clearBindings() {
		bindings.clear();
	}

	@Override
	public void forEachBinding(final BiConsumer<? super String, ? super C> biConsumer) {
		bindings.forEach(biConsumer);
	}

	@Override
	public void forEachBindingOnCopy(final BiConsumer<? super String, ? super C> biConsumer) {
		new HashMap<>(bindings).forEach(biConsumer);
	}

	@Override
	public int bindingsSize() {
		return bindings.size();
	}

	@Override
	public Collection<C> getBindings() {
		return bindings.values();
	}

	@Override
	public boolean containsKey(final String varName) {
		return bindings.containsKey(varName);
	}

	public C get(final String varName) {
		return bindings.get(varName);
	}

	public void putBinding(final String name, final C value) {
		bindings.put(name, value);
	}

}
