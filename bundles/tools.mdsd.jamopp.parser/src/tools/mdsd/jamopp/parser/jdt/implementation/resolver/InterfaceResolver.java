package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.classifiers.Interface;

public class InterfaceResolver extends ResolverAbstract<Interface, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final Set<ITypeBinding> typeBindings;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public InterfaceResolver(Map<String, Interface> bindings, Set<ITypeBinding> typeBindings,
			ClassifiersFactory classifiersFactory, ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Interface getByBinding(ITypeBinding binding) {
		String interName = toTypeNameConverter.convertToTypeName(binding);
		if (getBindings().containsKey(interName)) {
			return getBindings().get(interName);
		}
		typeBindings.add(binding);
		Interface result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
				.getConcreteClassifier(interName);
		if (classifier instanceof Interface) {
			result = (Interface) classifier;
		} else {
			result = classifiersFactory.createInterface();
		}
		getBindings().put(interName, result);
		return result;
	}

	@Override
	public Interface getByName(String name) {
		throw new RuntimeException("Not implemented");
	}

}
