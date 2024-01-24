package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToInterfaceMemberConverter implements Converter<BodyDeclaration, Member> {

	private final Converter<MethodDeclaration, Member> toInterfaceMethodOrConstructorConverter;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	public ToInterfaceMemberConverter(
			@Named("ToInterfaceMethodOrConstructorConverter") final Converter<MethodDeclaration, Member> toInterfaceMethodOrConstructorConverter,
			@Named("ToClassMemberConverter") final Converter<BodyDeclaration, Member> toClassMemberConverter) {
		this.toInterfaceMethodOrConstructorConverter = toInterfaceMethodOrConstructorConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@Override
	public Member convert(final BodyDeclaration body) {
		Member result;
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			result = toInterfaceMethodOrConstructorConverter.convert((MethodDeclaration) body);
		} else {
			result = toClassMemberConverter.convert(body);
		}
		return result;
	}

}
