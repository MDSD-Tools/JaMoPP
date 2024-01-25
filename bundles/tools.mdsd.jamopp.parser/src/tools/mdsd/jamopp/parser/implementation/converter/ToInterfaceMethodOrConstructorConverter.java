package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToInterfaceMethodOrConstructorConverter implements Converter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final JdtResolver utilJdtResolver;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final UtilNamedElement utilNamedElement;
	private final Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> toTypeParameterConverter;
	private final Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter;
	private final Converter<SingleVariableDeclaration, Parameter> toParameterConverter;
	private final Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper;

	@Inject
	public ToInterfaceMethodOrConstructorConverter(final UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			final UtilNamedElement utilNamedElement, final UtilLayout utilLayout, final JdtResolver iUtilJdtResolver,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> toTypeParameterConverter,
			final Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter,
			final Converter<SingleVariableDeclaration, Parameter> toParameterConverter,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			@Named("ToClassMethodOrConstructorConverter") final Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			final Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper,
			final StatementsFactory statementsFactory,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		utilJdtResolver = iUtilJdtResolver;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.utilNamedElement = utilNamedElement;
		this.toReceiverParameterConverter = toReceiverParameterConverter;
		this.toParameterConverter = toParameterConverter;
		this.inNamespaceClassifierReferenceWrapper = inNamespaceClassifierReferenceWrapper;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member convert(final MethodDeclaration methodDecl) {
		Member member;
		if (methodDecl.isConstructor()) {
			member = toClassMethodOrConstructorConverter.convert(methodDecl);
		} else {
			InterfaceMethod result;
			final IMethodBinding binding = methodDecl.resolveBinding();
			if (binding == null) {
				result = utilJdtResolver.getInterfaceMethod(methodDecl.getName().getIdentifier());
			} else {
				result = utilJdtResolver.getInterfaceMethod(binding);
			}
			methodDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
					.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
			methodDecl.typeParameters().forEach(
					obj -> result.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
			result.setTypeReference(toTypeReferenceConverter.convert(methodDecl.getReturnType2()));
			utilToArrayDimensionsAndSetConverter.convert(methodDecl.getReturnType2(), result);
			methodDecl.extraDimensions()
					.forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convert((Dimension) obj, result));
			utilNamedElement.setNameOfElement(methodDecl.getName(), result);
			if (methodDecl.getReceiverType() != null) {
				result.getParameters().add(toReceiverParameterConverter.convert(methodDecl));
			}
			methodDecl.parameters().forEach(
					obj -> result.getParameters().add(toParameterConverter.convert((SingleVariableDeclaration) obj)));
			methodDecl.thrownExceptionTypes().forEach(obj -> result.getExceptions()
					.add(inNamespaceClassifierReferenceWrapper.convert(toTypeReferenceConverter.convert((Type) obj))));
			if (methodDecl.getBody() != null) {
				utilTypeInstructionSeparation.addMethod(methodDecl.getBody(), result);
			} else {
				result.setStatement(statementsFactory.createEmptyStatement());
			}
			utilLayout.convertToMinimalLayoutInformation(result, methodDecl);
			member = result;
		}
		return member;
	}

}
