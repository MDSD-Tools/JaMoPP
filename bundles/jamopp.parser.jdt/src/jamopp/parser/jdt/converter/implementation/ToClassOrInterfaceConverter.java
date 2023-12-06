package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToClassOrInterfaceConverter implements ToConverter<TypeDeclaration, ConcreteClassifier> {

	private final UtilJdtResolver utilJdtResolver;
	private final ToConverter<BodyDeclaration, Member> toClassMemberConverter;
	private final ToConverter<BodyDeclaration, Member> toInterfaceMemberConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter;

	@Inject
	ToClassOrInterfaceConverter(UtilJdtResolver utilJdtResolver,
			ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter,
			@Named("ToInterfaceMemberConverter") ToConverter<BodyDeclaration, Member> toInterfaceMemberConverter,
			@Named("ToClassMemberConverter") ToConverter<BodyDeclaration, Member> toClassMemberConverter) {
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.utilJdtResolver = utilJdtResolver;
		this.toClassMemberConverter = toClassMemberConverter;
		this.toInterfaceMemberConverter = toInterfaceMemberConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConcreteClassifier convert(TypeDeclaration typeDecl) {
		if (typeDecl.isInterface()) {
			Interface interfaceObj = utilJdtResolver.getInterface(typeDecl.resolveBinding());
			typeDecl.typeParameters().forEach(
					obj -> interfaceObj.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
			typeDecl.superInterfaceTypes()
					.forEach(obj -> interfaceObj.getExtends().add(toTypeReferenceConverter.convert((Type) obj)));
			typeDecl.bodyDeclarations().forEach(
					obj -> interfaceObj.getMembers().add(toInterfaceMemberConverter.convert((BodyDeclaration) obj)));
			return interfaceObj;
		}
		org.emftext.language.java.classifiers.Class classObj = utilJdtResolver.getClass(typeDecl.resolveBinding());
		typeDecl.typeParameters().forEach(
				obj -> classObj.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
		if (typeDecl.getSuperclassType() != null) {
			classObj.setExtends(toTypeReferenceConverter.convert(typeDecl.getSuperclassType()));
		}
		typeDecl.superInterfaceTypes()
				.forEach(obj -> classObj.getImplements().add(toTypeReferenceConverter.convert((Type) obj)));
		typeDecl.bodyDeclarations()
				.forEach(obj -> classObj.getMembers().add(toClassMemberConverter.convert((BodyDeclaration) obj)));
		return classObj;
	}

}
