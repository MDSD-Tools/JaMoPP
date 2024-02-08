package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToFieldConverter implements Converter<FieldDeclaration, Field> {

	private final JdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final UtilLayout utilLayout;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<VariableDeclarationFragment, AdditionalField> toAdditionalFieldConverter;

	@Inject
	public ToFieldConverter(final UtilNamedElement utilNamedElement, final UtilLayout utilLayout,
			final JdtResolver iUtilJdtResolver, final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			final UtilTypeInstructionSeparation toInstructionSeparation,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			final Converter<VariableDeclarationFragment, AdditionalField> toAdditionalFieldConverter,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.utilNamedElement = utilNamedElement;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toInstructionSeparation = toInstructionSeparation;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.toAdditionalFieldConverter = toAdditionalFieldConverter;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Field convert(final FieldDeclaration fieldDecl) {
		final VariableDeclarationFragment firstFragment = (VariableDeclarationFragment) fieldDecl.fragments().get(0);
		Field result;
		final IVariableBinding binding = firstFragment.resolveBinding();
		if (binding != null) {
			result = iUtilJdtResolver.getField(binding);
		} else {
			result = iUtilJdtResolver.getField(firstFragment.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(firstFragment.getName(), result);
		fieldDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(fieldDecl.getType()));
		utilToArrayDimensionsAndSetConverter.convert(fieldDecl.getType(), result);
		firstFragment.extraDimensions()
				.forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convert((Dimension) obj, result));
		if (firstFragment.getInitializer() != null) {
			toInstructionSeparation.addField(firstFragment.getInitializer(), result);
		}
		for (int index = 1; index < fieldDecl.fragments().size(); index++) {
			result.getAdditionalFields().add(
					toAdditionalFieldConverter.convert((VariableDeclarationFragment) fieldDecl.fragments().get(index)));
		}
		utilLayout.convertToMinimalLayoutInformation(result, fieldDecl);
		return result;
	}

}
