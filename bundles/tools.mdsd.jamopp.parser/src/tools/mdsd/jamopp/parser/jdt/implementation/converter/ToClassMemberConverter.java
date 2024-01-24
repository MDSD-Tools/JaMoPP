package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

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

	private final Provider<Converter<AbstractTypeDeclaration, ConcreteClassifier>> toConcreteClassifierConverter;
	private final Converter<Initializer, tools.mdsd.jamopp.model.java.statements.Block> toBlockConverter;
	private final Converter<FieldDeclaration, Field> toFieldConverter;
	private final Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter;
	private final Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> toInterfaceMethodConverter;

	@Inject
	public ToClassMemberConverter(
			final Converter<Initializer, tools.mdsd.jamopp.model.java.statements.Block> toBlockConverter,
			@Named("ToClassMethodOrConstructorConverter") final Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter,
			final Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> toInterfaceMethodConverter,
			final Converter<FieldDeclaration, Field> toFieldConverter,
			final Provider<Converter<AbstractTypeDeclaration, ConcreteClassifier>> toConcreteClassifierConverter) {
		this.toConcreteClassifierConverter = toConcreteClassifierConverter;
		this.toBlockConverter = toBlockConverter;
		this.toFieldConverter = toFieldConverter;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		this.toInterfaceMethodConverter = toInterfaceMethodConverter;
	}

	@Override
	public Member convert(final BodyDeclaration body) {
		Member result = null;
		if (body instanceof AbstractTypeDeclaration) {
			result = toConcreteClassifierConverter.get().convert((AbstractTypeDeclaration) body);
		} else if (body.getNodeType() == ASTNode.INITIALIZER) {
			result = toBlockConverter.convert((Initializer) body);
		} else if (body.getNodeType() == ASTNode.FIELD_DECLARATION) {
			result = toFieldConverter.convert((FieldDeclaration) body);
		} else if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			result = toClassMethodOrConstructorConverter.convert((MethodDeclaration) body);
		} else if (body.getNodeType() == ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION) {
			result = toInterfaceMethodConverter.convert((AnnotationTypeMemberDeclaration) body);
		}
		return result;
	}

}
