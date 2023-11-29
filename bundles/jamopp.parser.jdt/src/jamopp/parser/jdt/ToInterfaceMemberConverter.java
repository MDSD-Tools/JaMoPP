package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

class ToInterfaceMemberConverter {

	private final ToInterfaceMethodOrConstructorConverter toInterfaceMethodOrConstructorConverter;
	private final ToClassMemberConverter toClassMemberConverter;

	ToInterfaceMemberConverter(ToInterfaceMethodOrConstructorConverter toInterfaceMethodOrConstructorConverter,
			ToClassMemberConverter toClassMemberConverter) {
		this.toInterfaceMethodOrConstructorConverter = toInterfaceMethodOrConstructorConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	org.emftext.language.java.members.Member convertToInterfaceMember(BodyDeclaration body) {
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return toInterfaceMethodOrConstructorConverter
					.convertToInterfaceMethodOrConstructor((MethodDeclaration) body);
		}
		return toClassMemberConverter.convertToClassMember(body);
	}

}
