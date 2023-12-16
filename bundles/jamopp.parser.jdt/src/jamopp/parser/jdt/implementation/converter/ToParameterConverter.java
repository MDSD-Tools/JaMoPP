package jamopp.parser.jdt.implementation.converter;

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

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

public class ToParameterConverter implements Converter<SingleVariableDeclaration, Parameter> {

	private final IUtilJdtResolver utilJDTResolver;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilLayout utilLayout;
	private final IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	ToParameterConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, IUtilJdtResolver utilJDTResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
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
