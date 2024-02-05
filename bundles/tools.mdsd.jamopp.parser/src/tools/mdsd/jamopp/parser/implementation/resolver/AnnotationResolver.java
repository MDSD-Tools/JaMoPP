package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnnotationResolver extends AbstractResolverWithCache<Annotation, ITypeBinding> {

	private final Set<ITypeBinding> typeBindings;
	private final ClassifiersFactory classifiersFactory;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public AnnotationResolver(final Map<String, Annotation> bindings, final Set<ITypeBinding> typeBindings,
			final ClassifiersFactory classifiersFactory, final ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.typeBindings = typeBindings;
		this.classifiersFactory = classifiersFactory;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Annotation getByBinding(final ITypeBinding binding) {
		typeBindings.add(binding);
		return getByName(toTypeNameConverter.convert(binding));
	}

	@Override
	public Annotation getByName(final String name) {
		Annotation annotation;
		if (containsKey(name)) {
			annotation = get(name);
		} else {
			Annotation result;
			final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
					.getConcreteClassifier(name);
			if (potClass instanceof Annotation) {
				result = (Annotation) potClass;
			} else {
				result = classifiersFactory.createAnnotation();
			}
			putBinding(name, result);
			annotation = result;
		}
		return annotation;
	}

}
