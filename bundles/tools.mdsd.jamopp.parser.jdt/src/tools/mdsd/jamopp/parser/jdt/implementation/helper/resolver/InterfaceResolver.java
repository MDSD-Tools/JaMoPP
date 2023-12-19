package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.classifiers.Interface;

public class InterfaceResolver extends ResolverAbstract<tools.mdsd.jamopp.model.java.classifiers.Interface, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final HashSet<ITypeBinding> typeBindings;

	public InterfaceResolver(HashMap<IBinding, String> nameCache, HashMap<String, Interface> bindings,
			HashSet<ITypeBinding> typeBindings, ClassifiersFactory classifiersFactory) {
		super(nameCache, bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
	}

	@Override
	public Interface getByBinding(ITypeBinding binding) {
		String interName = convertToTypeName(binding);
		if (getBindings().containsKey(interName)) {
			return getBindings().get(interName);
		}
		typeBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.Interface result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
				.getConcreteClassifier(interName);
		if (classifier instanceof tools.mdsd.jamopp.model.java.classifiers.Interface) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Interface) classifier;
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
