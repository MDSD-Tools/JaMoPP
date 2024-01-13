package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;

public class EnumerationResolver extends ResolverAbstract<Enumeration, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final Set<ITypeBinding> typeBindings;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public EnumerationResolver(Map<String, Enumeration> bindings, Set<ITypeBinding> typeBindings,
			ClassifiersFactory classifiersFactory, ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Enumeration getByBinding(ITypeBinding binding) {
		String enumName = toTypeNameConverter.convertToTypeName(binding);
		Enumeration enumeration;
		if (getBindings().containsKey(enumName)) {
			enumeration = getBindings().get(enumName);
		} else {
			typeBindings.add(binding);
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
					.getConcreteClassifier(enumName);
			Enumeration result;
			if (classifier instanceof Enumeration) {
				result = (Enumeration) classifier;
			} else {
				result = classifiersFactory.createEnumeration();
			}
			getBindings().put(enumName, result);
			enumeration = result;
		}
		return enumeration;
	}

	@Override
	public Enumeration getByName(String name) {
		throw new UnsupportedOperationException("Not implemented");
	}

}
