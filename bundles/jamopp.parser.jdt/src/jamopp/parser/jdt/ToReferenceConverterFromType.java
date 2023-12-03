package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;

class ToReferenceConverterFromType {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ReferenceWalker referenceWalker;
	private final ToReferenceConverterFromName toReferenceConverterFromName;

	@Inject
	ToReferenceConverterFromType(ToTypeReferenceConverter toTypeReferenceConverter,
			ToReferenceConverterFromName toReferenceConverterFromName,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter, ReferencesFactory referencesFactory,
			ReferenceWalker referenceWalker, UtilLayout layoutInformationConverter) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.referenceWalker = referenceWalker;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
	}

	org.emftext.language.java.references.Reference convertToReference(Type t) {
		return referenceWalker.walkUp(internalConvertToReference(t));
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.references.Reference internalConvertToReference(Type t) {
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqType = (NameQualifiedType) t;
			org.emftext.language.java.references.IdentifierReference parent = toReferenceConverterFromName
					.convertToIdentifierReference(nqType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromName
					.convertToIdentifierReference(nqType.getName());
			parent.setNext(child);
			nqType.annotations().forEach(obj -> child.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(child, nqType);
			return child;
		}
		if (t.isQualifiedType()) {
			QualifiedType qType = (QualifiedType) t;
			org.emftext.language.java.references.Reference parent = internalConvertToReference(qType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromName
					.convertToIdentifierReference(qType.getName());
			qType.annotations().forEach(obj -> child.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			parent.setNext(child);
			layoutInformationConverter.convertToMinimalLayoutInformation(child, qType);
			return child;
		}
		if (t.isSimpleType()) {
			SimpleType sType = (SimpleType) t;
			org.emftext.language.java.references.IdentifierReference result = toReferenceConverterFromName
					.convertToIdentifierReference(sType.getName());
			sType.annotations().forEach(obj -> result.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
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
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr, primRef);
			} else {
				org.emftext.language.java.references.IdentifierReference idRef = (org.emftext.language.java.references.IdentifierReference) result;
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr, idRef);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		return null;
	}

}
