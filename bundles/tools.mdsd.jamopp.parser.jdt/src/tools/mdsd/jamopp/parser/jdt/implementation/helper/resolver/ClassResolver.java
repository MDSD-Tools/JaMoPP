package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class ClassResolver extends ResolverAbstract<tools.mdsd.jamopp.model.java.classifiers.Class, IBinding> {

	private final ClassifiersFactory classifiersFactory;

	public ClassResolver(HashMap<IBinding, String> nameCache, HashMap<String, Class> bindings,
			ClassifiersFactory classifiersFactory) {
		super(nameCache, bindings);
		this.classifiersFactory = classifiersFactory;
	}

	@Override
	public Class getByBinding(IBinding binding) {
		throw new RuntimeException("Not implemented");
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
