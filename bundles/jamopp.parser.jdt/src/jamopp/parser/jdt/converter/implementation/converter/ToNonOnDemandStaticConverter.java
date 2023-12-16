package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.imports.StaticMemberImport;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.references.ReferenceableElement;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToNonOnDemandStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ModifiersFactory modifiersFactory;
	private final ImportsFactory importsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtResolverUtility;

	@Inject
	public ToNonOnDemandStaticConverter(IUtilNamedElement utilNamedElement, ModifiersFactory modifiersFactory,
			IUtilLayout layoutInformationConverter, IUtilJdtResolver jdtResolverUtility,
			ImportsFactory importsFactory) {
		this.modifiersFactory = modifiersFactory;
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public Import convert(ImportDeclaration importDecl) {
		StaticMemberImport convertedImport = importsFactory.createStaticMemberImport();
		convertedImport.setStatic(modifiersFactory.createStatic());
		QualifiedName qualifiedName = (QualifiedName) importDecl.getName();
		IBinding b = qualifiedName.resolveBinding();
		ReferenceableElement proxyMember = null;
		Classifier proxyClass = null;
		if (b == null || b.isRecovered()) {
			proxyMember = jdtResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		} else if (b instanceof IMethodBinding) {
			proxyMember = jdtResolverUtility.getMethod((IMethodBinding) b);
		} else if (b instanceof IVariableBinding) {
			proxyMember = jdtResolverUtility.getReferencableElement((IVariableBinding) b);
		} else if (b instanceof ITypeBinding typeBinding) {
			if (!typeBinding.isNested()) {
				proxyClass = jdtResolverUtility.getClassifier(typeBinding);
				ConcreteClassifier conCl = (ConcreteClassifier) proxyClass;
				for (Member m : conCl.getMembers()) {
					if (!(m instanceof Constructor) && m.getName().equals(qualifiedName.getName().getIdentifier())) {
						proxyMember = (ReferenceableElement) m;
						break;
					}
				}
				if (proxyMember == null) {
					proxyMember = jdtResolverUtility.getClassMethod(qualifiedName.getFullyQualifiedName());
					proxyMember.setName(qualifiedName.getName().getIdentifier());
					conCl.getMembers().add((Member) proxyMember);
				}
			} else {
				proxyMember = jdtResolverUtility.getClassifier(typeBinding);
				proxyClass = jdtResolverUtility.getClassifier(typeBinding.getDeclaringClass());
			}
		} else {
			proxyMember = jdtResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		}
		proxyMember.setName(qualifiedName.getName().getIdentifier());
		convertedImport.getStaticMembers().add(proxyMember);
		if (proxyClass == null) {
			IBinding binding = qualifiedName.getQualifier().resolveBinding();
			if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
				proxyClass = jdtResolverUtility.getClass(qualifiedName.getQualifier().getFullyQualifiedName());
			} else {
				proxyClass = jdtResolverUtility.getClassifier((ITypeBinding) binding);
			}
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		utilNamedElement.addNameToNameSpaceAndElement(qualifiedName.getQualifier(), convertedImport, proxyClass);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

}
