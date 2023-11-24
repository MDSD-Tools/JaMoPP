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

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayTypeable;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.commons.NamespaceAwareElement;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.UnknownTypeArgument;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

class BaseConverterUtility {
	
	private static final LayoutInformationConverter LayoutInformationConverter = new LayoutInformationConverter();
	private static final JDTResolverUtility JDTResolverUtility = new JDTResolverUtility();
	private static final JDTBindingConverterUtility JDTBindingConverterUtility = new JDTBindingConverterUtility();
	
	 TypeReference convertToClassifierOrNamespaceClassifierReference(Name name) {
		if (name.isSimpleName()) {
			return convertToClassifierReference((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		NamespaceClassifierReference ref = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
		if (name.resolveBinding() == null) {
			ref.getClassifierReferences().add(convertToClassifierReference(qualifiedName.getName()));
			convertToNamespacesAndSet(qualifiedName.getQualifier(), ref);
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
			convertToNamespacesAndSet(simpleName, ref);
		}
		if (qualifier != null) {
			convertToNamespacesAndSet(qualifier, ref);
		}
		return ref;
	}

	 ClassifierReference convertToClassifierReference(SimpleName simpleName) {
		ClassifierReference ref = TypesFactory.eINSTANCE.createClassifierReference();
		ITypeBinding binding = (ITypeBinding) simpleName.resolveBinding();
		Classifier proxy;
		if (binding == null || binding.isRecovered()) {
			proxy = JDTResolverUtility.getClass(simpleName.getIdentifier());
		} else {
			proxy = JDTResolverUtility.getClassifier(binding);
		}
		proxy.setName(simpleName.getIdentifier());
		ref.setTarget(proxy);
		return ref;
	}

	 void convertToNamespacesAndSimpleNameAndSet(Name name, NamespaceAwareElement namespaceElement,
			NamedElement namedElement) {
		if (name.isSimpleName()) {
			namedElement.setName(((SimpleName) name).getIdentifier());
		} else if (name.isQualifiedName()) {
			QualifiedName qualifiedName = (QualifiedName) name;
			namedElement.setName(qualifiedName.getName().getIdentifier());
			convertToNamespacesAndSet(qualifiedName.getQualifier(), namespaceElement);
		}
	}

	 void convertToNamespacesAndSet(Name name, NamespaceAwareElement namespaceElement) {
		if (name.isSimpleName()) {
			SimpleName simpleName = (SimpleName) name;
			namespaceElement.getNamespaces().add(0, simpleName.getIdentifier());
		} else if (name.isQualifiedName()) {
			QualifiedName qualifiedName = (QualifiedName) name;
			namespaceElement.getNamespaces().add(0, qualifiedName.getName().getIdentifier());
			convertToNamespacesAndSet(qualifiedName.getQualifier(), namespaceElement);
		}
	}

	 void convertToSimpleNameOnlyAndSet(Name name, NamedElement namedElement) {
		if (name.isSimpleName()) {
			SimpleName simpleName = (SimpleName) name;
			namedElement.setName(simpleName.getIdentifier());
		} else {
			// name.isQualifiedName()
			QualifiedName qualifiedName = (QualifiedName) name;
			namedElement.setName(qualifiedName.getName().getIdentifier());
		}
	}

	@SuppressWarnings("unchecked")
	 TypeReference convertToTypeReference(Type t) {
		if (t.isPrimitiveType()) {
			PrimitiveType primType = (PrimitiveType) t;
			org.emftext.language.java.types.PrimitiveType convertedType;
			if (primType.getPrimitiveTypeCode() == PrimitiveType.BOOLEAN) {
				convertedType = TypesFactory.eINSTANCE.createBoolean();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.BYTE) {
				convertedType = TypesFactory.eINSTANCE.createByte();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.CHAR) {
				convertedType = TypesFactory.eINSTANCE.createChar();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.DOUBLE) {
				convertedType = TypesFactory.eINSTANCE.createDouble();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.FLOAT) {
				convertedType = TypesFactory.eINSTANCE.createFloat();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.INT) {
				convertedType = TypesFactory.eINSTANCE.createInt();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.LONG) {
				convertedType = TypesFactory.eINSTANCE.createLong();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.SHORT) {
				convertedType = TypesFactory.eINSTANCE.createShort();
			} else { // primType.getPrimitiveTypeCode() == PrimitiveType.VOID
				convertedType = TypesFactory.eINSTANCE.createVoid();
			}
			primType.annotations().forEach(obj -> convertedType.getAnnotations()
					.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
			LayoutInformationConverter.convertToMinimalLayoutInformation(convertedType, primType);
			return convertedType;
		}
		if (t.isVar()) {
			InferableType ref = TypesFactory.eINSTANCE.createInferableType();
			ITypeBinding binding = t.resolveBinding();
			if (binding != null) {
				ref.getActualTargets().addAll(JDTBindingConverterUtility.convertToTypeReferences(binding));
				if (binding.isArray()) {
					JDTBindingConverterUtility.convertToArrayDimensionsAndSet(binding, ref);
				} else if (binding.isIntersectionType() && binding.getTypeBounds()[0].isArray()) {
					JDTBindingConverterUtility.convertToArrayDimensionsAndSet(binding.getTypeBounds()[0], ref);
				}
			}
			LayoutInformationConverter.convertToMinimalLayoutInformation(ref, t);
			return ref;
		}
		if (t.isArrayType()) {
			ArrayType arrT = (ArrayType) t;
			return convertToTypeReference(arrT.getElementType());
		}
		if (t.isSimpleType()) {
			SimpleType simT = (SimpleType) t;
			TypeReference ref;
			if (!simT.annotations().isEmpty()) {
				ClassifierReference tempRef = convertToClassifierReference((SimpleName) simT.getName());
				simT.annotations().forEach(obj -> tempRef.getAnnotations().add(
						AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
				ref = tempRef;
			} else {
				ref = convertToClassifierOrNamespaceClassifierReference(simT.getName());
			}
			LayoutInformationConverter.convertToMinimalLayoutInformation(ref, simT);
			return ref;
		}
		if (t.isQualifiedType()) {
			QualifiedType qualType = (QualifiedType) t;
			NamespaceClassifierReference result;
			TypeReference parentRef = convertToTypeReference(qualType.getQualifier());
			if (parentRef instanceof ClassifierReference) {
				result = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
				result.getClassifierReferences().add((ClassifierReference) parentRef);
			} else {
				// parentRef instanceof NamespaceClassifierReference
				result = (NamespaceClassifierReference) parentRef;
			}
			ClassifierReference childRef = convertToClassifierReference(qualType.getName());
			qualType.annotations().forEach(obj -> childRef.getAnnotations()
					.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
			result.getClassifierReferences().add(childRef);
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, qualType);
			return result;
		}
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqT = (NameQualifiedType) t;
			NamespaceClassifierReference result;
			TypeReference parentRef = convertToClassifierOrNamespaceClassifierReference(nqT.getQualifier());
			if (parentRef instanceof ClassifierReference) {
				result = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
				result.getClassifierReferences().add((ClassifierReference) parentRef);
			} else {
				result = (NamespaceClassifierReference) parentRef;
			}
			ClassifierReference child = convertToClassifierReference(nqT.getName());
			nqT.annotations().forEach(obj -> child.getAnnotations()
					.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
			result.getClassifierReferences().add(child);
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, nqT);
			return result;
		}
		if (t.isParameterizedType()) {
			ParameterizedType paramT = (ParameterizedType) t;
			TypeReference ref = convertToTypeReference(paramT.getType());
			ClassifierReference container;
			if (ref instanceof ClassifierReference) {
				container = (ClassifierReference) ref;
			} else {
				NamespaceClassifierReference containerContainer = (NamespaceClassifierReference) ref;
				container = containerContainer.getClassifierReferences()
						.get(containerContainer.getClassifierReferences().size() - 1);
			}
			paramT.typeArguments().forEach(obj -> container.getTypeArguments().add(convertToTypeArgument((Type) obj)));
			return ref;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	 TypeArgument convertToTypeArgument(Type t) {
		if (!t.isWildcardType()) {
			QualifiedTypeArgument result = GenericsFactory.eINSTANCE.createQualifiedTypeArgument();
			result.setTypeReference(convertToTypeReference(t));
			convertToArrayDimensionsAndSet(t, result);
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, t);
			return result;
		}
		WildcardType wildType = (WildcardType) t;
		if (wildType.getBound() == null) {
			UnknownTypeArgument result = GenericsFactory.eINSTANCE.createUnknownTypeArgument();
			wildType.annotations().forEach(obj -> result.getAnnotations()
					.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		if (wildType.isUpperBound()) {
			ExtendsTypeArgument result = GenericsFactory.eINSTANCE.createExtendsTypeArgument();
			wildType.annotations().forEach(obj -> result.getAnnotations()
					.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
			result.setExtendType(convertToTypeReference(wildType.getBound()));
			convertToArrayDimensionsAndSet(wildType.getBound(), result);
			LayoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		SuperTypeArgument result = GenericsFactory.eINSTANCE.createSuperTypeArgument();
		wildType.annotations().forEach(obj -> result.getAnnotations()
				.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) obj)));
		result.setSuperType(convertToTypeReference(wildType.getBound()));
		convertToArrayDimensionsAndSet(wildType.getBound(), result);
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

	 void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer) {
		convertToArrayDimensionsAndSet(t, arrDimContainer, 0);
	}

	 void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer, int ignoreDimensions) {
		if (t.isArrayType()) {
			ArrayType arrT = (ArrayType) t;
			for (int i = ignoreDimensions; i < arrT.dimensions().size(); i++) {
				arrDimContainer.getArrayDimensionsBefore()
						.add(convertToArrayDimension((Dimension) arrT.dimensions().get(i)));
			}
		}
	}

	 void convertToArrayDimensionAfterAndSet(Dimension dim, ArrayTypeable arrDimContainer) {
		arrDimContainer.getArrayDimensionsAfter().add(convertToArrayDimension(dim));
	}

	@SuppressWarnings("unchecked")
	private  ArrayDimension convertToArrayDimension(Dimension dim) {
		ArrayDimension result = ArraysFactory.eINSTANCE.createArrayDimension();
		dim.annotations().forEach(annot -> result.getAnnotations()
				.add(AnnotationInstanceOrModifierConverterUtility.convertToAnnotationInstance((Annotation) annot)));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, dim);
		return result;
	}
}
