package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.Collections;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class BindingToNamespaceClassifierReferenceConverter
		implements Converter<ITypeBinding, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;
	private final JdtResolver jdtTResolverUtility;

	@Inject
	public BindingToNamespaceClassifierReferenceConverter(final TypesFactory typesFactory,
			final JdtResolver jdtTResolverUtility) {
		this.typesFactory = typesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	@Override
	public NamespaceClassifierReference convert(final ITypeBinding binding) {
		final NamespaceClassifierReference ref = typesFactory.createNamespaceClassifierReference();
		if (binding.getPackage() != null) {
			Collections.addAll(ref.getNamespaces(), binding.getPackage().getNameComponents());
		}
		final ClassifierReference classRef = typesFactory.createClassifierReference();
		classRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		ref.getClassifierReferences().add(classRef);
		return ref;
	}

}
