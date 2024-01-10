package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;

public class EnumerationResolver extends ResolverAbstract<Enumeration, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final HashSet<ITypeBinding> typeBindings;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public EnumerationResolver(HashMap<String, Enumeration> bindings, HashSet<ITypeBinding> typeBindings,
			ClassifiersFactory classifiersFactory, ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Enumeration getByBinding(ITypeBinding binding) {
		String enumName = toTypeNameConverter.convertToTypeName(binding);
		if (getBindings().containsKey(enumName)) {
			return getBindings().get(enumName);
		}
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
		return result;
	}

	@Override
	public Enumeration getByName(String name) {
		throw new RuntimeException("Not implemented");
	}

}
