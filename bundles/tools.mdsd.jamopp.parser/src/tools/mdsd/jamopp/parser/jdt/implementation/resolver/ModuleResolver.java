package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IModuleBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Module;

public class ModuleResolver extends ResolverAbstract<Module, IModuleBinding> {

	private final Set<IModuleBinding> moduleBindings;
	private final ContainersFactory containersFactory;

	@Inject
	public ModuleResolver(Map<String, Module> bindings, Set<IModuleBinding> moduleBindings,
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
		Module module;
		if (getBindings().containsKey(name)) {
			module = getBindings().get(name);
		} else {
			Module result = JavaClasspath.get().getModule(name);
			if (result == null) {
				result = containersFactory.createModule();
			}
			getBindings().put(name, result);
			module = result;
		}
		return module;
	}

}
