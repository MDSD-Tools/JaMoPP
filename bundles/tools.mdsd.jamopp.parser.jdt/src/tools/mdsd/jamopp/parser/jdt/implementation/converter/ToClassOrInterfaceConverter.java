package tools.mdsd.jamopp.parser.jdt.implementation.converter;

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

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class ToClassOrInterfaceConverter implements Converter<TypeDeclaration, ConcreteClassifier> {

	private final UtilJdtResolver iUtilJdtResolver;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;
	private final Converter<BodyDeclaration, Member> toInterfaceMemberConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter;

	@Inject
	ToClassOrInterfaceConverter(UtilJdtResolver iUtilJdtResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter,
			@Named("ToInterfaceMemberConverter") Converter<BodyDeclaration, Member> toInterfaceMemberConverter,
			@Named("ToClassMemberConverter") Converter<BodyDeclaration, Member> toClassMemberConverter) {
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.toClassMemberConverter = toClassMemberConverter;
		this.toInterfaceMemberConverter = toInterfaceMemberConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConcreteClassifier convert(TypeDeclaration typeDecl) {
		if (typeDecl.isInterface()) {
			Interface interfaceObj = iUtilJdtResolver.getInterface(typeDecl.resolveBinding());
			typeDecl.typeParameters().forEach(
					obj -> interfaceObj.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
			typeDecl.superInterfaceTypes()
					.forEach(obj -> interfaceObj.getExtends().add(toTypeReferenceConverter.convert((Type) obj)));
			typeDecl.bodyDeclarations().forEach(
					obj -> interfaceObj.getMembers().add(toInterfaceMemberConverter.convert((BodyDeclaration) obj)));
			return interfaceObj;
		}
		org.emftext.language.java.classifiers.Class classObj = iUtilJdtResolver.getClass(typeDecl.resolveBinding());
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
