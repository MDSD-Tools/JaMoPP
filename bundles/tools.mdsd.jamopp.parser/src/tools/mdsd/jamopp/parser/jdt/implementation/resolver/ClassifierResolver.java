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
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass != null) {
			return potClass;
		}
		if (binding.isAnonymous() || binding.isLocal() && binding.getDeclaringMember() == null
				|| anonymousClassResolver.getBindings().containsKey(toTypeNameConverter.convertToTypeName(binding))) {
			return null;
		}
		if (binding.isAnnotation()) {
			return annotationResolver.getByBinding(binding);
		}
		if (binding.isInterface()) {
			return interfaceResolver.getByBinding(binding);
		}
		if (binding.isEnum()) {
			return enumerationResolver.getByBinding(binding);
		}
		if (binding.isClass()) {
			return classResolver.getByBinding(binding);
		}
		if (binding.isTypeVariable()) {
			return typeParameterResolver.getByBinding(binding);
		}
		if (binding.isArray()) {
			return getClassifier(binding.getElementType());
		}
		return null;
	}

}
