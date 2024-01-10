package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnnotationResolver extends ResolverAbstract<Annotation, ITypeBinding> {

	private final HashSet<ITypeBinding> typeBindings;
	private final ClassifiersFactory classifiersFactory;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public AnnotationResolver(HashMap<String, Annotation> bindings, HashSet<ITypeBinding> typeBindings,
			ClassifiersFactory classifiersFactory, ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.typeBindings = typeBindings;
		this.classifiersFactory = classifiersFactory;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Annotation getByBinding(ITypeBinding binding) {
		typeBindings.add(binding);
		return getByName(toTypeNameConverter.convertToTypeName(binding));
	}

	@Override
	public Annotation getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		Annotation result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(name);
		if (potClass instanceof Annotation) {
			result = (Annotation) potClass;
		} else {
			result = classifiersFactory.createAnnotation();
		}
		getBindings().put(name, result);
		return result;
	}

}
