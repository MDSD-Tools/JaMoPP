package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ClassifierResolver {

	private final UtilJdtResolverImpl utilJdtResolverImpl;
	private final AnonymousClassResolver anonymousClassResolver;

	@Inject
	public ClassifierResolver(UtilJdtResolverImpl utilJdtResolverImpl, AnonymousClassResolver anonymousClassResolver) {
		this.utilJdtResolverImpl = utilJdtResolverImpl;
		this.anonymousClassResolver = anonymousClassResolver;
	}

	public tools.mdsd.jamopp.model.java.classifiers.Classifier getClassifier(ITypeBinding binding) {
		String typeName = utilJdtResolverImpl.convertToTypeName(binding);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass != null) {
			return potClass;
		}
		if (binding.isAnonymous() || binding.isLocal() && binding.getDeclaringMember() == null
				|| anonymousClassResolver.getBindings().containsKey(utilJdtResolverImpl.convertToTypeName(binding))) {
			return null;
		}
		if (binding.isAnnotation()) {
			return utilJdtResolverImpl.getAnnotation(binding);
		}
		if (binding.isInterface()) {
			return utilJdtResolverImpl.getInterface(binding);
		}
		if (binding.isEnum()) {
			return utilJdtResolverImpl.getEnumeration(binding);
		}
		if (binding.isClass()) {
			return utilJdtResolverImpl.getClass(binding);
		}
		if (binding.isTypeVariable()) {
			return utilJdtResolverImpl.getTypeParameter(binding);
		}
		if (binding.isArray()) {
			return getClassifier(binding.getElementType());
		}
		return null;
	}

}
