package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class BindingToInternalReferenceConverter implements Converter<ITypeBinding, Reference> {

	private final ReferencesFactory referencesFactory;
	private final JdtResolver jdtTResolverUtility;

	@Inject
	public BindingToInternalReferenceConverter(final ReferencesFactory referencesFactory,
			final JdtResolver jdtTResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	@Override
	public Reference convert(final ITypeBinding binding) {
		final IdentifierReference idRef = referencesFactory.createIdentifierReference();
		idRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		if (binding.isNested()) {
			final Reference parentRef = convert(binding.getDeclaringClass());
			parentRef.setNext(idRef);
		}
		return idRef;
	}

}
