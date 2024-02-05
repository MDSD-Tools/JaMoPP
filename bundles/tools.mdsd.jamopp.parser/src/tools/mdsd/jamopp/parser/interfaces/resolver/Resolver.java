package tools.mdsd.jamopp.parser.interfaces.resolver;

/**
 * @param <C> Class
 * @param <B> BindingType
 */
public interface Resolver<C, B> {

	C getByBinding(B binding);

}
