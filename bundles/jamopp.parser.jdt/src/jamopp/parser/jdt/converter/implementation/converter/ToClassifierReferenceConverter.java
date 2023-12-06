package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;

public class ToClassifierReferenceConverter implements ToConverter<SimpleName, ClassifierReference> {

	private final TypesFactory typesFactory;
	private final IUtilJdtResolver jdtResolverUtility;

	@Inject
	ToClassifierReferenceConverter(IUtilJdtResolver jdtResolverUtility, TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public ClassifierReference convert(SimpleName simpleName) {
		ClassifierReference ref = typesFactory.createClassifierReference();
		ITypeBinding binding = (ITypeBinding) simpleName.resolveBinding();
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
