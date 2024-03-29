package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToOrdinaryParameterConverter implements Converter<SingleVariableDeclaration, OrdinaryParameter> {

	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	public ToOrdinaryParameterConverter(final UtilNamedElement utilNamedElement,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
	public OrdinaryParameter convert(final SingleVariableDeclaration decl) {
		final OrdinaryParameter result = jdtResolverUtility.getOrdinaryParameter(decl.resolveBinding());
		decl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(decl.getType()));
		utilToArrayDimensionsAndSetConverter.convert(decl.getType(), result);
		utilNamedElement.setNameOfElement(decl.getName(), result);
		decl.extraDimensions()
				.forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convert((Dimension) obj, result));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, decl);
		return result;
	}

}
