package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.List;

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

import com.google.inject.Inject;

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
	private Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToTypeReferenceConverter(Converter<Name, TypeReference> utilBaseConverter, TypesFactory typesFactory,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			UtilLayout layoutInformationConverter, UtilArrays jdtBindingConverterUtility) {
		this.typesFactory = typesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

	@Override
	public TypeReference convert(Type t) {
		if (t.isPrimitiveType()) {
			return handlePrimitiveType(t);
		} else if (t.isVar()) {
			return handleVar(t);
		} else if (t.isArrayType()) {
			return handleArrayType(t);
		} else if (t.isSimpleType()) {
			return handleSimpleType(t);
		} else if (t.isQualifiedType()) {
			return handleQualifiedType(t);
		} else if (t.isNameQualifiedType()) {
			return handleNameQualifiedType(t);
		} else if (t.isParameterizedType()) {
			return handleParameterizedType(t);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private TypeReference handlePrimitiveType(Type t) {
		PrimitiveType primType = (PrimitiveType) t;
		tools.mdsd.jamopp.model.java.types.PrimitiveType convertedType;
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

	@SuppressWarnings({ "unchecked" })
	private TypeReference handleParameterizedType(Type t) {
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
		paramT.typeArguments()
				.forEach(obj -> container.getTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		return ref;
	}

	@SuppressWarnings("unchecked")
	private TypeReference handleNameQualifiedType(Type t) {
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
		nqT.annotations()
				.forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.getClassifierReferences().add(child);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, nqT);
		return result;
	}

	@SuppressWarnings("unchecked")
	private TypeReference handleQualifiedType(Type t) {
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
		qualType.annotations()
				.forEach(obj -> childRef.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.getClassifierReferences().add(childRef);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, qualType);
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	private TypeReference handleSimpleType(Type t) {
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

	private TypeReference handleArrayType(Type t) {
		ArrayType arrT = (ArrayType) t;
		return convert(arrT.getElementType());
	}

	private TypeReference handleVar(Type t) {
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

	@Inject
	public void setTypeToTypeArgumentConverter(Converter<Type, TypeArgument> typeToTypeArgumentConverter) {
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

}
