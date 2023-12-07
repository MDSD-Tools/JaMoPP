package jamopp.parser.jdt.converter.implementation.converter;

import java.util.Collections;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;

public class BindingToNamespaceClassifierReferenceConverter
		implements Converter<ITypeBinding, NamespaceClassifierReference> {

	private final TypesFactory typesFactory;
	private final IUtilJdtResolver jdtTResolverUtility;

	@Inject
	BindingToNamespaceClassifierReferenceConverter(TypesFactory typesFactory, IUtilJdtResolver jdtTResolverUtility) {
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
