package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.model.java.imports.StaticMemberImport;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToNonOnDemandStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ModifiersFactory modifiersFactory;
	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToNonOnDemandStaticConverter(UtilNamedElement utilNamedElement, ModifiersFactory modifiersFactory,
			UtilLayout layoutInformationConverter, JdtResolver jdtResolverUtility,
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
