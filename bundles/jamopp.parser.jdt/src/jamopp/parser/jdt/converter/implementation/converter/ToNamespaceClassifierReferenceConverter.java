package jamopp.parser.jdt.converter.implementation.converter;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;

public class ToNamespaceClassifierReferenceConverter
		implements Converter<TypeReference, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;

	@Inject
	ToNamespaceClassifierReferenceConverter(TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
	}

	@Override
	public NamespaceClassifierReference convert(TypeReference ref) {
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
