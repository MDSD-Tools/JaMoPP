package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.VariableLengthParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

public class ToParameterConverter implements ToConverter<SingleVariableDeclaration, Parameter> {

	private final UtilJdtResolver utilJDTResolver;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilLayout utilLayout;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final ToConverter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	ToParameterConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
			ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.utilJDTResolver = utilJDTResolver;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilNamedElement = utilNamedElement;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilLayout = utilLayout;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Parameter convert(SingleVariableDeclaration decl) {
		if (decl.isVarargs()) {
			VariableLengthParameter result = utilJDTResolver.getVariableLengthParameter(decl.resolveBinding());
			decl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
					.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
			result.setTypeReference(toTypeReferenceConverter.convert(decl.getType()));
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(decl.getType(), result);
			utilNamedElement.setNameOfElement(decl.getName(), result);
			decl.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
					.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
			decl.varargsAnnotations().forEach(
					obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, decl);
			return result;
		}
		return toOrdinaryParameterConverter.convert(decl);
	}

}
