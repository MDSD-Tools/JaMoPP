package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToTypeParameterConverter implements
		ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter> {

	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver utilJDTResolver;
	private final IUtilLayout utilLayout;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToTypeParameterConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, IUtilJdtResolver utilJDTResolver,
			ToTypeReferenceConverter toTypeReferenceConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilJDTResolver = utilJDTResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	public org.emftext.language.java.generics.TypeParameter convert(TypeParameter param) {
		org.emftext.language.java.generics.TypeParameter result = utilJDTResolver
				.getTypeParameter(param.resolveBinding());
		param.modifiers()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		utilNamedElement.setNameOfElement(param.getName(), result);
		param.typeBounds().forEach(obj -> result.getExtendTypes().add(toTypeReferenceConverter.convert((Type) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, param);
		return result;
	}

}
