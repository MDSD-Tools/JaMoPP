package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.google.inject.Singleton;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

@Singleton
public class ToClassMemberConverter implements Converter<BodyDeclaration, Member> {

	private Converter<AbstractTypeDeclaration, ConcreteClassifier> toConcreteClassifierConverter;
	private final Converter<Initializer, tools.mdsd.jamopp.model.java.statements.Block> toBlockConverter;
	private final Converter<FieldDeclaration, Field> toFieldConverter;
	private final Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter;
	private final Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> toInterfaceMethodConverter;

	@Inject
	ToClassMemberConverter(Converter<Initializer, tools.mdsd.jamopp.model.java.statements.Block> toBlockConverter,
			@Named("ToClassMethodOrConstructorConverter") Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter,
			Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> toInterfaceMethodConverter,
			Converter<FieldDeclaration, Field> toFieldConverter) {
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
	public void setToConcreteClassifierConverter(
			Converter<AbstractTypeDeclaration, ConcreteClassifier> toConcreteClassifierConverter) {
		this.toConcreteClassifierConverter = toConcreteClassifierConverter;
	}

}
