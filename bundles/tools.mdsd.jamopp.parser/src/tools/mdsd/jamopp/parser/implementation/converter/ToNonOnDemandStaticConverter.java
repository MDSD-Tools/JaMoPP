package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

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
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToNonOnDemandStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ModifiersFactory modifiersFactory;
	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToNonOnDemandStaticConverter(final UtilNamedElement utilNamedElement,
			final ModifiersFactory modifiersFactory, final UtilLayout layoutInformationConverter,
			final JdtResolver jdtResolverUtility, final ImportsFactory importsFactory) {
		this.modifiersFactory = modifiersFactory;
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public Import convert(final ImportDeclaration importDecl) {
		final StaticMemberImport convertedImport = importsFactory.createStaticMemberImport();
		convertedImport.setStatic(modifiersFactory.createStatic());
		final QualifiedName qualifiedName = (QualifiedName) importDecl.getName();
		final IBinding iBinding = qualifiedName.resolveBinding();

		ReferenceableElement proxyMember;
		Classifier proxyClass = null;

		if (iBinding instanceof IMethodBinding) {
			proxyMember = jdtResolverUtility.getMethod((IMethodBinding) iBinding);
		} else if (iBinding instanceof IVariableBinding) {
			proxyMember = jdtResolverUtility.getReferencableElement((IVariableBinding) iBinding);
		} else if (iBinding instanceof final ITypeBinding typeBinding && typeBinding.isNested()) {
			proxyMember = jdtResolverUtility.getClassifier(typeBinding);
			proxyClass = jdtResolverUtility.getClassifier(typeBinding.getDeclaringClass());
		} else if (iBinding instanceof final ITypeBinding typeBinding) {
			proxyClass = jdtResolverUtility.getClassifier(typeBinding);
			final ConcreteClassifier conCl = (ConcreteClassifier) proxyClass;
			proxyMember = findProxyMember(qualifiedName, conCl);
			if (proxyMember == null) {
				proxyMember = jdtResolverUtility.getClassMethod(qualifiedName.getFullyQualifiedName());
				proxyMember.setName(qualifiedName.getName().getIdentifier());
				conCl.getMembers().add((Member) proxyMember);
			}
		} else {
			proxyMember = jdtResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		}

		proxyMember.setName(qualifiedName.getName().getIdentifier());
		convertedImport.getStaticMembers().add(proxyMember);
		if (proxyClass == null) {
			proxyClass = handleProxyClassIsNull(qualifiedName);
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		utilNamedElement.addNameToNameSpaceAndElement(qualifiedName.getQualifier(), convertedImport, proxyClass);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private ReferenceableElement findProxyMember(final QualifiedName qualifiedName, final ConcreteClassifier conCl) {
		ReferenceableElement newProxyMember = null;
		for (final Member m : conCl.getMembers()) {
			if (!(m instanceof Constructor) && m.getName().equals(qualifiedName.getName().getIdentifier())) {
				newProxyMember = (ReferenceableElement) m;
				break;
			}
		}
		return newProxyMember;
	}

	private Classifier handleProxyClassIsNull(final QualifiedName qualifiedName) {
		Classifier proxyClass;
		final IBinding binding = qualifiedName.getQualifier().resolveBinding();
		if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
			proxyClass = jdtResolverUtility.getClass(qualifiedName.getQualifier().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getClassifier((ITypeBinding) binding);
		}
		return proxyClass;
	}

}
