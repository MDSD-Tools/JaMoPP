package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToClassOrInterfaceConverter implements Converter<TypeDeclaration, ConcreteClassifier> {

	private final JdtResolver iUtilJdtResolver;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;
	private final Converter<BodyDeclaration, Member> toInterfaceMemberConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> toTypeParameterConverter;

	@Inject
	ToClassOrInterfaceConverter(JdtResolver iUtilJdtResolver, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> toTypeParameterConverter,
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
		tools.mdsd.jamopp.model.java.classifiers.Class classObj = iUtilJdtResolver.getClass(typeDecl.resolveBinding());
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
