package tools.mdsd.jamopp.parser.interfaces.resolver;

import org.eclipse.jdt.core.dom.IBinding;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public interface ResolverWithName<C, B extends IBinding> extends Resolver<C, B> {

	C getByName(String name);

}