package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToReferenceConverterFromSimpleName implements Converter<SimpleName, IdentifierReference>{

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	
	@Inject
	public ToReferenceConverterFromSimpleName(ReferencesFactory referencesFactory, UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
	}
	
	@Override
	public IdentifierReference convert(SimpleName name) {
		org.emftext.language.java.references.IdentifierReference result = referencesFactory.createIdentifierReference();
		IBinding b = name.resolveBinding();
		org.emftext.language.java.references.ReferenceableElement target = null;
		if (b == null || b.isRecovered()) {
			target = jdtResolverUtility.getReferenceableElementByNameMatching(name.getIdentifier());
		} else if (b instanceof ITypeBinding) {
			target = jdtResolverUtility.getClassifier((ITypeBinding) b);
		} else if (b instanceof IVariableBinding) {
			target = jdtResolverUtility.getReferencableElement((IVariableBinding) b);
		} else if (b instanceof IMethodBinding) {
			target = jdtResolverUtility.getMethod((IMethodBinding) b);
		} else if (b instanceof IPackageBinding) {
			target = jdtResolverUtility.getPackage((IPackageBinding) b);
		} else {
			target = jdtResolverUtility.getReferenceableElementByNameMatching(name.getIdentifier());
		}
		target.setName(name.getIdentifier());
		result.setTarget(target);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, name);
		return result;
	}
	
}