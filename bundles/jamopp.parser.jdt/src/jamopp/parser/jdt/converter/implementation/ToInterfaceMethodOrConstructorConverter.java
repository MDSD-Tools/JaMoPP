package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.helper.UtilNamedElement;
import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToInterfaceMethodOrConstructorConverter implements ToConverter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final IUtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilJdtResolver utilJdtResolver;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final UtilNamedElement utilNamedElement;
	private final ToConverter<MethodDeclaration, Member> toClassMethodOrConstructorConverter;
	private final ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter;
	private final ToConverter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter;
	private final ToConverter<SingleVariableDeclaration, Parameter> toParameterConverter;
	private final ToConverter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper;

	@Inject
	ToInterfaceMethodOrConstructorConverter(IUtilTypeInstructionSeparation utilTypeInstructionSeparation,
			UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter,
			ToConverter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter,
			ToConverter<SingleVariableDeclaration, Parameter> toParameterConverter,
			ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			@Named("ToClassMethodOrConstructorConverter") ToConverter<MethodDeclaration, Member> toClassMethodOrConstructorConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			ToConverter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper,
			StatementsFactory statementsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		this.utilJdtResolver = utilJdtResolver;
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
	public Member convert(MethodDeclaration methodDecl) {
		if (methodDecl.isConstructor()) {
			return toClassMethodOrConstructorConverter.convert(methodDecl);
		}
		InterfaceMethod result;
		IMethodBinding binding = methodDecl.resolveBinding();
		if (binding == null) {
			result = utilJdtResolver.getInterfaceMethod(methodDecl.getName().getIdentifier());
		} else {
			result = utilJdtResolver.getInterfaceMethod(binding);
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
