package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ClassResolver extends ResolverAbstract<tools.mdsd.jamopp.model.java.classifiers.Class, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final HashSet<ITypeBinding> typeBindings;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public ClassResolver(HashMap<IBinding, String> nameCache, HashMap<String, Class> bindings,
			ClassifiersFactory classifiersFactory, UtilJdtResolverImpl utilJdtResolverImpl,
			HashSet<ITypeBinding> typeBindings) {
		super(nameCache, bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public Class getByBinding(ITypeBinding binding) {
		typeBindings.add(binding);
		return getByName(utilJdtResolverImpl.convertToTypeName(binding));
	}

	@Override
	public Class getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.classifiers.Class result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(name);
		if (potClass instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Class) potClass;
		} else {
			result = classifiersFactory.createClass();
		}
		getBindings().put(name, result);
		return result;
	}

}
