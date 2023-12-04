package jamopp.parser.jdt.converter.implementation;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

public class ToNamespaceClassifierReferenceConverter {

	private final TypesFactory typesFactory;

	@Inject
	ToNamespaceClassifierReferenceConverter(TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
	}

	public NamespaceClassifierReference convertToNamespaceClassifierReference(TypeReference ref) {
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
