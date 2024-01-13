package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.generics.ExtendsTypeArgument;
import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.QualifiedTypeArgument;
import tools.mdsd.jamopp.model.java.generics.SuperTypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.UnknownTypeArgument;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class TypeToTypeArgumentConverter implements Converter<Type, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	public TypeToTypeArgumentConverter(Converter<Type, TypeReference> toTypeReferenceConverter,
			ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			UtilLayout layoutInformationConverter, GenericsFactory genericsFactory) {
		this.genericsFactory = genericsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeArgument convert(Type type) {
		TypeArgument result;
		if (type.isWildcardType()) {
			WildcardType wildType = (WildcardType) type;
			if (wildType.getBound() == null) {
				result = handleNoBound(wildType);
			} else if (wildType.isUpperBound()) {
				result = handleUpperBound(wildType);
			} else {
				SuperTypeArgument newResult = genericsFactory.createSuperTypeArgument();
				wildType.annotations().forEach(
						obj -> newResult.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
				newResult.setSuperType(toTypeReferenceConverter.convert(wildType.getBound()));
				utilToArrayDimensionsAndSetConverter.convert(wildType.getBound(), newResult);
				layoutInformationConverter.convertToMinimalLayoutInformation(newResult, wildType);
				result = newResult;
			}
		} else {
			result = handleIsNotWildcard(type);
		}
		return result;
	}

	private TypeArgument handleIsNotWildcard(Type type) {
		QualifiedTypeArgument result = genericsFactory.createQualifiedTypeArgument();
		result.setTypeReference(toTypeReferenceConverter.convert(type));
		utilToArrayDimensionsAndSetConverter.convert(type, result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, type);
		return result;
	}

	@SuppressWarnings("unchecked")
	private TypeArgument handleNoBound(WildcardType wildType) {
		UnknownTypeArgument result = genericsFactory.createUnknownTypeArgument();
		wildType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

	@SuppressWarnings("unchecked")
	private TypeArgument handleUpperBound(WildcardType wildType) {
		ExtendsTypeArgument result = genericsFactory.createExtendsTypeArgument();
		wildType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.setExtendType(toTypeReferenceConverter.convert(wildType.getBound()));
		utilToArrayDimensionsAndSetConverter.convert(wildType.getBound(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

}
