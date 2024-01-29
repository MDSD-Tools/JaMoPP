package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IModuleBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Module;

public class ModuleResolver extends ResolverWithCache<Module, IModuleBinding> {

	private final Set<IModuleBinding> moduleBindings;
	private final ContainersFactory containersFactory;

	@Inject
	public ModuleResolver(final Map<String, Module> bindings, final Set<IModuleBinding> moduleBindings,
			final ContainersFactory containersFactory) {
		super(bindings);
		this.moduleBindings = moduleBindings;
		this.containersFactory = containersFactory;
	}

	@Override
	public Module getByBinding(final IModuleBinding binding) {
		moduleBindings.add(binding);
		return getByName(binding.getName());
	}

	@Override
	public Module getByName(final String name) {
		Module module;
		if (containsKey(name)) {
			module = get(name);
		} else {
			Module result = JavaClasspath.get().getModule(name);
			if (result == null) {
				result = containersFactory.createModule();
			}
			putBinding(name, result);
			module = result;
		}
		return module;
	}

}
