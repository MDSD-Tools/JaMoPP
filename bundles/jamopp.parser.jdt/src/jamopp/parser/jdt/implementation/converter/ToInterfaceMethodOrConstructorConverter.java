package jamopp.parser.jdt.implementation.converter;

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

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToInterfaceMethodOrConstructorConverter implements Converter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final IUtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final IUtilLayout utilLayout;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final IUtilJdtResolver iUtilJdtResolver;
	private final IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final IUtilNamedElement utilNamedElement;
	private final Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter;
	private final Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter;
	private final Converter<SingleVariableDeclaration, Parameter> toParameterConverter;
	private final Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper;

	@Inject
	ToInterfaceMethodOrConstructorConverter(IUtilTypeInstructionSeparation utilTypeInstructionSeparation,
			IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, IUtilJdtResolver iUtilJdtResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> toTypeParameterConverter,
			Converter<MethodDeclaration, ReceiverParameter> toReceiverParameterConverter,
			Converter<SingleVariableDeclaration, Parameter> toParameterConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			@Named("ToClassMethodOrConstructorConverter") Converter<MethodDeclaration, Member> toClassMethodOrConstructorConverter,
			IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			Converter<TypeReference, NamespaceClassifierReference> inNamespaceClassifierReferenceWrapper,
			StatementsFactory statementsFactory,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		this.iUtilJdtResolver = iUtilJdtResolver;
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
			result = iUtilJdtResolver.getInterfaceMethod(methodDecl.getName().getIdentifier());
		} else {
			result = iUtilJdtResolver.getInterfaceMethod(binding);
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
