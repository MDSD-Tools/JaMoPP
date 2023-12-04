package jamopp.parser.jdt.converter.other;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Interface;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.UtilJdtResolver;

public class ToClassOrInterfaceConverter {

	private final ToTypeParameterConverter toTypeParameterConverter;
	private final UtilJdtResolver utilJdtResolver;
	private final ToClassMemberConverter toClassMemberConverter;
	private final ToInterfaceMemberConverter toInterfaceMemberConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;

	@Inject
	ToClassOrInterfaceConverter(UtilJdtResolver utilJdtResolver, ToTypeReferenceConverter toTypeReferenceConverter,
			ToTypeParameterConverter toTypeParameterConverter, ToInterfaceMemberConverter toInterfaceMemberConverter,
			ToClassMemberConverter toClassMemberConverter) {
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.utilJdtResolver = utilJdtResolver;
		this.toClassMemberConverter = toClassMemberConverter;
		this.toInterfaceMemberConverter = toInterfaceMemberConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
	}

	@SuppressWarnings("unchecked")
	ConcreteClassifier convertToClassOrInterface(TypeDeclaration typeDecl) {
		if (typeDecl.isInterface()) {
			Interface interfaceObj = utilJdtResolver.getInterface(typeDecl.resolveBinding());
			typeDecl.typeParameters().forEach(obj -> interfaceObj.getTypeParameters()
					.add(toTypeParameterConverter.convert((TypeParameter) obj)));
			typeDecl.superInterfaceTypes().forEach(
					obj -> interfaceObj.getExtends().add(toTypeReferenceConverter.convert((Type) obj)));
			typeDecl.bodyDeclarations().forEach(obj -> interfaceObj.getMembers()
					.add(toInterfaceMemberConverter.convert((BodyDeclaration) obj)));
			return interfaceObj;
		}
		org.emftext.language.java.classifiers.Class classObj = utilJdtResolver.getClass(typeDecl.resolveBinding());
		typeDecl.typeParameters().forEach(obj -> classObj.getTypeParameters()
				.add(toTypeParameterConverter.convert((TypeParameter) obj)));
		if (typeDecl.getSuperclassType() != null) {
			classObj.setExtends(toTypeReferenceConverter.convert(typeDecl.getSuperclassType()));
		}
		typeDecl.superInterfaceTypes().forEach(
				obj -> classObj.getImplements().add(toTypeReferenceConverter.convert((Type) obj)));
		typeDecl.bodyDeclarations().forEach(
				obj -> classObj.getMembers().add(toClassMemberConverter.convertToClassMember((BodyDeclaration) obj)));
		return classObj;
	}

}
