package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

class ToTypeParameterConverter {

	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver utilJDTResolver;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilLayout utilLayout;

	public ToTypeParameterConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout,
			UtilJdtResolver utilJDTResolver, ToTypeReferenceConverter toTypeReferenceConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilJDTResolver = utilJDTResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.generics.TypeParameter convertToTypeParameter(TypeParameter param) {
		org.emftext.language.java.generics.TypeParameter result = utilJDTResolver
				.getTypeParameter(param.resolveBinding());
		param.modifiers().forEach(obj -> result.getAnnotations()
				.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
		utilNamedElement.setNameOfElement(param.getName(), result);
		param.typeBounds().forEach(
				obj -> result.getExtendTypes().add(toTypeReferenceConverter.convertToTypeReference((Type) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, param);
		return result;
	}

}
