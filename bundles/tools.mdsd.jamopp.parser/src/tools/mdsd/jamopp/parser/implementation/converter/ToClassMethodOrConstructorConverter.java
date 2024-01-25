package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Constructor;
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

public class ToClassMethodOrConstructorConverter implements Converter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final JdtResolver jdtResolverUtility;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilNamedElement utilNamedElement;
	private final Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> toTypeParameterConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<SingleVariableDeclaration, Parameter> toParameterConverter;
	private final Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper;

	@Inject
	public ToClassMethodOrConstructorConverter(final UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			final UtilNamedElement utilNamedElement, final UtilLayout utilLayout,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> toTypeParameterConverter,
			final Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter,
			final Converter<SingleVariableDeclaration, Parameter> toParameterConverter,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			final JdtResolver jdtResolverUtility,
			final Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper,
			final StatementsFactory statementsFactory,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.toReceiverParameterConverter = toReceiverParameterConverter;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toParameterConverter = toParameterConverter;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.inNamespaceClassifierReferenceWrapper = inNamespaceClassifierReferenceWrapper;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	public Member convert(final MethodDeclaration methodDecl) {
		Member result;
		if (methodDecl.isConstructor()) {
			result = handleConstructor(methodDecl);
		} else {
			result = handleClassMethod(methodDecl);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Member handleClassMethod(final MethodDeclaration methodDecl) {
		ClassMethod result;
		final IMethodBinding binding = methodDecl.resolveBinding();
		if (binding != null) {
			result = jdtResolverUtility.getClassMethod(binding);
		} else {
			result = jdtResolverUtility.getClassMethod(methodDecl.getName().getIdentifier());
		}
		methodDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		methodDecl.typeParameters()
				.forEach(obj -> result.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
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
		return result;
	}

	@SuppressWarnings("unchecked")
	private Member handleConstructor(final MethodDeclaration methodDecl) {
		Constructor result;
		final IMethodBinding binding = methodDecl.resolveBinding();
		if (binding == null) {
			result = jdtResolverUtility.getConstructor(methodDecl.getName().getIdentifier());
		} else {
			result = jdtResolverUtility.getConstructor(binding);
		}
		methodDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		methodDecl.typeParameters()
				.forEach(obj -> result.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
		utilNamedElement.setNameOfElement(methodDecl.getName(), result);
		if (methodDecl.getReceiverType() != null) {
			result.getParameters().add(toReceiverParameterConverter.convert(methodDecl));
		}
		methodDecl.parameters().forEach(
				obj -> result.getParameters().add(toParameterConverter.convert((SingleVariableDeclaration) obj)));
		methodDecl.thrownExceptionTypes().forEach(obj -> result.getExceptions()
				.add(inNamespaceClassifierReferenceWrapper.convert(toTypeReferenceConverter.convert((Type) obj))));
		utilTypeInstructionSeparation.addConstructor(methodDecl.getBody(), result);
		utilLayout.convertToMinimalLayoutInformation(result, methodDecl);
		return result;
	}

}
