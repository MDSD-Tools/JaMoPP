package tools.mdsd.jamopp.parser.interfaces.resolver;

import org.eclipse.jdt.core.dom.IBinding;

public interface Converter<B extends IBinding> {

	String convert(B binding);

}