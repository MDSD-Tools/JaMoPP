package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

class ToClassifierReferenceConverter {

	private final UtilJdtResolver jdtResolverUtility;

	@Inject
	ToClassifierReferenceConverter(UtilJdtResolver jdtResolverUtility) {
		this.jdtResolverUtility = jdtResolverUtility;
	}

	ClassifierReference convertToClassifierReference(SimpleName simpleName) {
		ClassifierReference ref = TypesFactory.eINSTANCE.createClassifierReference();
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
