package tools.mdsd.jamopp.parser.jdt.interfaces.resolver;

import java.util.Map;

import org.eclipse.jdt.core.dom.IBinding;

public interface Resolver<Clazz, BindingType extends IBinding> {

	Map<String, Clazz> getBindings();

	Clazz getByBinding(BindingType binding);

	Clazz getByName(String name);

}