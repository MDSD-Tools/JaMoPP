package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;

public abstract class Resolver<Clazz, BindingType extends IBinding> {

	private final HashMap<String, Clazz> bindings = new HashMap<>();

	public abstract Clazz getByBinding(BindingType binding);

	public abstract Clazz getByName(String annotName);

	public final HashMap<String, Clazz> getBindings() {
		return this.bindings;
	}

	public final void clearBindings() {
		this.bindings.clear();
	}

}
