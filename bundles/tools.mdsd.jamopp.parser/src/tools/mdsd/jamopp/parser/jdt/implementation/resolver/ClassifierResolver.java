package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;

public class ClassifierResolver {

	private final AnonymousClassResolver anonymousClassResolver;
	private final ToTypeNameConverter toTypeNameConverter;
	private final AnnotationResolver annotationResolver;
	private final InterfaceResolver interfaceResolver;
	private final EnumerationResolver enumerationResolver;
	private final ClassResolver classResolver;
	private final TypeParameterResolver typeParameterResolver;

	@Inject
	public ClassifierResolver(AnonymousClassResolver anonymousClassResolver,
			TypeParameterResolver typeParameterResolver, ToTypeNameConverter toTypeNameConverter,
			InterfaceResolver interfaceResolver, EnumerationResolver enumerationResolver, ClassResolver classResolver,
			AnnotationResolver annotationResolver) {
		this.anonymousClassResolver = anonymousClassResolver;
		this.toTypeNameConverter = toTypeNameConverter;
		this.annotationResolver = annotationResolver;
		this.interfaceResolver = interfaceResolver;
		this.enumerationResolver = enumerationResolver;
		this.classResolver = classResolver;
		this.typeParameterResolver = typeParameterResolver;
	}

	public tools.mdsd.jamopp.model.java.classifiers.Classifier getClassifier(ITypeBinding binding) {
		String typeName = toTypeNameConverter.convertToTypeName(binding);
		tools.mdsd.jamopp.model.java.classifiers.Classifier classifier = null;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass != null) {
			classifier = potClass;
		} else if (binding.isAnonymous() || binding.isLocal() && binding.getDeclaringMember() == null
				|| anonymousClassResolver.getBindings().containsKey(toTypeNameConverter.convertToTypeName(binding))) {
			classifier = null;
		} else if (binding.isAnnotation()) {
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
			classifier = getClassifier(binding.getElementType());
		}
		return classifier;
	}

}
