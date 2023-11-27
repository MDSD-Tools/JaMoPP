package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;
import org.emftext.language.java.arrays.ArrayTypeable;
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

class ToTypeReferenceConverter {
	
	private final UtilLayout layoutInformationConverter;
	private final UtilJDTBindingConverter jdtBindingConverterUtility;
	private final UtilBaseConverter utilBaseConverter;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;

	public ToTypeReferenceConverter(UtilBaseConverter utilBaseConverter, ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter, UtilLayout layoutInformationConverter, UtilJDTBindingConverter jdtBindingConverterUtility, ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
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
			primType.annotations()
					.forEach(obj -> convertedType.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(convertedType, primType);
			return convertedType;
		}
		if (t.isVar()) {
			InferableType ref = TypesFactory.eINSTANCE.createInferableType();
			ITypeBinding binding = t.resolveBinding();
			if (binding != null) {
				ref.getActualTargets().addAll(jdtBindingConverterUtility.convertToTypeReferences(binding));
				if (binding.isArray()) {
					jdtBindingConverterUtility.convertToArrayDimensionsAndSet(binding, ref);
				} else if (binding.isIntersectionType() && binding.getTypeBounds()[0].isArray()) {
					jdtBindingConverterUtility.convertToArrayDimensionsAndSet(binding.getTypeBounds()[0], ref);
				}
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(ref, t);
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
				ClassifierReference tempRef = utilBaseConverter.convertToClassifierReference((SimpleName) simT.getName());
				simT.annotations()
						.forEach(obj -> tempRef.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
				ref = tempRef;
			} else {
				ref =utilBaseConverter. convertToClassifierOrNamespaceClassifierReference(simT.getName());
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(ref, simT);
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
			ClassifierReference childRef = utilBaseConverter.convertToClassifierReference(qualType.getName());
			qualType.annotations()
					.forEach(obj -> childRef.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			result.getClassifierReferences().add(childRef);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, qualType);
			return result;
		}
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqT = (NameQualifiedType) t;
			NamespaceClassifierReference result;
			TypeReference parentRef = utilBaseConverter.convertToClassifierOrNamespaceClassifierReference(nqT.getQualifier());
			if (parentRef instanceof ClassifierReference) {
				result = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
				result.getClassifierReferences().add((ClassifierReference) parentRef);
			} else {
				result = (NamespaceClassifierReference) parentRef;
			}
			ClassifierReference child = utilBaseConverter.convertToClassifierReference(nqT.getName());
			nqT.annotations().forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			result.getClassifierReferences().add(child);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, nqT);
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
			layoutInformationConverter.convertToMinimalLayoutInformation(result, t);
			return result;
		}
		WildcardType wildType = (WildcardType) t;
		if (wildType.getBound() == null) {
			UnknownTypeArgument result = GenericsFactory.eINSTANCE.createUnknownTypeArgument();
			wildType.annotations()
					.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		if (wildType.isUpperBound()) {
			ExtendsTypeArgument result = GenericsFactory.eINSTANCE.createExtendsTypeArgument();
			wildType.annotations()
					.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			result.setExtendType(convertToTypeReference(wildType.getBound()));
			convertToArrayDimensionsAndSet(wildType.getBound(), result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		SuperTypeArgument result = GenericsFactory.eINSTANCE.createSuperTypeArgument();
		wildType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
		result.setSuperType(convertToTypeReference(wildType.getBound()));
		convertToArrayDimensionsAndSet(wildType.getBound(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
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
						.add(toArrayDimensionAfterAndSetConverter.convertToArrayDimension((Dimension) arrT.dimensions().get(i)));
			}
		}
	}
	
}
