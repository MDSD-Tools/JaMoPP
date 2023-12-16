package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class ToFieldConverter implements Converter<FieldDeclaration, Field> {

	private final UtilJdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<VariableDeclarationFragment, AdditionalField> toAdditionalFieldConverter;

	@Inject
	ToFieldConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver iUtilJdtResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			UtilTypeInstructionSeparation toInstructionSeparation,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			Converter<VariableDeclarationFragment, AdditionalField> toAdditionalFieldConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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

	@SuppressWarnings("unchecked")
	public Field convert(FieldDeclaration fieldDecl) {
		VariableDeclarationFragment firstFragment = (VariableDeclarationFragment) fieldDecl.fragments().get(0);
		Field result;
		IVariableBinding binding = firstFragment.resolveBinding();
		if (binding != null) {
			result = iUtilJdtResolver.getField(binding);
		} else {
			result = iUtilJdtResolver.getField(firstFragment.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(firstFragment.getName(), result);
		fieldDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(fieldDecl.getType()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(fieldDecl.getType(), result);
		firstFragment.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
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
