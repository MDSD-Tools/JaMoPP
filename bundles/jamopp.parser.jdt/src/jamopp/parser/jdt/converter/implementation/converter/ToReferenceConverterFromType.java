package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilReferenceWalker;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ReferenceConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToReferenceConverterFromType implements ReferenceConverter<Type>, ToConverter<Type, Reference> {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final ToReferenceConverterFromName toReferenceConverterFromName;

	@Inject
	ToReferenceConverterFromType(ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToReferenceConverterFromName toReferenceConverterFromName,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter, ReferencesFactory referencesFactory,
			UtilReferenceWalker utilReferenceWalker, UtilLayout layoutInformationConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilReferenceWalker = utilReferenceWalker;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	public Reference convert(Type t) {
		return utilReferenceWalker.walkUp(internalConvertToReference(t));
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.references.Reference internalConvertToReference(Type t) {
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqType = (NameQualifiedType) t;
			org.emftext.language.java.references.IdentifierReference parent = toReferenceConverterFromName
					.convertToIdentifierReference(nqType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromName
					.convert(nqType.getName());
			parent.setNext(child);
			nqType.annotations().forEach(
					obj -> child.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(child, nqType);
			return child;
		}
		if (t.isQualifiedType()) {
			QualifiedType qType = (QualifiedType) t;
			org.emftext.language.java.references.Reference parent = internalConvertToReference(qType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromName
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
					.convertToIdentifierReference(sType.getName());
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
			org.emftext.language.java.references.Reference result = internalConvertToReference(arr.getElementType());
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
