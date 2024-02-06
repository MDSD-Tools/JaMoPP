package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ToReferenceConverterFromType
		implements Converter<Type, tools.mdsd.jamopp.model.java.references.Reference> {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	private final Converter<Name, IdentifierReference> toReferenceConverterFromName;
	private final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName;

	@Inject
	public ToReferenceConverterFromType(final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<Name, IdentifierReference> toReferenceConverterFromName,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			final ReferencesFactory referencesFactory, final UtilLayout layoutInformationConverter,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.toReferenceConverterFromSimpleName = toReferenceConverterFromSimpleName;
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.Reference convert(final Type type) {
		tools.mdsd.jamopp.model.java.references.Reference result = null;
		if (type.isNameQualifiedType()) {
			result = handleNameQualifiedType(type);
		} else if (type.isQualifiedType()) {
			result = handleQualifiedType(type);
		} else if (type.isSimpleType()) {
			result = handleSimpleType(type);
		} else if (type.isPrimitiveType()) {
			result = handlePrimitiveType(type);
		} else if (type.isArrayType()) {
			result = handleArrayType(type);
		}
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleArrayType(final Type type) {
		final ArrayType arr = (ArrayType) type;
		final tools.mdsd.jamopp.model.java.references.Reference result = convert(arr.getElementType());
		if (arr.getElementType().isPrimitiveType()) {
			final tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference primRef = (tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference) result;
			utilToArrayDimensionsAndSetConverter.convert(arr, primRef);
		} else {
			final IdentifierReference idRef = (IdentifierReference) result;
			utilToArrayDimensionsAndSetConverter.convert(arr, idRef);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handlePrimitiveType(final Type type) {
		final TypeReference typeRef = toTypeReferenceConverter.convert(type);
		final tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference temp = referencesFactory
				.createPrimitiveTypeReference();
		temp.setPrimitiveType((tools.mdsd.jamopp.model.java.types.PrimitiveType) typeRef);
		temp.getLayoutInformations().addAll(typeRef.getLayoutInformations());
		return temp;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleSimpleType(final Type type) {
		final SimpleType sType = (SimpleType) type;
		final IdentifierReference result = toReferenceConverterFromName.convert(sType.getName());
		sType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, sType);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleQualifiedType(final Type type) {
		final QualifiedType qType = (QualifiedType) type;
		final tools.mdsd.jamopp.model.java.references.Reference parent = convert(qType.getQualifier());
		final IdentifierReference child = toReferenceConverterFromSimpleName.convert(qType.getName());
		qType.annotations()
				.forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		parent.setNext(child);
		layoutInformationConverter.convertToMinimalLayoutInformation(child, qType);
		return child;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleNameQualifiedType(final Type type) {
		final NameQualifiedType nqType = (NameQualifiedType) type;
		final IdentifierReference parent = toReferenceConverterFromName.convert(nqType.getQualifier());
		final IdentifierReference child = toReferenceConverterFromSimpleName.convert(nqType.getName());
		parent.setNext(child);
		nqType.annotations()
				.forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(child, nqType);
		return child;
	}

}
