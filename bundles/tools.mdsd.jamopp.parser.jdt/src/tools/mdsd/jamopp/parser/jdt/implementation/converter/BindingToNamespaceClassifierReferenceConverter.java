package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Collections;

import org.eclipse.jdt.core.dom.ITypeBinding;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class BindingToNamespaceClassifierReferenceConverter
		implements Converter<ITypeBinding, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;
	private final UtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToNamespaceClassifierReferenceConverter(TypesFactory typesFactory, UtilJdtResolver jdtTResolverUtility) {
		this.typesFactory = typesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	@Override
	public NamespaceClassifierReference convert(ITypeBinding binding) {
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
