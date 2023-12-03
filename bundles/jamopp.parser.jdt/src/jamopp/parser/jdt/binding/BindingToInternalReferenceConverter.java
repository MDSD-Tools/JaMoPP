package jamopp.parser.jdt.binding;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.other.UtilJdtResolver;

public class BindingToInternalReferenceConverter {

	private final ReferencesFactory referencesFactory;
	private final UtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToInternalReferenceConverter(ReferencesFactory referencesFactory, UtilJdtResolver jdtTResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	public Reference internalConvertToReference(ITypeBinding binding) {
		IdentifierReference idRef = referencesFactory.createIdentifierReference();
		idRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		if (binding.isNested()) {
			Reference parentRef = internalConvertToReference(binding.getDeclaringClass());
			parentRef.setNext(idRef);
		}
		return idRef;
	}

}