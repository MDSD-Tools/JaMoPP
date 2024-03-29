package tools.mdsd.jamopp.parser.implementation.converter;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToEnumConverter implements Converter<EnumDeclaration, Enumeration> {

	private final JdtResolver iUtilJdtResolver;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<EnumConstantDeclaration, EnumConstant> toEnumConstantConverter;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	public ToEnumConverter(final JdtResolver iUtilJdtResolver,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<EnumConstantDeclaration, EnumConstant> toEnumConstantConverter,
			@Named("ToClassMemberConverter") final Converter<BodyDeclaration, Member> toClassMemberConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toEnumConstantConverter = toEnumConstantConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration convert(final EnumDeclaration enumDecl) {
		final Enumeration result = iUtilJdtResolver.getEnumeration(enumDecl.resolveBinding());
		enumDecl.superInterfaceTypes()
				.forEach(obj -> result.getImplements().add(toTypeReferenceConverter.convert((Type) obj)));
		enumDecl.enumConstants().forEach(
				obj -> result.getConstants().add(toEnumConstantConverter.convert((EnumConstantDeclaration) obj)));
		enumDecl.bodyDeclarations()
				.forEach(obj -> result.getMembers().add(toClassMemberConverter.convert((BodyDeclaration) obj)));
		return result;
	}

}
