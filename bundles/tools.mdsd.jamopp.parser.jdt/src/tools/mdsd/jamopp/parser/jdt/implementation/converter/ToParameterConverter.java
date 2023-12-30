package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToParameterConverter implements Converter<SingleVariableDeclaration, Parameter> {

	private final UtilJdtResolver utilJDTResolver;
	private final UtilNamedElement utilNamedElement;
	private final UtilLayout utilLayout;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	ToParameterConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
			utilToArrayDimensionsAndSetConverter.convert(decl.getType(), result);
			utilNamedElement.setNameOfElement(decl.getName(), result);
			decl.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
					.convert((Dimension) obj, result));
			decl.varargsAnnotations().forEach(
					obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, decl);
			return result;
		}
		return toOrdinaryParameterConverter.convert(decl);
	}

}
