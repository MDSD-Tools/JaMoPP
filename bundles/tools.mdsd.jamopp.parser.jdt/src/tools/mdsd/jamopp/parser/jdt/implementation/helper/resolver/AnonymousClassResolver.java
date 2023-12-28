package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class AnonymousClassResolver extends ResolverAbstract<AnonymousClass, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public AnonymousClassResolver(HashMap<IBinding, String> nameCache, HashMap<String, AnonymousClass> bindings,
			ClassifiersFactory classifiersFactory, UtilJdtResolverImpl utilJdtResolverImpl) {
		super(nameCache, bindings);
		this.classifiersFactory = classifiersFactory;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public AnonymousClass getByBinding(ITypeBinding binding) {
		String typeName = utilJdtResolverImpl.convertToTypeName(binding);
		return getByName(typeName);
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
