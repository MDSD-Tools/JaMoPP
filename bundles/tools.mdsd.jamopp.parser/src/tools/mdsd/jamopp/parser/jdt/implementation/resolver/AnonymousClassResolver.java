package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnonymousClassResolver extends ResolverAbstract<AnonymousClass, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public AnonymousClassResolver(HashMap<String, AnonymousClass> bindings, ClassifiersFactory classifiersFactory,
			ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public AnonymousClass getByBinding(ITypeBinding binding) {
		String typeName = toTypeNameConverter.convertToTypeName(binding);
		return getByName(typeName);
	}

	@Override
	public AnonymousClass getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		AnonymousClass result = classifiersFactory.createAnonymousClass();
		getBindings().put(name, result);
		return result;
	}

}
