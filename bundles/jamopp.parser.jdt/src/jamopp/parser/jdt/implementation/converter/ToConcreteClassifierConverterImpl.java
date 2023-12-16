package jamopp.parser.jdt.implementation.converter;

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

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;

public class ToConcreteClassifierConverterImpl implements Converter<AbstractTypeDeclaration, ConcreteClassifier> {

	private final IUtilLayout layoutInformationConverter;
	private final IUtilJdtResolver jdtResolverUtility;
	private final IUtilNamedElement utilNamedElement;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<BodyDeclaration, Member> toInterfaceMember;
	private final Converter<TypeDeclaration, ConcreteClassifier> toClassOrInterface;
	private final Converter<EnumDeclaration, Enumeration> toEnumConverter;

	@Inject
	ToConcreteClassifierConverterImpl(
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			IUtilLayout layoutInformationConverter, IUtilJdtResolver jdtResolverUtility,
			IUtilNamedElement utilNamedElement,
			@Named("ToInterfaceMemberConverter") Converter<BodyDeclaration, Member> toInterfaceMember,
			Converter<EnumDeclaration, Enumeration> toEnumConverter,
			Converter<TypeDeclaration, ConcreteClassifier> toClassOrInterface) {
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
