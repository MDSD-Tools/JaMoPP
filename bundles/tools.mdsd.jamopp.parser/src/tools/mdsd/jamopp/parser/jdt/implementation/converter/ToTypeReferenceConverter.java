package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.InferableType;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToTypeReferenceConverter implements Converter<Type, TypeReference> {

	private final TypesFactory typesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilArrays jdtBindingConverterUtility;
	private final Converter<Name, TypeReference> utilBaseConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final Provider<Converter<Type, TypeArgument>> typeToTypeArgumentConverter;

	@Inject
	public ToTypeReferenceConverter(final Converter<Name, TypeReference> utilBaseConverter,
			final TypesFactory typesFactory,
			final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			final Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			final UtilLayout layoutInformationConverter, final UtilArrays jdtBindingConverterUtility,
			final Provider<Converter<Type, TypeArgument>> typeToTypeArgumentConverter) {
		this.typesFactory = typesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	@Override
	public TypeReference convert(final Type type) {
		TypeReference result = null;
		if (type.isPrimitiveType()) {
			result = handlePrimitiveType(type);
		} else if (type.isVar()) {
			result = handleVar(type);
		} else if (type.isArrayType()) {
			result = handleArrayType(type);
		} else if (type.isSimpleType()) {
			result = handleSimpleType(type);
		} else if (type.isQualifiedType()) {
			result = handleQualifiedType(type);
		} else if (type.isNameQualifiedType()) {
			result = handleNameQualifiedType(type);
		} else if (type.isParameterizedType()) {
			result = handleParameterizedType(type);
		}
		return result;
	}

	private TypeReference handlePrimitiveType(final Type type) {
		final PrimitiveType primType = (PrimitiveType) type;
		tools.mdsd.jamopp.model.java.types.PrimitiveType convertedType;
		if (primType.getPrimitiveTypeCode().equals(PrimitiveType.BOOLEAN)) {
			convertedType = typesFactory.createBoolean();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.BYTE)) {
			convertedType = typesFactory.createByte();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.CHAR)) {
			convertedType = typesFactory.createChar();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.DOUBLE)) {
			convertedType = typesFactory.createDouble();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.FLOAT)) {
			convertedType = typesFactory.createFloat();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.INT)) {
			convertedType = typesFactory.createInt();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.LONG)) {
			convertedType = typesFactory.createLong();
		} else if (primType.getPrimitiveTypeCode().equals(PrimitiveType.SHORT)) {
			convertedType = typesFactory.createShort();
		} else { // primType.getPrimitiveTypeCode() == PrimitiveType.VOID
			convertedType = typesFactory.createVoid();
		}
		((List<?>) primType.annotations()).forEach(
				obj -> convertedType.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedType, primType);
		return convertedType;
	}

	@SuppressWarnings({ "unchecked" })
	private TypeReference handleParameterizedType(final Type type) {
		final ParameterizedType paramT = (ParameterizedType) type;
		final TypeReference ref = convert(paramT.getType());
		ClassifierReference container;
		if (ref instanceof ClassifierReference) {
			container = (ClassifierReference) ref;
		} else {
			final NamespaceClassifierReference containerContainer = (NamespaceClassifierReference) ref;
			container = containerContainer.getClassifierReferences()
					.get(containerContainer.getClassifierReferences().size() - 1);
		}
		paramT.typeArguments().forEach(
				obj -> container.getTypeArguments().add(typeToTypeArgumentConverter.get().convert((Type) obj)));
		return ref;
	}

	private TypeReference handleNameQualifiedType(final Type type) {
		final NameQualifiedType nqT = (NameQualifiedType) type;
		NamespaceClassifierReference result;
		final TypeReference parentRef = utilBaseConverter.convert(nqT.getQualifier());
		if (parentRef instanceof ClassifierReference) {
			result = typesFactory.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) parentRef);
		} else {
			result = (NamespaceClassifierReference) parentRef;
		}
		final ClassifierReference child = toClassifierReferenceConverter.convert(nqT.getName());
		((List<?>) nqT.annotations())
				.forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.getClassifierReferences().add(child);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, nqT);
		return result;
	}

	private TypeReference handleQualifiedType(final Type type) {
		final QualifiedType qualType = (QualifiedType) type;
		NamespaceClassifierReference result;
		final TypeReference parentRef = convert(qualType.getQualifier());
		if (parentRef instanceof ClassifierReference) {
			result = typesFactory.createNamespaceClassifierReference();
			result.getClassifierReferences().add((ClassifierReference) parentRef);
		} else {
			// parentRef instanceof NamespaceClassifierReference
			result = (NamespaceClassifierReference) parentRef;
		}
		final ClassifierReference childRef = toClassifierReferenceConverter.convert(qualType.getName());
		((List<?>) qualType.annotations())
				.forEach(obj -> childRef.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.getClassifierReferences().add(childRef);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, qualType);
		return result;
	}

	private TypeReference handleSimpleType(final Type type) {
		final SimpleType simT = (SimpleType) type;
		TypeReference ref;
		if (simT.annotations().isEmpty()) {
			ref = utilBaseConverter.convert(simT.getName());
		} else {
			final ClassifierReference tempRef = toClassifierReferenceConverter.convert((SimpleName) simT.getName());
			((List<?>) simT.annotations()).forEach(
					obj -> tempRef.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			ref = tempRef;
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(ref, simT);
		return ref;
	}

	private TypeReference handleArrayType(final Type type) {
		final ArrayType arrT = (ArrayType) type;
		return convert(arrT.getElementType());
	}

	private TypeReference handleVar(final Type type) {
		final InferableType ref = typesFactory.createInferableType();
		final ITypeBinding binding = type.resolveBinding();
		if (binding != null) {
			ref.getActualTargets().addAll(toTypeReferencesConverter.convert(binding));
			if (binding.isArray()) {
				jdtBindingConverterUtility.convertToArrayDimensionsAndSet(binding, ref);
			} else if (binding.isIntersectionType() && binding.getTypeBounds()[0].isArray()) {
				jdtBindingConverterUtility.convertToArrayDimensionsAndSet(binding.getTypeBounds()[0], ref);
			}
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(ref, type);
		return ref;
	}

}
