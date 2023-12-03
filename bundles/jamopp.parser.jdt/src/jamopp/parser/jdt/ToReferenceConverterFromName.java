package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;

public class ToReferenceConverterFromName {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;

	@Inject
	ToReferenceConverterFromName(ReferencesFactory referencesFactory, UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	org.emftext.language.java.references.IdentifierReference convertToIdentifierReference(Name name) {
		if (name.isSimpleName()) {
			return convertToIdentifierReference((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		org.emftext.language.java.references.IdentifierReference parent = convertToIdentifierReference(
				qualifiedName.getQualifier());
		org.emftext.language.java.references.IdentifierReference child = convertToIdentifierReference(
				qualifiedName.getName());
		parent.setNext(child);
		return child;
	}

	org.emftext.language.java.references.IdentifierReference convertToIdentifierReference(SimpleName name) {
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
