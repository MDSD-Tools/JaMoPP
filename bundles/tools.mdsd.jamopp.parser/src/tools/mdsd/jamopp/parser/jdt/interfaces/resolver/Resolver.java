package tools.mdsd.jamopp.parser.jdt.interfaces.resolver;

import java.util.Map;

import org.eclipse.jdt.core.dom.IBinding;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public interface Resolver<C, B extends IBinding> {

	Map<String, C> getBindings();

	C getByBinding(B binding);

	C getByName(String name);

}