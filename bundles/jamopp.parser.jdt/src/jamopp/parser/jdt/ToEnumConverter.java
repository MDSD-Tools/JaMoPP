package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.classifiers.Enumeration;

import com.google.inject.Inject;

class ToEnumConverter {

	private final UtilJdtResolver utilJdtResolver;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToEnumConstantConverter toEnumConstantConverter;
	private final ToClassMemberConverter toClassMemberConverter;

	@Inject
	ToEnumConverter(UtilJdtResolver utilJdtResolver, ToTypeReferenceConverter toTypeReferenceConverter,
			ToEnumConstantConverter toEnumConstantConverter, ToClassMemberConverter toClassMemberConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toEnumConstantConverter = toEnumConstantConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	Enumeration convertToEnum(EnumDeclaration enumDecl) {
		Enumeration result = utilJdtResolver.getEnumeration(enumDecl.resolveBinding());
		enumDecl.superInterfaceTypes().forEach(
				obj -> result.getImplements().add(toTypeReferenceConverter.convertToTypeReference((Type) obj)));
		enumDecl.enumConstants().forEach(obj -> result.getConstants()
				.add(toEnumConstantConverter.convertToEnumConstant((EnumConstantDeclaration) obj)));
		enumDecl.bodyDeclarations().forEach(
				obj -> result.getMembers().add(toClassMemberConverter.convertToClassMember((BodyDeclaration) obj)));
		return result;
	}

}
