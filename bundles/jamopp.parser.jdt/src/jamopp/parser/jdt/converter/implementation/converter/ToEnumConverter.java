package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToEnumConverter implements ToConverter<EnumDeclaration, Enumeration> {

	private final UtilJdtResolver utilJdtResolver;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<EnumConstantDeclaration, EnumConstant> toEnumConstantConverter;
	private final ToConverter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToEnumConverter(UtilJdtResolver utilJdtResolver, ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<EnumConstantDeclaration, EnumConstant> toEnumConstantConverter,
			@Named("ToClassMemberConverter") ToConverter<BodyDeclaration, Member> toClassMemberConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toEnumConstantConverter = toEnumConstantConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration convert(EnumDeclaration enumDecl) {
		Enumeration result = utilJdtResolver.getEnumeration(enumDecl.resolveBinding());
		enumDecl.superInterfaceTypes()
				.forEach(obj -> result.getImplements().add(toTypeReferenceConverter.convert((Type) obj)));
		enumDecl.enumConstants().forEach(
				obj -> result.getConstants().add(toEnumConstantConverter.convert((EnumConstantDeclaration) obj)));
		enumDecl.bodyDeclarations()
				.forEach(obj -> result.getMembers().add(toClassMemberConverter.convert((BodyDeclaration) obj)));
		return result;
	}

}
