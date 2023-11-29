package jamopp.parser.jdt;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

class InNamespaceClassifierReferenceWrapper {

	NamespaceClassifierReference wrapInNamespaceClassifierReference(TypeReference ref) {
		if (ref instanceof NamespaceClassifierReference) {
			return (NamespaceClassifierReference) ref;
		}
		if (ref instanceof ClassifierReference) {
			NamespaceClassifierReference result = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) ref);
			return result;
		}
		return null;
	}
	
}
