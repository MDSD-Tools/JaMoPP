package tools.mdsd.jamopp.parser.interfaces.resolver;

import java.util.Collection;
import java.util.function.BiConsumer;

import org.eclipse.jdt.core.dom.IBinding;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public interface ResolverWithCache<C, B extends IBinding> extends ResolverWithName<C, B> {

	Collection<C> getBindings();

	int bindingsSize();

	void clearBindings();

	void forEachBinding(BiConsumer<? super String, ? super C> biConsumer);

	void forEachBindingOnCopy(BiConsumer<? super String, ? super C> biConsumer);

}