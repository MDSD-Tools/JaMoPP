package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IPackageBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Package;

public class PackageResolver extends ResolverAbstract<Package, IPackageBinding> {

	private final Set<IPackageBinding> packageBindings;
	private final ContainersFactory containersFactory;

	@Inject
	public PackageResolver(Map<String, Package> bindings, Set<IPackageBinding> packageBindings,
			ContainersFactory containersFactory) {
		super(bindings);
		this.packageBindings = packageBindings;
		this.containersFactory = containersFactory;
	}

	@Override
	public Package getByBinding(IPackageBinding binding) {
		packageBindings.add(binding);
		return getByName(binding.getName());
	}

	@Override
	public Package getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		Package result = JavaClasspath.get().getPackage(name);
		if (result == null) {
			result = containersFactory.createPackage();
		}
		getBindings().put(name, result);
		return result;
	}

}
