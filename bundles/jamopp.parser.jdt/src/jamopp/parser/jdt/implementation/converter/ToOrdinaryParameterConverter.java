package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;

public class ToOrdinaryParameterConverter implements Converter<SingleVariableDeclaration, OrdinaryParameter> {

	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	ToOrdinaryParameterConverter(UtilNamedElement utilNamedElement,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OrdinaryParameter convert(SingleVariableDeclaration decl) {
		OrdinaryParameter result = jdtResolverUtility.getOrdinaryParameter(decl.resolveBinding());
		decl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(decl.getType()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(decl.getType(), result);
		utilNamedElement.setNameOfElement(decl.getName(), result);
		decl.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, decl);
		return result;
	}

}
