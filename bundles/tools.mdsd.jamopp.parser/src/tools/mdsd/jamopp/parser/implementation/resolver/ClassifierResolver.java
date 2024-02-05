package tools.mdsd.jamopp.parser.implementation.resolver;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;

public class ClassifierResolver implements Resolver<Classifier, ITypeBinding> {

	private final AnonymousClassResolver anonymousClassResolver;
	private final ToTypeNameConverter toTypeNameConverter;
	private final AnnotationResolver annotationResolver;
	private final InterfaceResolver interfaceResolver;
	private final EnumerationResolver enumerationResolver;
	private final ClassResolver classResolver;
	private final TypeParameterResolver typeParameterResolver;

	@Inject
	public ClassifierResolver(final AnonymousClassResolver anonymousClassResolver,
			final TypeParameterResolver typeParameterResolver, final ToTypeNameConverter toTypeNameConverter,
			final InterfaceResolver interfaceResolver, final EnumerationResolver enumerationResolver,
			final ClassResolver classResolver, final AnnotationResolver annotationResolver) {
		this.anonymousClassResolver = anonymousClassResolver;
		this.toTypeNameConverter = toTypeNameConverter;
		this.annotationResolver = annotationResolver;
		this.interfaceResolver = interfaceResolver;
		this.enumerationResolver = enumerationResolver;
		this.classResolver = classResolver;
		this.typeParameterResolver = typeParameterResolver;
	}

	@Override
	public Classifier getByBinding(final ITypeBinding binding) {
		final String typeName = toTypeNameConverter.convert(binding);
		Classifier classifier = null;
		final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass != null) {
			classifier = potClass;
		} else if (!binding.isAnonymous() && (!binding.isLocal() || binding.getDeclaringMember() != null)
				&& !anonymousClassResolver.containsKey(toTypeNameConverter.convert(binding))) {
			classifier = switchOverBinding(binding);
		}
		return classifier;
	}

	private Classifier switchOverBinding(final ITypeBinding binding) {
		Classifier classifier = null;
		if (binding.isAnnotation()) {
			classifier = annotationResolver.getByBinding(binding);
		} else if (binding.isInterface()) {
			classifier = interfaceResolver.getByBinding(binding);
		} else if (binding.isEnum()) {
			classifier = enumerationResolver.getByBinding(binding);
		} else if (binding.isClass()) {
			classifier = classResolver.getByBinding(binding);
		} else if (binding.isTypeVariable()) {
			classifier = typeParameterResolver.getByBinding(binding);
		} else if (binding.isArray()) {
			classifier = getByBinding(binding.getElementType());
		}
		return classifier;
	}

}
