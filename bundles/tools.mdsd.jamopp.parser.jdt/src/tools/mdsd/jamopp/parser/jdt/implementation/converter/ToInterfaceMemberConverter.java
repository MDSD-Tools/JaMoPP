package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import tools.mdsd.jamopp.model.java.members.Member;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToInterfaceMemberConverter implements Converter<BodyDeclaration, Member> {

	private final Converter<MethodDeclaration, Member> toInterfaceMethodOrConstructorConverter;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToInterfaceMemberConverter(
			@Named("ToInterfaceMethodOrConstructorConverter") Converter<MethodDeclaration, Member> toInterfaceMethodOrConstructorConverter,
			@Named("ToClassMemberConverter") Converter<BodyDeclaration, Member> toClassMemberConverter) {
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
