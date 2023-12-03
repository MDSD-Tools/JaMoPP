package jamopp.parser.jdt.binding;

import java.util.Collections;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.other.UtilJdtResolver;

public class BindingToNamespaceClassifierReferenceConverter {

	private final TypesFactory typesFactory;
	private final UtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToNamespaceClassifierReferenceConverter(TypesFactory typesFactory, UtilJdtResolver jdtTResolverUtility) {
		this.typesFactory = typesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	NamespaceClassifierReference convertToNamespaceClassifierReference(ITypeBinding binding) {
		NamespaceClassifierReference ref = typesFactory.createNamespaceClassifierReference();
		if (binding.getPackage() != null) {
			Collections.addAll(ref.getNamespaces(), binding.getPackage().getNameComponents());
		}
		ClassifierReference classRef = typesFactory.createClassifierReference();
		classRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		ref.getClassifierReferences().add(classRef);
		return ref;
	}

}