package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import com.google.inject.Inject;

class BindingToInternalReferenceConverter {

	private final ReferencesFactory referencesFactory;
	private final UtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToInternalReferenceConverter(ReferencesFactory referencesFactory, UtilJdtResolver jdtTResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	Reference internalConvertToReference(ITypeBinding binding) {
		IdentifierReference idRef = referencesFactory.createIdentifierReference();
		idRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		if (binding.isNested()) {
			Reference parentRef = internalConvertToReference(binding.getDeclaringClass());
			parentRef.setNext(idRef);
		}
		return idRef;
	}

}
