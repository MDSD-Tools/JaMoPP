package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IPackageBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Package;

public class PackageResolver
		extends ResolverAbstract<tools.mdsd.jamopp.model.java.containers.Package, IPackageBinding> {

	private final HashSet<IPackageBinding> packageBindings;
	private final ContainersFactory containersFactory;

	@Inject
	public PackageResolver(HashMap<String, Package> bindings, HashSet<IPackageBinding> packageBindings,
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
		tools.mdsd.jamopp.model.java.containers.Package result = JavaClasspath.get().getPackage(name);
		if (result == null) {
			result = containersFactory.createPackage();
		}
		getBindings().put(name, result);
		return result;
	}

}
