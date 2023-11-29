package jamopp.parser.jdt;

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
import org.emftext.language.java.statements.StatementsFactory;

public class ToClassMethodOrConstructorConverter {

	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToReceiverParameterConverter toReceiverParameterConverter;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeParameterConverter toTypeParameterConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToParameterConverter toParameterConverter;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final InNamespaceClassifierReferenceWrapper inNamespaceClassifierReferenceWrapper;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;

	public ToClassMethodOrConstructorConverter(UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			UtilNamedElement utilNamedElement, UtilLayout utilLayout, ToTypeReferenceConverter toTypeReferenceConverter,
			ToTypeParameterConverter toTypeParameterConverter,
			ToReceiverParameterConverter toReceiverParameterConverter, ToParameterConverter toParameterConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			UtilJdtResolver jdtResolverUtility,
			InNamespaceClassifierReferenceWrapper inNamespaceClassifierReferenceWrapper) {
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.toReceiverParameterConverter = toReceiverParameterConverter;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeParameterConverter = toTypeParameterConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toParameterConverter = toParameterConverter;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.inNamespaceClassifierReferenceWrapper = inNamespaceClassifierReferenceWrapper;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked") Member convertToClassMethodOrConstructor(MethodDeclaration methodDecl) {
		if (methodDecl.isConstructor()) {
			Constructor result;
			IMethodBinding binding = methodDecl.resolveBinding();
			if (binding == null) {
				result = jdtResolverUtility.getConstructor(methodDecl.getName().getIdentifier());
			} else {
				result = jdtResolverUtility.getConstructor(binding);
			}
			methodDecl.modifiers()
					.forEach(obj -> result.getAnnotationsAndModifiers().add(toModifierOrAnnotationInstanceConverter
							.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
			methodDecl.typeParameters().forEach(obj -> result.getTypeParameters()
					.add(toTypeParameterConverter.convertToTypeParameter((TypeParameter) obj)));
			utilNamedElement.setNameOfElement(methodDecl.getName(), result);
			if (methodDecl.getReceiverType() != null) {
				result.getParameters().add(toReceiverParameterConverter.convertToReceiverParameter(methodDecl));
			}
			methodDecl.parameters().forEach(obj -> result.getParameters()
					.add(toParameterConverter.convertToParameter((SingleVariableDeclaration) obj)));
			methodDecl.thrownExceptionTypes()
					.forEach(obj -> result.getExceptions()
							.add(inNamespaceClassifierReferenceWrapper.wrapInNamespaceClassifierReference(
									toTypeReferenceConverter.convertToTypeReference((Type) obj))));
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
		methodDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers().add(
				toModifierOrAnnotationInstanceConverter.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		methodDecl.typeParameters().forEach(obj -> result.getTypeParameters()
				.add(toTypeParameterConverter.convertToTypeParameter((TypeParameter) obj)));
		result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(methodDecl.getReturnType2()));
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(methodDecl.getReturnType2(), result);
		methodDecl.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		utilNamedElement.setNameOfElement(methodDecl.getName(), result);
		if (methodDecl.getReceiverType() != null) {
			result.getParameters().add(toReceiverParameterConverter.convertToReceiverParameter(methodDecl));
		}
		methodDecl.parameters().forEach(obj -> result.getParameters()
				.add(toParameterConverter.convertToParameter((SingleVariableDeclaration) obj)));
		methodDecl.thrownExceptionTypes()
				.forEach(obj -> result.getExceptions()
						.add(inNamespaceClassifierReferenceWrapper.wrapInNamespaceClassifierReference(
								toTypeReferenceConverter.convertToTypeReference((Type) obj))));
		if (methodDecl.getBody() != null) {
			utilTypeInstructionSeparation.addMethod(methodDecl.getBody(), result);
		} else {
			result.setStatement(StatementsFactory.eINSTANCE.createEmptyStatement());
		}
		utilLayout.convertToMinimalLayoutInformation(result, methodDecl);
		return result;
	}

}
