package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

import com.google.inject.Inject;

import jamopp.parser.jdt.resolver.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToTypeParameterConverter
		extends ToConverter<TypeParameter, org.emftext.language.java.generics.TypeParameter> {

	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver utilJDTResolver;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilLayout utilLayout;

	@Inject
	ToTypeParameterConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
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
		param.modifiers().forEach(obj -> result.getAnnotations()
				.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
		utilNamedElement.setNameOfElement(param.getName(), result);
		param.typeBounds().forEach(obj -> result.getExtendTypes().add(toTypeReferenceConverter.convert((Type) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, param);
		return result;
	}

}
