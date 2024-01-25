package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToTypeParameterConverter
		implements Converter<TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter> {

	private final UtilNamedElement utilNamedElement;
	private final JdtResolver utilJDTResolver;
	private final UtilLayout utilLayout;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	public ToTypeParameterConverter(final UtilNamedElement utilNamedElement, final UtilLayout utilLayout,
			final JdtResolver utilJDTResolver, final ToTypeReferenceConverter toTypeReferenceConverter,
			final ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilJDTResolver = utilJDTResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilLayout = utilLayout;
	}

	@Override
	@SuppressWarnings("unchecked")
	public tools.mdsd.jamopp.model.java.generics.TypeParameter convert(final TypeParameter param) {
		final tools.mdsd.jamopp.model.java.generics.TypeParameter result = utilJDTResolver
				.getTypeParameter(param.resolveBinding());
		param.modifiers()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		utilNamedElement.setNameOfElement(param.getName(), result);
		param.typeBounds().forEach(obj -> result.getExtendTypes().add(toTypeReferenceConverter.convert((Type) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, param);
		return result;
	}

}
