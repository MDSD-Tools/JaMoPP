package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.members.Member;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jamopp.parser.jdt.converter.interfaces.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

@Singleton
public class ToClassMemberConverter implements ToConverter<BodyDeclaration, Member> {

	private ToConcreteClassifierConverter toConcreteClassifierConverter;
	private final ToBlockConverter toBlockConverter;
	private final ToFieldConverter toFieldConverter;
	private final ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter;
	private final ToInterfaceMethodConverter toInterfaceMethodConverter;

	@Inject
	ToClassMemberConverter(ToBlockConverter toBlockConverter,
			ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter,
			ToInterfaceMethodConverter toInterfaceMethodConverter, ToFieldConverter toFieldConverter) {
		this.toBlockConverter = toBlockConverter;
		this.toFieldConverter = toFieldConverter;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		this.toInterfaceMethodConverter = toInterfaceMethodConverter;
	}

	@Override
	public Member convert(BodyDeclaration body) {
		if (body instanceof AbstractTypeDeclaration) {
			return toConcreteClassifierConverter.convert((AbstractTypeDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.INITIALIZER) {
			return toBlockConverter.convert((Initializer) body);
		}
		if (body.getNodeType() == ASTNode.FIELD_DECLARATION) {
			return toFieldConverter.convert((FieldDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return toClassMethodOrConstructorConverter.convert((MethodDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION) {
			return toInterfaceMethodConverter.convert((AnnotationTypeMemberDeclaration) body);
		}
		return null;
	}

	@Inject
	public void setToConcreteClassifierConverter(ToConcreteClassifierConverter toConcreteClassifierConverter) {
		this.toConcreteClassifierConverter = toConcreteClassifierConverter;
	}

}
