package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IPackageBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.Package;

public class PackageResolver extends ResolverAbstract<Package, IPackageBinding> {

	private final Set<IPackageBinding> packageBindings;
	private final ContainersFactory containersFactory;

	@Inject
	public PackageResolver(final Map<String, Package> bindings, final Set<IPackageBinding> packageBindings,
			final ContainersFactory containersFactory) {
		super(bindings);
		this.packageBindings = packageBindings;
		this.containersFactory = containersFactory;
	}

	@Override
	public Package getByBinding(final IPackageBinding binding) {
		packageBindings.add(binding);
		return getByName(binding.getName());
	}

	@Override
	public Package getByName(final String name) {
		Package resultPackage;
		if (getBindings().containsKey(name)) {
			resultPackage = getBindings().get(name);
		} else {
			Package result = JavaClasspath.get().getPackage(name);
			if (result == null) {
				result = containersFactory.createPackage();
			}
			getBindings().put(name, result);
			resultPackage = result;
		}
		return resultPackage;
	}

}
