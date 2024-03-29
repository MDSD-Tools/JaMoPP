package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToNamespaceClassifierReferenceConverter implements Converter<TypeReference, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;

	@Inject
	public ToNamespaceClassifierReferenceConverter(final TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
	}

	@Override
	public NamespaceClassifierReference convert(final TypeReference ref) {
		NamespaceClassifierReference reference = null;
		if (ref instanceof NamespaceClassifierReference) {
			reference = (NamespaceClassifierReference) ref;
		} else if (ref instanceof ClassifierReference) {
			final NamespaceClassifierReference result = typesFactory.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) ref);
			reference = result;
		}
		return reference;
	}

}
