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
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationParameterList;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.annotations.SingleAnnotationParameter;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.commons.NamespaceAwareElement;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.UnknownTypeArgument;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

class UtilBaseConverter {

	private final UtilLayout layoutInformationConverter;
	private final UtilJDTResolver jdtResolverUtility;
	private final UtilJDTBindingConverter jdtBindingConverterUtility;
	private final UtilExpressionConverter expressionConverterUtility;
	private final UtilNamedElement utilNamedElement;

	private UtilTypeInstructionSeparation typeInstructionSeparationUtility;

	UtilBaseConverter(UtilLayout layoutInformationConverter, UtilJDTResolver jdtResolverUtility,
			UtilJDTBindingConverter jdtBindingConverterUtility, UtilExpressionConverter expressionConverterUtility,
			UtilNamedElement utilNamedElement) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
	}

	TypeReference convertToClassifierOrNamespaceClassifierReference(Name name) {
		if (name.isSimpleName()) {
			return convertToClassifierReference((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		NamespaceClassifierReference ref = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
		if (name.resolveBinding() == null) {
			ref.getClassifierReferences().add(convertToClassifierReference(qualifiedName.getName()));
			utilNamedElement.addNameToNameSpace(qualifiedName.getQualifier(), ref);
			return ref;
		}
		Name qualifier = qualifiedName.getQualifier();
		SimpleName simpleName = qualifiedName.getName();
		while (simpleName != null && simpleName.resolveBinding() instanceof ITypeBinding) {
			ref.getClassifierReferences().add(0, convertToClassifierReference(simpleName));
			if (qualifier == null) {
				simpleName = null;
			} else if (qualifier.isSimpleName()) {
				simpleName = (SimpleName) qualifier;
				qualifier = null;
			} else {
				simpleName = ((QualifiedName) qualifier).getName();
				qualifier = ((QualifiedName) qualifier).getQualifier();
			}
		}
		if (simpleName != null && !(simpleName.resolveBinding() instanceof ITypeBinding)) {
			utilNamedElement.addNameToNameSpace(simpleName, ref);
		}
		if (qualifier != null) {
			utilNamedElement.addNameToNameSpace(qualifier, ref);
		}
		return ref;
	}

	ClassifierReference convertToClassifierReference(SimpleName simpleName) {
		ClassifierReference ref = TypesFactory.eINSTANCE.createClassifierReference();
		ITypeBinding binding = (ITypeBinding) simpleName.resolveBinding();
		Classifier proxy;
		if (binding == null || binding.isRecovered()) {
			proxy = jdtResolverUtility.getClass(simpleName.getIdentifier());
		} else {
			proxy = jdtResolverUtility.getClassifier(binding);
		}
		proxy.setName(simpleName.getIdentifier());
		ref.setTarget(proxy);
		return ref;
	}



	org.emftext.language.java.modifiers.Modifier convertToModifier(Modifier mod) {
		org.emftext.language.java.modifiers.Modifier result = null;
		if (mod.isAbstract()) {
			result = ModifiersFactory.eINSTANCE.createAbstract();
		} else if (mod.isDefault()) {
			result = ModifiersFactory.eINSTANCE.createDefault();
		} else if (mod.isFinal()) {
			result = ModifiersFactory.eINSTANCE.createFinal();
		} else if (mod.isNative()) {
			result = ModifiersFactory.eINSTANCE.createNative();
		} else if (mod.isPrivate()) {
			result = ModifiersFactory.eINSTANCE.createPrivate();
		} else if (mod.isProtected()) {
			result = ModifiersFactory.eINSTANCE.createProtected();
		} else if (mod.isPublic()) {
			result = ModifiersFactory.eINSTANCE.createPublic();
		} else if (mod.isStatic()) {
			result = ModifiersFactory.eINSTANCE.createStatic();
		} else if (mod.isStrictfp()) {
			result = ModifiersFactory.eINSTANCE.createStrictfp();
		} else if (mod.isSynchronized()) {
			result = ModifiersFactory.eINSTANCE.createSynchronized();
		} else if (mod.isTransient()) {
			result = ModifiersFactory.eINSTANCE.createTransient();
		} else { // mod.isVolatile()
			result = ModifiersFactory.eINSTANCE.createVolatile();
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, mod);
		return result;
	}

	@SuppressWarnings("unchecked")
	AnnotationInstance convertToAnnotationInstance(Annotation annot) {
		AnnotationInstance result = AnnotationsFactory.eINSTANCE.createAnnotationInstance();
		utilNamedElement.addNameToNameSpace(annot.getTypeName(), result);
		org.emftext.language.java.classifiers.Annotation proxyClass;
		IAnnotationBinding binding = annot.resolveAnnotationBinding();
		if (binding == null) {
			proxyClass = jdtResolverUtility.getAnnotation(annot.getTypeName().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getAnnotation(binding.getAnnotationType());
		}
		result.setAnnotation(proxyClass);
		if (annot.isSingleMemberAnnotation()) {
			SingleAnnotationParameter param = AnnotationsFactory.eINSTANCE.createSingleAnnotationParameter();
			result.setParameter(param);
			SingleMemberAnnotation singleAnnot = (SingleMemberAnnotation) annot;
			typeInstructionSeparationUtility.addSingleAnnotationParameter(singleAnnot.getValue(), param);
		} else if (annot.isNormalAnnotation()) {
			AnnotationParameterList param = AnnotationsFactory.eINSTANCE.createAnnotationParameterList();
			result.setParameter(param);
			NormalAnnotation normalAnnot = (NormalAnnotation) annot;
			normalAnnot.values().forEach(obj -> {
				MemberValuePair memVal = (MemberValuePair) obj;
				AnnotationAttributeSetting attrSet = AnnotationsFactory.eINSTANCE.createAnnotationAttributeSetting();
				InterfaceMethod methodProxy;
				if (memVal.resolveMemberValuePairBinding() != null) {
					methodProxy = jdtResolverUtility
							.getInterfaceMethod(memVal.resolveMemberValuePairBinding().getMethodBinding());
				} else {
					methodProxy = jdtResolverUtility.getInterfaceMethod(memVal.getName().getIdentifier());
					if (!proxyClass.getMembers().contains(methodProxy)) {
						proxyClass.getMembers().add(methodProxy);
					}
				}
				utilNamedElement.setNameOfElement(memVal.getName(), methodProxy);
				attrSet.setAttribute(methodProxy);
				typeInstructionSeparationUtility.addAnnotationAttributeSetting(memVal.getValue(), attrSet);
				layoutInformationConverter.convertToMinimalLayoutInformation(attrSet, memVal);
				param.getSettings().add(attrSet);
			});
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, annot);
		return result;
	}

	void setTypeInstructionSeparationUtility(UtilTypeInstructionSeparation typeInstructionSeparationUtility) {
		this.typeInstructionSeparationUtility = typeInstructionSeparationUtility;
	}

}
