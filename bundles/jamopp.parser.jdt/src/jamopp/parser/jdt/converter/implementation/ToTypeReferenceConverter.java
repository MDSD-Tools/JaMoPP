package jamopp.parser.jdt.converter.implementation;

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

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilArrays;
import jamopp.parser.jdt.util.UtilLayout;

public class ToTypeReferenceConverter implements ToConverter<Type, TypeReference> {

	private final GenericsFactory genericsFactory;
	private final TypesFactory typesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilArrays jdtBindingConverterUtility;
	private final ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ToClassifierReferenceConverter toClassifierReferenceConverter;
	private final ToTypeReferencesConverter toTypeReferencesConverter;

	@Inject
	ToTypeReferenceConverter(ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, UtilArrays jdtBindingConverterUtility,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter,
			ToClassifierReferenceConverter toClassifierReferenceConverter, TypesFactory typesFactory,
			GenericsFactory genericsFactory, ToTypeReferencesConverter toTypeReferencesConverter) {
		this.genericsFactory = genericsFactory;
		this.typesFactory = typesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TypeReference convert(Type t) {
		if (t.isPrimitiveType()) {
			PrimitiveType primType = (PrimitiveType) t;
			org.emftext.language.java.types.PrimitiveType convertedType;
			if (primType.getPrimitiveTypeCode() == PrimitiveType.BOOLEAN) {
				convertedType = typesFactory.createBoolean();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.BYTE) {
				convertedType = typesFactory.createByte();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.CHAR) {
				convertedType = typesFactory.createChar();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.DOUBLE) {
				convertedType = typesFactory.createDouble();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.FLOAT) {
				convertedType = typesFactory.createFloat();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.INT) {
				convertedType = typesFactory.createInt();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.LONG) {
				convertedType = typesFactory.createLong();
			} else if (primType.getPrimitiveTypeCode() == PrimitiveType.SHORT) {
				convertedType = typesFactory.createShort();
			} else { // primType.getPrimitiveTypeCode() == PrimitiveType.VOID
				convertedType = typesFactory.createVoid();
			}
			primType.annotations().forEach(obj -> convertedType.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(convertedType, primType);
			return convertedType;
		}
		if (t.isVar()) {
			InferableType ref = typesFactory.createInferableType();
			ITypeBinding binding = t.resolveBinding();
			if (binding != null) {
				ref.getActualTargets().addAll(toTypeReferencesConverter.convert(binding));
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
			return convert(arrT.getElementType());
		}
		if (t.isSimpleType()) {
			SimpleType simT = (SimpleType) t;
			TypeReference ref;
			if (!simT.annotations().isEmpty()) {
				ClassifierReference tempRef = toClassifierReferenceConverter
						.convertToClassifierReference((SimpleName) simT.getName());
				simT.annotations().forEach(obj -> tempRef.getAnnotations()
						.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
				ref = tempRef;
			} else {
				ref = utilBaseConverter.convertToClassifierOrNamespaceClassifierReference(simT.getName());
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(ref, simT);
			return ref;
		}
		if (t.isQualifiedType()) {
			QualifiedType qualType = (QualifiedType) t;
			NamespaceClassifierReference result;
			TypeReference parentRef = convert(qualType.getQualifier());
			if (parentRef instanceof ClassifierReference) {
				result = typesFactory.createNamespaceClassifierReference();
				result.getClassifierReferences().add((ClassifierReference) parentRef);
			} else {
				// parentRef instanceof NamespaceClassifierReference
				result = (NamespaceClassifierReference) parentRef;
			}
			ClassifierReference childRef = toClassifierReferenceConverter
					.convertToClassifierReference(qualType.getName());
			qualType.annotations().forEach(obj -> childRef.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			result.getClassifierReferences().add(childRef);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, qualType);
			return result;
		}
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqT = (NameQualifiedType) t;
			NamespaceClassifierReference result;
			TypeReference parentRef = utilBaseConverter
					.convertToClassifierOrNamespaceClassifierReference(nqT.getQualifier());
			if (parentRef instanceof ClassifierReference) {
				result = typesFactory.createNamespaceClassifierReference();
				result.getClassifierReferences().add((ClassifierReference) parentRef);
			} else {
				result = (NamespaceClassifierReference) parentRef;
			}
			ClassifierReference child = toClassifierReferenceConverter.convertToClassifierReference(nqT.getName());
			nqT.annotations().forEach(obj -> child.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			result.getClassifierReferences().add(child);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, nqT);
			return result;
		}
		if (t.isParameterizedType()) {
			ParameterizedType paramT = (ParameterizedType) t;
			TypeReference ref = convert(paramT.getType());
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
	public TypeArgument convertToTypeArgument(Type t) {
		if (!t.isWildcardType()) {
			QualifiedTypeArgument result = genericsFactory.createQualifiedTypeArgument();
			result.setTypeReference(convert(t));
			convertToArrayDimensionsAndSet(t, result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, t);
			return result;
		}
		WildcardType wildType = (WildcardType) t;
		if (wildType.getBound() == null) {
			UnknownTypeArgument result = genericsFactory.createUnknownTypeArgument();
			wildType.annotations().forEach(obj -> result.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		if (wildType.isUpperBound()) {
			ExtendsTypeArgument result = genericsFactory.createExtendsTypeArgument();
			wildType.annotations().forEach(obj -> result.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			result.setExtendType(convert(wildType.getBound()));
			convertToArrayDimensionsAndSet(wildType.getBound(), result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		SuperTypeArgument result = genericsFactory.createSuperTypeArgument();
		wildType.annotations().forEach(obj -> result.getAnnotations()
				.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
		result.setSuperType(convert(wildType.getBound()));
		convertToArrayDimensionsAndSet(wildType.getBound(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

	public void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer) {
		convertToArrayDimensionsAndSet(t, arrDimContainer, 0);
	}

	public void convertToArrayDimensionsAndSet(Type t, ArrayTypeable arrDimContainer, int ignoreDimensions) {
		if (t.isArrayType()) {
			ArrayType arrT = (ArrayType) t;
			for (int i = ignoreDimensions; i < arrT.dimensions().size(); i++) {
				arrDimContainer.getArrayDimensionsBefore().add(toArrayDimensionAfterAndSetConverter
						.convertToArrayDimension((Dimension) arrT.dimensions().get(i)));
			}
		}
	}

}
