package tools.mdsd.jamopp.parser.implementation.resolver;

import  com.google.inject.name.Named;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;

public class ClassifierResolver implements Resolver<Classifier, ITypeBinding> {

	private final ToStringConverter<ITypeBinding> toTypeNameConverter;
	private final ResolverWithCache<AnonymousClass, ITypeBinding> anonymousClassResolver;
	private final ResolverWithCache<Annotation, ITypeBinding> annotationResolver;
	private final ResolverWithCache<Interface, ITypeBinding> interfaceResolver;
	private final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver;
	private final ResolverWithCache<Class, ITypeBinding> classResolver;
	private final ResolverWithCache<TypeParameter, ITypeBinding> typeParameterResolver;

	@Inject
	public ClassifierResolver(final ResolverWithCache<AnonymousClass, ITypeBinding> anonymousClassResolver,
			final ResolverWithCache<TypeParameter, ITypeBinding> typeParameterResolver,
			@Named("ToTypeNameConverterFromBinding") final ToStringConverter<ITypeBinding> toTypeNameConverter,
			final ResolverWithCache<Interface, ITypeBinding> interfaceResolver,
			final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver,
			final ResolverWithCache<Class, ITypeBinding> classResolver,
			final ResolverWithCache<Annotation, ITypeBinding> annotationResolver) {
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
