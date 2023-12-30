package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;

public interface Resolver<Clazz, BindingType extends IBinding> {

	HashMap<String, Clazz> getBindings();

	Clazz getByBinding(BindingType binding);

	Clazz getByName(String name);

}