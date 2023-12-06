package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class BindingToInternalReferenceConverter implements ToConverter<ITypeBinding, Reference> {

	private final ReferencesFactory referencesFactory;
	private final UtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToInternalReferenceConverter(ReferencesFactory referencesFactory, UtilJdtResolver jdtTResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	public Reference convert(ITypeBinding binding) {
		IdentifierReference idRef = referencesFactory.createIdentifierReference();
		idRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		if (binding.isNested()) {
			Reference parentRef = convert(binding.getDeclaringClass());
			parentRef.setNext(idRef);
		}
		return idRef;
	}

}
