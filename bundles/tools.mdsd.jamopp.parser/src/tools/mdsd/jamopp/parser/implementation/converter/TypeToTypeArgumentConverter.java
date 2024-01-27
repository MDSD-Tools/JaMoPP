package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

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
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class TypeToTypeArgumentConverter implements Converter<Type, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	public TypeToTypeArgumentConverter(final Converter<Type, TypeReference> toTypeReferenceConverter,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			final UtilLayout layoutInformationConverter, final GenericsFactory genericsFactory) {
		this.genericsFactory = genericsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeArgument convert(final Type type) {
		TypeArgument result;
		if (type.isWildcardType()) {
			final WildcardType wildType = (WildcardType) type;
			if (wildType.getBound() == null) {
				result = handleNoBound(wildType);
			} else if (wildType.isUpperBound()) {
				result = handleUpperBound(wildType);
			} else {
				final SuperTypeArgument newResult = genericsFactory.createSuperTypeArgument();
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

	private TypeArgument handleIsNotWildcard(final Type type) {
		final QualifiedTypeArgument result = genericsFactory.createQualifiedTypeArgument();
		result.setTypeReference(toTypeReferenceConverter.convert(type));
		utilToArrayDimensionsAndSetConverter.convert(type, result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, type);
		return result;
	}

	@SuppressWarnings("unchecked")
	private TypeArgument handleNoBound(final WildcardType wildType) {
		final UnknownTypeArgument result = genericsFactory.createUnknownTypeArgument();
		wildType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

	@SuppressWarnings("unchecked")
	private TypeArgument handleUpperBound(final WildcardType wildType) {
		final ExtendsTypeArgument result = genericsFactory.createExtendsTypeArgument();
		wildType.annotations()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		result.setExtendType(toTypeReferenceConverter.convert(wildType.getBound()));
		utilToArrayDimensionsAndSetConverter.convert(wildType.getBound(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, wildType);
		return result;
	}

}
