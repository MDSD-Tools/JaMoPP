package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToParameterConverter implements ToConverter<SingleVariableDeclaration, Parameter> {

	private final UtilJdtResolver utilJDTResolver;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilNamedElement utilNamedElement;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final UtilLayout utilLayout;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final ToOrdinaryParameterConverter toOrdinaryParameterConverter;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;

	@Inject
	ToParameterConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
			ToTypeReferenceConverter toTypeReferenceConverter,
			ToOrdinaryParameterConverter toOrdinaryParameterConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
