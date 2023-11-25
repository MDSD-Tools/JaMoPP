/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.parameters.VariableLengthParameter;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

class ClassifierConverterUtility {

	private final TypeInstructionSeparationUtility TypeInstructionSeparationUtility;
	private final LayoutInformationConverter LayoutInformationConverter;
	private final JDTResolverUtility JDTResolverUtility;
	private final ExpressionConverterUtility ExpressionConverterUtility;
	private final BaseConverterUtility BaseConverterUtility;
	private final AnnotationInstanceOrModifierConverterUtility AnnotationInstanceOrModifierConverterUtility;
	
	public ClassifierConverterUtility(TypeInstructionSeparationUtility typeInstructionSeparationUtility, LayoutInformationConverter layoutInformationConverter, JDTResolverUtility jdtResolverUtility, ExpressionConverterUtility expressionConverterUtility, BaseConverterUtility baseConverterUtility, AnnotationInstanceOrModifierConverterUtility annotationInstanceOrModifierConverterUtility) {
		this.TypeInstructionSeparationUtility = typeInstructionSeparationUtility;
		this.LayoutInformationConverter = layoutInformationConverter;
		this.JDTResolverUtility = jdtResolverUtility;
		this.ExpressionConverterUtility = expressionConverterUtility;
		this.BaseConverterUtility = baseConverterUtility;
		this.AnnotationInstanceOrModifierConverterUtility = annotationInstanceOrModifierConverterUtility;
	}

	@SuppressWarnings("unchecked")
	ConcreteClassifier convertToConcreteClassifier(AbstractTypeDeclaration typeDecl) {
		ConcreteClassifier result = null;
		if (typeDecl.getNodeType() == ASTNode.TYPE_DECLARATION) {
			result = convertToClassOrInterface((TypeDeclaration) typeDecl);
		} else if (typeDecl.getNodeType() == ASTNode.ANNOTATION_TYPE_DECLARATION) {
			result = JDTResolverUtility.getAnnotation(typeDecl.resolveBinding());
			ConcreteClassifier fR = result;
			typeDecl.bodyDeclarations()
					.forEach(obj -> fR.getMembers().add(convertToInterfaceMember((BodyDeclaration) obj)));
		} else {
			// typeDecl.getNodeType() == ASTNode.ENUM_DECLARATION
			result = convertToEnum((EnumDeclaration) typeDecl);
		}
		ConcreteClassifier finalResult = result;
		typeDecl.modifiers().forEach(
				obj -> finalResult.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
						.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(typeDecl.getName(), result);
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, typeDecl);
		return result;
	}

	@SuppressWarnings("unchecked")
	private ConcreteClassifier convertToClassOrInterface(TypeDeclaration typeDecl) {
		if (typeDecl.isInterface()) {
			Interface interfaceObj = JDTResolverUtility.getInterface(typeDecl.resolveBinding());
			typeDecl.typeParameters()
					.forEach(obj -> interfaceObj.getTypeParameters().add(convertToTypeParameter((TypeParameter) obj)));
			typeDecl.superInterfaceTypes().forEach(
					obj -> interfaceObj.getExtends().add(BaseConverterUtility.convertToTypeReference((Type) obj)));
			typeDecl.bodyDeclarations()
					.forEach(obj -> interfaceObj.getMembers().add(convertToInterfaceMember((BodyDeclaration) obj)));
			return interfaceObj;
		}
		org.emftext.language.java.classifiers.Class classObj = JDTResolverUtility.getClass(typeDecl.resolveBinding());
		typeDecl.typeParameters()
				.forEach(obj -> classObj.getTypeParameters().add(convertToTypeParameter((TypeParameter) obj)));
		if (typeDecl.getSuperclassType() != null) {
			classObj.setExtends(BaseConverterUtility.convertToTypeReference(typeDecl.getSuperclassType()));
		}
		typeDecl.superInterfaceTypes()
				.forEach(obj -> classObj.getImplements().add(BaseConverterUtility.convertToTypeReference((Type) obj)));
		typeDecl.bodyDeclarations()
				.forEach(obj -> classObj.getMembers().add(convertToClassMember((BodyDeclaration) obj)));
		return classObj;
	}

	@SuppressWarnings("unchecked")
	private Enumeration convertToEnum(EnumDeclaration enumDecl) {
		Enumeration result = JDTResolverUtility.getEnumeration(enumDecl.resolveBinding());
		enumDecl.superInterfaceTypes()
				.forEach(obj -> result.getImplements().add(BaseConverterUtility.convertToTypeReference((Type) obj)));
		enumDecl.enumConstants()
				.forEach(obj -> result.getConstants().add(convertToEnumConstant((EnumConstantDeclaration) obj)));
		enumDecl.bodyDeclarations()
				.forEach(obj -> result.getMembers().add(convertToClassMember((BodyDeclaration) obj)));
		return result;
	}

	private org.emftext.language.java.members.Member convertToInterfaceMember(BodyDeclaration body) {
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return convertToInterfaceMethodOrConstructor((MethodDeclaration) body);
		}
		return convertToClassMember(body);
	}

	private Member convertToClassMember(BodyDeclaration body) {
		if (body instanceof AbstractTypeDeclaration) {
			return convertToConcreteClassifier((AbstractTypeDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.INITIALIZER) {
			return convertToBlock((Initializer) body);
		}
		if (body.getNodeType() == ASTNode.FIELD_DECLARATION) {
			return convertToField((FieldDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return convertToClassMethodOrConstructor((MethodDeclaration) body);
		}
		if (body.getNodeType() == ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION) {
			return convertToInterfaceMethod((AnnotationTypeMemberDeclaration) body);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Block convertToBlock(Initializer init) {
		Block result = StatementsFactory.eINSTANCE.createBlock();
		result.setName("");
		TypeInstructionSeparationUtility.addInitializer(init.getBody(), result);
		init.modifiers().forEach(obj -> result.getModifiers()
				.add(AnnotationInstanceOrModifierConverterUtility.convertToModifier((Modifier) obj)));
		return result;
	}

	@SuppressWarnings("unchecked")
	private Field convertToField(FieldDeclaration fieldDecl) {
		VariableDeclarationFragment firstFragment = (VariableDeclarationFragment) fieldDecl.fragments().get(0);
		Field result;
		IVariableBinding binding = firstFragment.resolveBinding();
		if (binding != null) {
			result = JDTResolverUtility.getField(binding);
		} else {
			result = JDTResolverUtility.getField(firstFragment.getName().getIdentifier());
		}
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(firstFragment.getName(), result);
		fieldDecl.modifiers()
				.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
						.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		result.setTypeReference(BaseConverterUtility.convertToTypeReference(fieldDecl.getType()));
		BaseConverterUtility.convertToArrayDimensionsAndSet(fieldDecl.getType(), result);
		firstFragment.extraDimensions()
				.forEach(obj -> BaseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (firstFragment.getInitializer() != null) {
			TypeInstructionSeparationUtility.addField(firstFragment.getInitializer(), result);
		}
		for (int index = 1; index < fieldDecl.fragments().size(); index++) {
			result.getAdditionalFields()
					.add(convertToAdditionalField((VariableDeclarationFragment) fieldDecl.fragments().get(index)));
		}
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, fieldDecl);
		return result;
	}

	@SuppressWarnings("unchecked")
	private AdditionalField convertToAdditionalField(VariableDeclarationFragment frag) {
		AdditionalField result;
		IVariableBinding binding = frag.resolveBinding();
		if (binding != null) {
			result = JDTResolverUtility.getAdditionalField(binding);
		} else {
			result = JDTResolverUtility.getAdditionalField(frag.getName().getIdentifier());
		}
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(frag.getName(), result);
		frag.extraDimensions()
				.forEach(obj -> BaseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			TypeInstructionSeparationUtility.addAdditionalField(frag.getInitializer(), result);
		}
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, frag);
		return result;
	}

	@SuppressWarnings("unchecked")
	private InterfaceMethod convertToInterfaceMethod(AnnotationTypeMemberDeclaration annDecl) {
		IMethodBinding binding = annDecl.resolveBinding();
		InterfaceMethod result;
		if (binding != null) {
			result = JDTResolverUtility.getInterfaceMethod(binding);
		} else {
			result = JDTResolverUtility.getInterfaceMethod(annDecl.getName().getIdentifier());
		}
		annDecl.modifiers()
				.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
						.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		result.setTypeReference(BaseConverterUtility.convertToTypeReference(annDecl.getType()));
		BaseConverterUtility.convertToArrayDimensionsAndSet(annDecl.getType(), result);
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(annDecl.getName(), result);
		if (annDecl.getDefault() != null) {
			TypeInstructionSeparationUtility.addAnnotationMethod(annDecl.getDefault(), result);
		}
		result.setStatement(StatementsFactory.eINSTANCE.createEmptyStatement());
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, annDecl);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Member convertToInterfaceMethodOrConstructor(MethodDeclaration methodDecl) {
		if (methodDecl.isConstructor()) {
			return convertToClassMethodOrConstructor(methodDecl);
		}
		InterfaceMethod result;
		IMethodBinding binding = methodDecl.resolveBinding();
		if (binding == null) {
			result = JDTResolverUtility.getInterfaceMethod(methodDecl.getName().getIdentifier());
		} else {
			result = JDTResolverUtility.getInterfaceMethod(binding);
		}
		methodDecl.modifiers()
				.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
						.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		methodDecl.typeParameters()
				.forEach(obj -> result.getTypeParameters().add(convertToTypeParameter((TypeParameter) obj)));
		result.setTypeReference(BaseConverterUtility.convertToTypeReference(methodDecl.getReturnType2()));
		BaseConverterUtility.convertToArrayDimensionsAndSet(methodDecl.getReturnType2(), result);
		methodDecl.extraDimensions()
				.forEach(obj -> BaseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(methodDecl.getName(), result);
		if (methodDecl.getReceiverType() != null) {
			result.getParameters().add(convertToReceiverParameter(methodDecl));
		}
		methodDecl.parameters()
				.forEach(obj -> result.getParameters().add(convertToParameter((SingleVariableDeclaration) obj)));
		methodDecl.thrownExceptionTypes().forEach(obj -> result.getExceptions()
				.add(wrapInNamespaceClassifierReference(BaseConverterUtility.convertToTypeReference((Type) obj))));
		if (methodDecl.getBody() != null) {
			TypeInstructionSeparationUtility.addMethod(methodDecl.getBody(), result);
		} else {
			result.setStatement(StatementsFactory.eINSTANCE.createEmptyStatement());
		}
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, methodDecl);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Member convertToClassMethodOrConstructor(MethodDeclaration methodDecl) {
		if (methodDecl.isConstructor()) {
			Constructor result;
			IMethodBinding binding = methodDecl.resolveBinding();
			if (binding == null) {
				result = JDTResolverUtility.getConstructor(methodDecl.getName().getIdentifier());
			} else {
				result = JDTResolverUtility.getConstructor(binding);
			}
			methodDecl.modifiers()
					.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
							.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
			methodDecl.typeParameters()
					.forEach(obj -> result.getTypeParameters().add(convertToTypeParameter((TypeParameter) obj)));
			BaseConverterUtility.convertToSimpleNameOnlyAndSet(methodDecl.getName(), result);
			if (methodDecl.getReceiverType() != null) {
				result.getParameters().add(convertToReceiverParameter(methodDecl));
			}
			methodDecl.parameters()
					.forEach(obj -> result.getParameters().add(convertToParameter((SingleVariableDeclaration) obj)));
			methodDecl.thrownExceptionTypes().forEach(obj -> result.getExceptions()
					.add(wrapInNamespaceClassifierReference(BaseConverterUtility.convertToTypeReference((Type) obj))));
			TypeInstructionSeparationUtility.addConstructor(methodDecl.getBody(), result);
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, methodDecl);
			return result;
		}
		ClassMethod result;
		IMethodBinding binding = methodDecl.resolveBinding();
		if (binding != null) {
			result = JDTResolverUtility.getClassMethod(binding);
		} else {
			result = JDTResolverUtility.getClassMethod(methodDecl.getName().getIdentifier());
		}
		methodDecl.modifiers()
				.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
						.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		methodDecl.typeParameters()
				.forEach(obj -> result.getTypeParameters().add(convertToTypeParameter((TypeParameter) obj)));
		result.setTypeReference(BaseConverterUtility.convertToTypeReference(methodDecl.getReturnType2()));
		BaseConverterUtility.convertToArrayDimensionsAndSet(methodDecl.getReturnType2(), result);
		methodDecl.extraDimensions()
				.forEach(obj -> BaseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(methodDecl.getName(), result);
		if (methodDecl.getReceiverType() != null) {
			result.getParameters().add(convertToReceiverParameter(methodDecl));
		}
		methodDecl.parameters()
				.forEach(obj -> result.getParameters().add(convertToParameter((SingleVariableDeclaration) obj)));
		methodDecl.thrownExceptionTypes().forEach(obj -> result.getExceptions()
				.add(wrapInNamespaceClassifierReference(BaseConverterUtility.convertToTypeReference((Type) obj))));
		if (methodDecl.getBody() != null) {
			TypeInstructionSeparationUtility.addMethod(methodDecl.getBody(), result);
		} else {
			result.setStatement(StatementsFactory.eINSTANCE.createEmptyStatement());
		}
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, methodDecl);
		return result;
	}

	private NamespaceClassifierReference wrapInNamespaceClassifierReference(TypeReference ref) {
		if (ref instanceof NamespaceClassifierReference) {
			return (NamespaceClassifierReference) ref;
		}
		if (ref instanceof ClassifierReference) {
			NamespaceClassifierReference result = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) ref);
			return result;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private EnumConstant convertToEnumConstant(EnumConstantDeclaration enDecl) {
		EnumConstant result;
		IVariableBinding binding = enDecl.resolveVariable();
		if (binding == null) {
			result = JDTResolverUtility.getEnumConstant(enDecl.getName().getIdentifier());
		} else {
			result = JDTResolverUtility.getEnumConstant(binding);
		}
		enDecl.modifiers().forEach(obj -> result.getAnnotations()
				.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(enDecl.getName(), result);
		enDecl.arguments().forEach(
				obj -> result.getArguments().add(ExpressionConverterUtility.convertToExpression((Expression) obj)));
		if (enDecl.getAnonymousClassDeclaration() != null) {
			result.setAnonymousClass(convertToAnonymousClass(enDecl.getAnonymousClassDeclaration()));
		}
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, enDecl);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.generics.TypeParameter convertToTypeParameter(TypeParameter param) {
		org.emftext.language.java.generics.TypeParameter result = JDTResolverUtility
				.getTypeParameter(param.resolveBinding());
		param.modifiers().forEach(obj -> result.getAnnotations()
				.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(param.getName(), result);
		param.typeBounds()
				.forEach(obj -> result.getExtendTypes().add(BaseConverterUtility.convertToTypeReference((Type) obj)));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, param);
		return result;
	}

	@SuppressWarnings("unchecked")
	AnonymousClass convertToAnonymousClass(AnonymousClassDeclaration anon) {
		ITypeBinding binding = anon.resolveBinding();
		AnonymousClass result;
		if (binding != null) {
			result = JDTResolverUtility.getAnonymousClass(binding);
		} else {
			result = JDTResolverUtility.getAnonymousClass("" + anon.hashCode());
		}
		anon.bodyDeclarations().forEach(obj -> result.getMembers().add(convertToClassMember((BodyDeclaration) obj)));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, anon);
		return result;
	}

	private ReceiverParameter convertToReceiverParameter(MethodDeclaration methodDecl) {
		ReceiverParameter result = ParametersFactory.eINSTANCE.createReceiverParameter();
		result.setName("");
		result.setTypeReference(BaseConverterUtility.convertToTypeReference(methodDecl.getReceiverType()));
		if (methodDecl.getReceiverQualifier() != null) {
			result.setOuterTypeReference(
					BaseConverterUtility.convertToClassifierReference(methodDecl.getReceiverQualifier()));
		}
		result.setThisReference(LiteralsFactory.eINSTANCE.createThis());
		return result;
	}

	@SuppressWarnings("unchecked")
	private Parameter convertToParameter(SingleVariableDeclaration decl) {
		if (decl.isVarargs()) {
			VariableLengthParameter result = JDTResolverUtility.getVariableLengthParameter(decl.resolveBinding());
			decl.modifiers()
					.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
							.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
			result.setTypeReference(BaseConverterUtility.convertToTypeReference(decl.getType()));
			BaseConverterUtility.convertToArrayDimensionsAndSet(decl.getType(), result);
			BaseConverterUtility.convertToSimpleNameOnlyAndSet(decl.getName(), result);
			decl.extraDimensions()
					.forEach(obj -> BaseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
			decl.varargsAnnotations().forEach(obj -> result.getAnnotations()
					.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, decl);
			return result;
		}
		return convertToOrdinaryParameter(decl);
	}

	@SuppressWarnings("unchecked")
	OrdinaryParameter convertToOrdinaryParameter(SingleVariableDeclaration decl) {
		OrdinaryParameter result = JDTResolverUtility.getOrdinaryParameter(decl.resolveBinding());
		decl.modifiers()
				.forEach(obj -> result.getAnnotationsAndModifiers().add(AnnotationInstanceOrModifierConverterUtility
						.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		result.setTypeReference(BaseConverterUtility.convertToTypeReference(decl.getType()));
		BaseConverterUtility.convertToArrayDimensionsAndSet(decl.getType(), result);
		BaseConverterUtility.convertToSimpleNameOnlyAndSet(decl.getName(), result);
		decl.extraDimensions()
				.forEach(obj -> BaseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, decl);
		return result;
	}
}
