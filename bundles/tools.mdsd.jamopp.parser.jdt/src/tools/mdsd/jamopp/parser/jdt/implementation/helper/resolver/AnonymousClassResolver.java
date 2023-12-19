package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnonymousClassResolver extends ResolverAbstract<AnonymousClass, IBinding> {

	private final ClassifiersFactory classifiersFactory;

	public AnonymousClassResolver(HashMap<IBinding, String> nameCache, HashMap<String, AnonymousClass> bindings,
			ClassifiersFactory classifiersFactory) {
		super(nameCache, bindings);
		this.classifiersFactory = classifiersFactory;
	}

	@Override
	public AnonymousClass getByBinding(IBinding binding) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public AnonymousClass getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.classifiers.AnonymousClass result = classifiersFactory.createAnonymousClass();
		getBindings().put(name, result);
		return result;
	}

}
