package jamopp.parser.jdt.converter.implementation.converter;

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

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToClassMethodOrConstructorConverter implements ToConverter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final UtilJdtResolver jdtResolverUtility;
	private final IUtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final IUtilLayout utilLayout;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final IUtilNamedElement utilNamedElement;
	private final ToConverter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter;
	private final ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<SingleVariableDeclaration, Parameter> toParameterConverter;
	private final ToConverter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper;

	@Inject
	ToClassMethodOrConstructorConverter(IUtilTypeInstructionSeparation utilTypeInstructionSeparation,
			IUtilNamedElement utilNamedElement, IUtilLayout utilLayout,
			ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter,
			ToConverter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter,
			ToConverter<SingleVariableDeclaration, Parameter> toParameterConverter,
			ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			UtilJdtResolver jdtResolverUtility,
			ToConverter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper,
			StatementsFactory statementsFactory,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
