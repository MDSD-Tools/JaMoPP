package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class BindingToNamespaceClassifierReferenceConverter
		implements Converter<ITypeBinding, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;
	private final JdtResolver jdtTResolverUtility;

	@Inject
	public BindingToNamespaceClassifierReferenceConverter(TypesFactory typesFactory, JdtResolver jdtTResolverUtility) {
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
