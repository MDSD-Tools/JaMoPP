package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnnotationResolver extends Resolver<Annotation, ITypeBinding> {

	public AnnotationResolver(HashMap<IBinding, String> nameCache, HashMap<String, Annotation> bindings,
			HashSet<ITypeBinding> typeBindings, ClassifiersFactory classifiersFactory) {
		super(nameCache, bindings);
		this.typeBindings = typeBindings;
		this.classifiersFactory = classifiersFactory;
	}

	private final HashSet<ITypeBinding> typeBindings;

	private final ClassifiersFactory classifiersFactory;

	@Override
	public Annotation getByBinding(ITypeBinding binding) {
		this.typeBindings.add(binding);
		return getByName(convertToTypeName(binding));
	}

	@Override
	public Annotation getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.classifiers.Annotation result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(name);
		if (potClass instanceof tools.mdsd.jamopp.model.java.classifiers.Annotation) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Annotation) potClass;
		} else {
			result = this.classifiersFactory.createAnnotation();
		}
		getBindings().put(name, result);
		return result;
	}

}