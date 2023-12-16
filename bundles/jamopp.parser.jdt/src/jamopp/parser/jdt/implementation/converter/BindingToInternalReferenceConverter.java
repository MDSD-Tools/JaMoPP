package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;

public class BindingToInternalReferenceConverter implements Converter<ITypeBinding, Reference> {

	private final ReferencesFactory referencesFactory;
	private final IUtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToInternalReferenceConverter(ReferencesFactory referencesFactory, IUtilJdtResolver jdtTResolverUtility) {
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
