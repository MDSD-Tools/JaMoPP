package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.references.ReferenceableElement;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToEnumConverter implements ToConverter<EnumDeclaration, Enumeration> {

	private final UtilJdtResolver utilJdtResolver;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToEnumConstantConverter toEnumConstantConverter;
	private final ToConverter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToEnumConverter(UtilJdtResolver utilJdtResolver, ToTypeReferenceConverter toTypeReferenceConverter,
			ToEnumConstantConverter toEnumConstantConverter,
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
