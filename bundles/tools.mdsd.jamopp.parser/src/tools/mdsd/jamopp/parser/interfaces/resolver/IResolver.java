package tools.mdsd.jamopp.parser.interfaces.resolver;

import org.eclipse.jdt.core.dom.IBinding;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public interface IResolver<C, B extends IBinding> {

	C getByBinding(B binding);

	C getByName(String name);

}