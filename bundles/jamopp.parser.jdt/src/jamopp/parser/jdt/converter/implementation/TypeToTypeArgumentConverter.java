package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.UnknownTypeArgument;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class TypeToTypeArgumentConverter implements ToConverter<Type, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	public TypeToTypeArgumentConverter(ToConverter<Type, TypeReference> toTypeReferenceConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			UtilLayout layoutInformationConverter, GenericsFactory genericsFactory) {
		this.genericsFactory = genericsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	public TypeArgument convert(Type t) {
		if (!t.isWildcardType()) {
			QualifiedTypeArgument result = genericsFactory.createQualifiedTypeArgument();
			result.setTypeReference(toTypeReferenceConverter.convert(t));
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(t, result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, t);
			return result;
		}
		WildcardType wildType = (WildcardType) t;
		if (wildType.getBound() == null) {
			UnknownTypeArgument result = genericsFactory.createUnknownTypeArgument();
			wildType.annotations().forEach(
					obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		if (wildType.isUpperBound()) {
			ExtendsTypeArgument result = genericsFactory.createExtendsTypeArgument();
			wildType.annotations().forEach(
					obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			result.setExtendType(toTypeReferenceConverter.convert(wildType.getBound()));
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(wildType.getBound(), result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
			return result;
		}
		SuperTypeArgument result = genericsFactory.createSuperTypeArgument();
		wildType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.setSuperType(toTypeReferenceConverter.convert(wildType.getBound()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(wildType.getBound(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

}
