package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.converter.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToConcreteClassifierConverterImpl
		implements ToConcreteClassifierConverter, ToConverter<AbstractTypeDeclaration, ConcreteClassifier> {

	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final ToConverter<BodyDeclaration, Member> toInterfaceMember;
	private final ToConverter<TypeDeclaration, ConcreteClassifier> toClassOrInterface;
	private final ToConverter<EnumDeclaration, Enumeration> toEnumConverter;

	@Inject
	ToConcreteClassifierConverterImpl(
			ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			UtilNamedElement utilNamedElement,
			@Named("ToInterfaceMemberConverter") ToConverter<BodyDeclaration, Member> toInterfaceMember,
			ToConverter<EnumDeclaration, Enumeration> toEnumConverter,
			ToConverter<TypeDeclaration, ConcreteClassifier> toClassOrInterface) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toInterfaceMember = toInterfaceMember;
		this.toClassOrInterface = toClassOrInterface;
		this.toEnumConverter = toEnumConverter;
		this.utilNamedElement = utilNamedElement;
	}

	@SuppressWarnings("unchecked")
	public ConcreteClassifier convert(AbstractTypeDeclaration typeDecl) {
		ConcreteClassifier result = null;
		if (typeDecl.getNodeType() == ASTNode.TYPE_DECLARATION) {
			result = toClassOrInterface.convert((TypeDeclaration) typeDecl);
		} else if (typeDecl.getNodeType() == ASTNode.ANNOTATION_TYPE_DECLARATION) {
			result = jdtResolverUtility.getAnnotation(typeDecl.resolveBinding());
			ConcreteClassifier fR = result;
			typeDecl.bodyDeclarations()
					.forEach(obj -> fR.getMembers().add(toInterfaceMember.convert((BodyDeclaration) obj)));
		} else {
			result = toEnumConverter.convert((EnumDeclaration) typeDecl);
		}
		ConcreteClassifier finalResult = result;
		typeDecl.modifiers().forEach(obj -> finalResult.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		utilNamedElement.setNameOfElement(typeDecl.getName(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, typeDecl);
		return result;
	}

}
