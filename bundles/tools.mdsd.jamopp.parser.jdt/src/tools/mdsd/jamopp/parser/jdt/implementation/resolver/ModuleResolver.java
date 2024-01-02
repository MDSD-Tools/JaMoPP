package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IModuleBinding;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Module;

public class ModuleResolver extends ResolverAbstract<tools.mdsd.jamopp.model.java.containers.Module, IModuleBinding> {

	private final HashSet<IModuleBinding> moduleBindings;
	private final ContainersFactory containersFactory;

	@Inject
	public ModuleResolver(HashMap<String, Module> bindings, HashSet<IModuleBinding> moduleBindings,
			ContainersFactory containersFactory) {
		super(bindings);
		this.moduleBindings = moduleBindings;
		this.containersFactory = containersFactory;
	}

	@Override
	public Module getByBinding(IModuleBinding binding) {
		moduleBindings.add(binding);
		return getByName(binding.getName());
	}

	@Override
	public Module getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.containers.Module result = JavaClasspath.get().getModule(name);
		if (result == null) {
			result = containersFactory.createModule();
		}
		getBindings().put(name, result);
		return result;
	}

}
