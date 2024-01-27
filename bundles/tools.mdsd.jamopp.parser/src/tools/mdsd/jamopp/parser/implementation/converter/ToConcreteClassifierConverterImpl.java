package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToConcreteClassifierConverterImpl implements Converter<AbstractTypeDeclaration, ConcreteClassifier> {

	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<BodyDeclaration, Member> toInterfaceMember;
	private final Converter<TypeDeclaration, ConcreteClassifier> toClassOrInterface;
	private final Converter<EnumDeclaration, Enumeration> toEnumConverter;

	@Inject
	public ToConcreteClassifierConverterImpl(
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final UtilNamedElement utilNamedElement,
			@Named("ToInterfaceMemberConverter") final Converter<BodyDeclaration, Member> toInterfaceMember,
			final Converter<EnumDeclaration, Enumeration> toEnumConverter,
			final Converter<TypeDeclaration, ConcreteClassifier> toClassOrInterface) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toInterfaceMember = toInterfaceMember;
		this.toClassOrInterface = toClassOrInterface;
		this.toEnumConverter = toEnumConverter;
		this.utilNamedElement = utilNamedElement;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ConcreteClassifier convert(final AbstractTypeDeclaration typeDecl) {
		ConcreteClassifier result;
		if (typeDecl.getNodeType() == ASTNode.TYPE_DECLARATION) {
			result = toClassOrInterface.convert((TypeDeclaration) typeDecl);
		} else if (typeDecl.getNodeType() == ASTNode.ANNOTATION_TYPE_DECLARATION) {
			result = jdtResolverUtility.getAnnotation(typeDecl.resolveBinding());
			final ConcreteClassifier concreteClassifier = result;
			typeDecl.bodyDeclarations().forEach(
					obj -> concreteClassifier.getMembers().add(toInterfaceMember.convert((BodyDeclaration) obj)));
		} else {
			result = toEnumConverter.convert((EnumDeclaration) typeDecl);
		}
		final ConcreteClassifier finalResult = result;
		typeDecl.modifiers().forEach(obj -> finalResult.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		utilNamedElement.setNameOfElement(typeDecl.getName(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, typeDecl);
		return result;
	}

}
