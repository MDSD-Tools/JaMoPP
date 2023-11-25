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
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationParameterList;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.annotations.SingleAnnotationParameter;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.ModifiersFactory;

class AnnotationInstanceOrModifierConverterUtility {

	private TypeInstructionSeparationUtility typeInstructionSeparationUtility;
	private final LayoutInformationConverter layoutInformationConverter;
	private final JDTResolverUtility jdtResolverUtility;
	private final ExpressionConverterUtility expressionConverterUtility;
	private final BaseConverterUtility baseConverterUtility;

	AnnotationInstanceOrModifierConverterUtility(LayoutInformationConverter layoutInformationConverter,
			JDTResolverUtility jdtResolverUtility, ExpressionConverterUtility expressionConverterUtility,
			BaseConverterUtility baseConverterUtility) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.baseConverterUtility = baseConverterUtility;
	}

	AnnotationInstanceOrModifier converToModifierOrAnnotationInstance(IExtendedModifier mod) {
		if (mod.isModifier()) {
			return convertToModifier((Modifier) mod);
		}
		return convertToAnnotationInstance((Annotation) mod);
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
		baseConverterUtility.convertToNamespacesAndSet(annot.getTypeName(), result);
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
				baseConverterUtility.convertToSimpleNameOnlyAndSet(memVal.getName(), methodProxy);
				attrSet.setAttribute(methodProxy);
				typeInstructionSeparationUtility.addAnnotationAttributeSetting(memVal.getValue(), attrSet);
				layoutInformationConverter.convertToMinimalLayoutInformation(attrSet, memVal);
				param.getSettings().add(attrSet);
			});
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, annot);
		return result;
	}

	AnnotationValue convertToAnnotationValue(Expression expr) {
		if (expr instanceof Annotation) {
			return convertToAnnotationInstance((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			return convertToArrayInitializer((ArrayInitializer) expr);
		}
		return (AssignmentExpressionChild) expressionConverterUtility.convertToExpression(expr);
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.arrays.ArrayInitializer convertToArrayInitializer(ArrayInitializer arr) {
		org.emftext.language.java.arrays.ArrayInitializer result = ArraysFactory.eINSTANCE.createArrayInitializer();
		arr.expressions().forEach(obj -> {
			org.emftext.language.java.arrays.ArrayInitializationValue value = null;
			Expression expr = (Expression) obj;
			if (expr instanceof ArrayInitializer) {
				value = convertToArrayInitializer((ArrayInitializer) expr);
			} else if (expr instanceof Annotation) {
				value = convertToAnnotationInstance((Annotation) expr);
			} else {
				value = expressionConverterUtility.convertToExpression(expr);
			}
			result.getInitialValues().add(value);
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	void setTypeInstructionSeparationUtility(TypeInstructionSeparationUtility typeInstructionSeparationUtility) {
		this.typeInstructionSeparationUtility = typeInstructionSeparationUtility;
	}
}
