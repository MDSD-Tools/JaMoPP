package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.members.Member;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToInterfaceMemberConverter implements ToConverter<BodyDeclaration, Member> {

	private final ToInterfaceMethodOrConstructorConverter toInterfaceMethodOrConstructorConverter;
	private final ToClassMemberConverter toClassMemberConverter;

	@Inject
	ToInterfaceMemberConverter(ToInterfaceMethodOrConstructorConverter toInterfaceMethodOrConstructorConverter,
			ToClassMemberConverter toClassMemberConverter) {
		this.toInterfaceMethodOrConstructorConverter = toInterfaceMethodOrConstructorConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	public Member convert(BodyDeclaration body) {
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return toInterfaceMethodOrConstructorConverter.convert((MethodDeclaration) body);
		}
		return toClassMemberConverter.convertToClassMember(body);
	}

}
