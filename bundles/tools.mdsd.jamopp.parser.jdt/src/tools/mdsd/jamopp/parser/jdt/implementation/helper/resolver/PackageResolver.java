package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.IPackageBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Package;

public class PackageResolver extends Resolver<tools.mdsd.jamopp.model.java.containers.Package, IPackageBinding> {

	private final HashSet<IPackageBinding> packageBindings;
	private final ContainersFactory containersFactory;

	public PackageResolver(HashSet<IPackageBinding> packageBindings, ContainersFactory containersFactory) {
		this.packageBindings = packageBindings;
		this.containersFactory = containersFactory;
	}

	@Override
	public Package getByBinding(IPackageBinding binding) {
		this.packageBindings.add(binding);
		return getByName(binding.getName());
	}

	@Override
	public Package getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.containers.Package result = JavaClasspath.get().getPackage(name);
		if (result == null) {
			result = this.containersFactory.createPackage();
		}
		getBindings().put(name, result);
		return result;
	}

}
