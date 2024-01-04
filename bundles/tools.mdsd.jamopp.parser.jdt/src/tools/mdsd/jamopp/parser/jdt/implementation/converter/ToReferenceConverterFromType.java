package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

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
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

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
	ToReferenceConverterFromType(Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<Name, IdentifierReference> toReferenceConverterFromName,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ReferencesFactory referencesFactory, UtilLayout layoutInformationConverter,
			ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.toReferenceConverterFromSimpleName = toReferenceConverterFromSimpleName;
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.Reference convert(Type t) {
		if (t.isNameQualifiedType()) {
			return handleNameQualifiedType(t);
		} else if (t.isQualifiedType()) {
			return handleQualifiedType(t);
		} else if (t.isSimpleType()) {
			return handleSimpleType(t);
		} else if (t.isPrimitiveType()) {
			return handlePrimitiveType(t);
		} else if (t.isArrayType()) {
			return handleArrayType(t);
		}
		return null;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleArrayType(Type t) {
		ArrayType arr = (ArrayType) t;
		tools.mdsd.jamopp.model.java.references.Reference result = convert(arr.getElementType());
		if (arr.getElementType().isPrimitiveType()) {
			tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference primRef = (tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference) result;
			utilToArrayDimensionsAndSetConverter.convert(arr, primRef);
		} else {
			tools.mdsd.jamopp.model.java.references.IdentifierReference idRef = (tools.mdsd.jamopp.model.java.references.IdentifierReference) result;
			utilToArrayDimensionsAndSetConverter.convert(arr, idRef);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handlePrimitiveType(Type t) {
		tools.mdsd.jamopp.model.java.types.TypeReference typeRef = toTypeReferenceConverter.convert(t);
		tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference temp = referencesFactory
				.createPrimitiveTypeReference();
		temp.setPrimitiveType((tools.mdsd.jamopp.model.java.types.PrimitiveType) typeRef);
		temp.getLayoutInformations().addAll(typeRef.getLayoutInformations());
		return temp;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleSimpleType(Type t) {
		SimpleType sType = (SimpleType) t;
		tools.mdsd.jamopp.model.java.references.IdentifierReference result = toReferenceConverterFromName
				.convert(sType.getName());
		sType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, sType);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleQualifiedType(Type t) {
		QualifiedType qType = (QualifiedType) t;
		tools.mdsd.jamopp.model.java.references.Reference parent = convert(qType.getQualifier());
		tools.mdsd.jamopp.model.java.references.IdentifierReference child = toReferenceConverterFromSimpleName
				.convert(qType.getName());
		qType.annotations()
				.forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		parent.setNext(child);
		layoutInformationConverter.convertToMinimalLayoutInformation(child, qType);
		return child;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleNameQualifiedType(Type t) {
		NameQualifiedType nqType = (NameQualifiedType) t;
		tools.mdsd.jamopp.model.java.references.IdentifierReference parent = toReferenceConverterFromName
				.convert(nqType.getQualifier());
		tools.mdsd.jamopp.model.java.references.IdentifierReference child = toReferenceConverterFromSimpleName
				.convert(nqType.getName());
		parent.setNext(child);
		nqType.annotations()
				.forEach(obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(child, nqType);
		return child;
	}

}
