package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.references.IdentifierReference;
import org.eclipse.jdt.core.dom.Name;

public class ToReferenceConverterFromType implements Converter<Type, org.emftext.language.java.references.Reference> {

	private final ReferencesFactory referencesFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	
	private final Converter<Name, IdentifierReference> toReferenceConverterFromName;
	private final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName;

	@Inject
	ToReferenceConverterFromType(Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<Name, IdentifierReference> toReferenceConverterFromName,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ReferencesFactory referencesFactory, IUtilLayout layoutInformationConverter,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter, Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.toReferenceConverterFromSimpleName = toReferenceConverterFromSimpleName;
	}

	@SuppressWarnings("unchecked")
	public org.emftext.language.java.references.Reference convert(Type t) {
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqType = (NameQualifiedType) t;
			org.emftext.language.java.references.IdentifierReference parent = toReferenceConverterFromName
					.convert(nqType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromSimpleName
					.convert(nqType.getName());
			parent.setNext(child);
			nqType.annotations().forEach(
					obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(child, nqType);
			return child;
		}
		if (t.isQualifiedType()) {
			QualifiedType qType = (QualifiedType) t;
			org.emftext.language.java.references.Reference parent = convert(qType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromSimpleName
					.convert(qType.getName());
			qType.annotations().forEach(
					obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			parent.setNext(child);
			layoutInformationConverter.convertToMinimalLayoutInformation(child, qType);
			return child;
		}
		if (t.isSimpleType()) {
			SimpleType sType = (SimpleType) t;
			org.emftext.language.java.references.IdentifierReference result = toReferenceConverterFromName
					.convert(sType.getName());
			sType.annotations().forEach(
					obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, sType);
			return result;
		}
		if (t.isPrimitiveType()) {
			org.emftext.language.java.types.TypeReference typeRef = toTypeReferenceConverter.convert(t);
			org.emftext.language.java.references.PrimitiveTypeReference temp = referencesFactory
					.createPrimitiveTypeReference();
			temp.setPrimitiveType((org.emftext.language.java.types.PrimitiveType) typeRef);
			temp.getLayoutInformations().addAll(typeRef.getLayoutInformations());
			return temp;
		}
		if (t.isArrayType()) {
			ArrayType arr = (ArrayType) t;
			org.emftext.language.java.references.Reference result = convert(arr.getElementType());
			if (arr.getElementType().isPrimitiveType()) {
				org.emftext.language.java.references.PrimitiveTypeReference primRef = (org.emftext.language.java.references.PrimitiveTypeReference) result;
				utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(arr, primRef);
			} else {
				org.emftext.language.java.references.IdentifierReference idRef = (org.emftext.language.java.references.IdentifierReference) result;
				utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(arr, idRef);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		return null;
	}

}
