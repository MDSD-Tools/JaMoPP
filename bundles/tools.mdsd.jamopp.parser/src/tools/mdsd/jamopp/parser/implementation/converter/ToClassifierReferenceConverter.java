package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToClassifierReferenceConverter implements Converter<SimpleName, ClassifierReference> {

	private final TypesFactory typesFactory;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToClassifierReferenceConverter(final JdtResolver jdtResolverUtility, final TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public ClassifierReference convert(final SimpleName simpleName) {
		final ClassifierReference ref = typesFactory.createClassifierReference();
		final ITypeBinding binding = (ITypeBinding) simpleName.resolveBinding();
		Classifier proxy;
		if (binding == null || binding.isRecovered()) {
			proxy = jdtResolverUtility.getClass(simpleName.getIdentifier());
		} else {
			proxy = jdtResolverUtility.getClassifier(binding);
		}
		proxy.setName(simpleName.getIdentifier());
		ref.setTarget(proxy);
		return ref;
	}

}
