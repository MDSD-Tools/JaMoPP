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

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;

public class ToEnumConverter implements Converter<EnumDeclaration, Enumeration> {

	private final IUtilJdtResolver iUtilJdtResolver;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<EnumConstantDeclaration, EnumConstant> toEnumConstantConverter;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToEnumConverter(IUtilJdtResolver iUtilJdtResolver, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<EnumConstantDeclaration, EnumConstant> toEnumConstantConverter,
			@Named("ToClassMemberConverter") Converter<BodyDeclaration, Member> toClassMemberConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toEnumConstantConverter = toEnumConstantConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration convert(EnumDeclaration enumDecl) {
		Enumeration result = iUtilJdtResolver.getEnumeration(enumDecl.resolveBinding());
		enumDecl.superInterfaceTypes()
				.forEach(obj -> result.getImplements().add(toTypeReferenceConverter.convert((Type) obj)));
		enumDecl.enumConstants().forEach(
				obj -> result.getConstants().add(toEnumConstantConverter.convert((EnumConstantDeclaration) obj)));
		enumDecl.bodyDeclarations()
				.forEach(obj -> result.getMembers().add(toClassMemberConverter.convert((BodyDeclaration) obj)));
		return result;
	}

}
