package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
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

	private final TypesFactory typesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilArrays jdtBindingConverterUtility;
	private final ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ToClassifierReferenceConverter toClassifierReferenceConverter;
	private final ToTypeReferencesConverter toTypeReferencesConverter;
	private final TypeToTypeArgumentConverter typeToTypeArgumentConverter;

	@Inject
	ToTypeReferenceConverter(ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter,
			TypesFactory typesFactory, TypeToTypeArgumentConverter typeToTypeArgumentConverter,
			ToTypeReferencesConverter toTypeReferencesConverter,
			ToClassifierReferenceConverter toClassifierReferenceConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter, UtilLayout layoutInformationConverter,
			UtilArrays jdtBindingConverterUtility) {
		this.typesFactory = typesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
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
			primType.annotations().forEach(
					obj -> convertedType.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
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
				ClassifierReference tempRef = toClassifierReferenceConverter.convert((SimpleName) simT.getName());
				simT.annotations().forEach(
						obj -> tempRef.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
				ref = tempRef;
			} else {
				ref = utilBaseConverter.convert(simT.getName());
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
			ClassifierReference childRef = toClassifierReferenceConverter.convert(qualType.getName());
			qualType.annotations().forEach(
					obj -> childRef.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			result.getClassifierReferences().add(childRef);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, qualType);
			return result;
		}
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqT = (NameQualifiedType) t;
			NamespaceClassifierReference result;
			TypeReference parentRef = utilBaseConverter.convert(nqT.getQualifier());
			if (parentRef instanceof ClassifierReference) {
				result = typesFactory.createNamespaceClassifierReference();
				result.getClassifierReferences().add((ClassifierReference) parentRef);
			} else {
				result = (NamespaceClassifierReference) parentRef;
			}
			ClassifierReference child = toClassifierReferenceConverter.convert(nqT.getName());
			nqT.annotations().forEach(
					obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
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
			paramT.typeArguments().forEach(obj -> container.getTypeArguments()
					.add(typeToTypeArgumentConverter.convertToTypeArgument((Type) obj)));
			return ref;
		}
		return null;
	}

}
