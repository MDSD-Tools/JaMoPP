package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.classifiers.Interface;

public class InterfaceResolver extends ResolverWithCache<Interface, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final Set<ITypeBinding> typeBindings;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public InterfaceResolver(final Map<String, Interface> bindings, final Set<ITypeBinding> typeBindings,
			final ClassifiersFactory classifiersFactory, final ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Interface getByBinding(final ITypeBinding binding) {
		final String interName = toTypeNameConverter.convertToTypeName(binding);
		Interface interfaceResult;
		if (containsKey(interName)) {
			interfaceResult = get(interName);
		} else {
			typeBindings.add(binding);
			Interface result;
			final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
					.getConcreteClassifier(interName);
			if (classifier instanceof Interface) {
				result = (Interface) classifier;
			} else {
				result = classifiersFactory.createInterface();
			}
			putBinding(interName, result);
			interfaceResult = result;
		}
		return interfaceResult;
	}

	@Override
	public Interface getByName(final String name) {
		throw new UnsupportedOperationException("Not implemented");
	}

}
