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
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToInterfaceMethodOrConstructorConverter implements ToConverter<MethodDeclaration, Member> {

	private final StatementsFactory statementsFactory;
	private final ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter;
	private final UtilJdtResolver utilJdtResolver;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToTypeParameterConverter toTypeParameterConverter;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final UtilNamedElement utilNamedElement;
	private final ToReceiverParameterConverter toReceiverParameterConverter;
	private final ToParameterConverter toParameterConverter;
	private final ToNamespaceClassifierReferenceConverter inNamespaceClassifierReferenceWrapper;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;

	@Inject
	ToInterfaceMethodOrConstructorConverter(UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToTypeReferenceConverter toTypeReferenceConverter, ToTypeParameterConverter toTypeParameterConverter,
			ToReceiverParameterConverter toReceiverParameterConverter, ToParameterConverter toParameterConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			ToNamespaceClassifierReferenceConverter inNamespaceClassifierReferenceWrapper,
			StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toClassMethodOrConstructorConverter = toClassMethodOrConstructorConverter;
		this.utilJdtResolver = utilJdtResolver;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.utilNamedElement = utilNamedElement;
		this.toReceiverParameterConverter = toReceiverParameterConverter;
		this.toParameterConverter = toParameterConverter;
		this.inNamespaceClassifierReferenceWrapper = inNamespaceClassifierReferenceWrapper;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member convert(MethodDeclaration methodDecl) {
		if (methodDecl.isConstructor()) {
			return toClassMethodOrConstructorConverter.convertToClassMethodOrConstructor(methodDecl);
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
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(methodDecl.getReturnType2(), result);
		methodDecl.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		utilNamedElement.setNameOfElement(methodDecl.getName(), result);
		if (methodDecl.getReceiverType() != null) {
			result.getParameters().add(toReceiverParameterConverter.convert(methodDecl));
		}
		methodDecl.parameters().forEach(
				obj -> result.getParameters().add(toParameterConverter.convert((SingleVariableDeclaration) obj)));
		methodDecl.thrownExceptionTypes()
				.forEach(obj -> result.getExceptions().add(inNamespaceClassifierReferenceWrapper
						.convertToNamespaceClassifierReference(toTypeReferenceConverter.convert((Type) obj))));
		if (methodDecl.getBody() != null) {
			utilTypeInstructionSeparation.addMethod(methodDecl.getBody(), result);
		} else {
			result.setStatement(statementsFactory.createEmptyStatement());
		}
		utilLayout.convertToMinimalLayoutInformation(result, methodDecl);
		return result;
	}

}
