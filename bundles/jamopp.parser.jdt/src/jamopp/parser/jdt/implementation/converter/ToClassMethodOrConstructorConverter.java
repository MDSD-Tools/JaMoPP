package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class ToClassMethodOrConstructorConverter implements Converter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilNamedElement utilNamedElement;
	private final Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<SingleVariableDeclaration, Parameter> toParameterConverter;
	private final Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper;

	@Inject
	ToClassMethodOrConstructorConverter(UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			UtilNamedElement utilNamedElement, UtilLayout utilLayout,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter,
			Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter,
			Converter<SingleVariableDeclaration, Parameter> toParameterConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			UtilJdtResolver jdtResolverUtility,
			Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper,
			StatementsFactory statementsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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

	@SuppressWarnings("unchecked")
	@Override
	public Member convert(MethodDeclaration methodDecl) {
		if (methodDecl.isConstructor()) {
			Constructor result;
			IMethodBinding binding = methodDecl.resolveBinding();
			if (binding == null) {
				result = jdtResolverUtility.getConstructor(methodDecl.getName().getIdentifier());
			} else {
				result = jdtResolverUtility.getConstructor(binding);
			}
			methodDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
					.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
			methodDecl.typeParameters().forEach(
					obj -> result.getTypeParameters().add(toTypeParameterConverter.convert((TypeParameter) obj)));
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
		ClassMethod result;
		IMethodBinding binding = methodDecl.resolveBinding();
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
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(methodDecl.getReturnType2(), result);
		methodDecl.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
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

}