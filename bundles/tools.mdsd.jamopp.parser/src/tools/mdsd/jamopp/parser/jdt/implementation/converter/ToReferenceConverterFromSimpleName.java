package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToReferenceConverterFromSimpleName implements Converter<SimpleName, IdentifierReference> {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToReferenceConverterFromSimpleName(final ReferencesFactory referencesFactory,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public IdentifierReference convert(final SimpleName name) {
		final IdentifierReference result = referencesFactory.createIdentifierReference();
		final IBinding binding = name.resolveBinding();
		tools.mdsd.jamopp.model.java.references.ReferenceableElement target;
		if (binding instanceof ITypeBinding) {
			target = jdtResolverUtility.getClassifier((ITypeBinding) binding);
		} else if (binding instanceof IVariableBinding) {
			target = jdtResolverUtility.getReferencableElement((IVariableBinding) binding);
		} else if (binding instanceof IMethodBinding) {
			target = jdtResolverUtility.getMethod((IMethodBinding) binding);
		} else if (binding instanceof IPackageBinding) {
			target = jdtResolverUtility.getPackage((IPackageBinding) binding);
		} else {
			target = jdtResolverUtility.getReferenceableElementByNameMatching(name.getIdentifier());
		}

		target.setName(name.getIdentifier());
		result.setTarget(target);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, name);
		return result;
	}

}
