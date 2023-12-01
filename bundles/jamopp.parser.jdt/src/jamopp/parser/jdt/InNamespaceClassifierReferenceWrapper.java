package jamopp.parser.jdt;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

class InNamespaceClassifierReferenceWrapper {

	private final TypesFactory typesFactory;

	@Inject
	InNamespaceClassifierReferenceWrapper(TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
	}

	NamespaceClassifierReference wrapInNamespaceClassifierReference(TypeReference ref) {
		if (ref instanceof NamespaceClassifierReference) {
			return (NamespaceClassifierReference) ref;
		}
		if (ref instanceof ClassifierReference) {
			NamespaceClassifierReference result = typesFactory.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) ref);
			return result;
		}
		return null;
	}

}
