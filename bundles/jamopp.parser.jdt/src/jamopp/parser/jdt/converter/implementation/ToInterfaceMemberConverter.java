package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.members.Member;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToInterfaceMemberConverter implements ToConverter<BodyDeclaration, Member> {

	private final ToConverter<MethodDeclaration, Member> toInterfaceMethodOrConstructorConverter;
	private final ToConverter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToInterfaceMemberConverter(
			@Named("ToInterfaceMethodOrConstructorConverter") ToConverter<MethodDeclaration, Member> toInterfaceMethodOrConstructorConverter,
			@Named("ToClassMemberConverter") ToConverter<BodyDeclaration, Member> toClassMemberConverter) {
		this.toInterfaceMethodOrConstructorConverter = toInterfaceMethodOrConstructorConverter;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@Override
	public Member convert(BodyDeclaration body) {
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return toInterfaceMethodOrConstructorConverter.convert((MethodDeclaration) body);
		}
		return toClassMemberConverter.convert(body);
	}

}
