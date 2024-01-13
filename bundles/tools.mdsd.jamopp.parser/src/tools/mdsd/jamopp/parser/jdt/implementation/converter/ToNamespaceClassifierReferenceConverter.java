package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToNamespaceClassifierReferenceConverter implements Converter<TypeReference, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;

	@Inject
	public ToNamespaceClassifierReferenceConverter(TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
	}

	@Override
	public NamespaceClassifierReference convert(TypeReference ref) {
		NamespaceClassifierReference reference = null;
		if (ref instanceof NamespaceClassifierReference) {
			reference = (NamespaceClassifierReference) ref;
		} else if (ref instanceof ClassifierReference) {
			NamespaceClassifierReference result = typesFactory.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) ref);
			reference = result;
		}
		return reference;
	}

}
