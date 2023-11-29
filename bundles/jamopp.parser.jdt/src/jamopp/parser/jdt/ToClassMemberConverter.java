package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.members.Member;

class ToClassMemberConverter {

	private ToConcreteClassifierConverter toConcreteClassifierConverter;
	private final ToBlockConverter toBlockConverter;
	private final ToFieldConverter toFieldConverter;
	private final ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter;
	private final ToInterfaceMethodConverter toInterfaceMethodConverter;

	public ToClassMemberConverter(ToBlockConverter toBlockConverter,
			ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter,
			ToInterfaceMethodConverter toInterfaceMethodConverter, ToFieldConverter toFieldConverter) {
		this.toBlockConverter = toBlockConverter;
		this.toFieldConverter = toFieldConverter;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		this.toInterfaceMethodConverter = toInterfaceMethodConverter;
	}

	Member convertToClassMember(BodyDeclaration body) {
		if (body instanceof AbstractTypeDeclaration) {
			return toConcreteClassifierConverter.convertToConcreteClassifier((AbstractTypeDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.INITIALIZER) {
			return toBlockConverter.convertToBlock((Initializer) body);
		}
		if (body.getNodeType() == ASTNode.FIELD_DECLARATION) {
			return toFieldConverter.convertToField((FieldDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return toClassMethodOrConstructorConverter.convertToClassMethodOrConstructor((MethodDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION) {
			return toInterfaceMethodConverter.convertToInterfaceMethod((AnnotationTypeMemberDeclaration) body);
		}
		return null;
	}
	
	public void setToConcreteClassifierConverter(ToConcreteClassifierConverter toConcreteClassifierConverter) {
		this.toConcreteClassifierConverter = toConcreteClassifierConverter;
	}

}
