package jamopp.parser.jdt.converter.implementation.converter;

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

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToFieldConverter implements Converter<FieldDeclaration, Field> {

	private final IUtilJdtResolver iUtilJdtResolver;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilLayout utilLayout;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final IUtilTypeInstructionSeparation toInstructionSeparation;
	private final IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<VariableDeclarationFragment, AdditionalField> toAdditionalFieldConverter;

	@Inject
	ToFieldConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, IUtilJdtResolver iUtilJdtResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			IUtilTypeInstructionSeparation toInstructionSeparation,
			IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			Converter<VariableDeclarationFragment, AdditionalField> toAdditionalFieldConverter,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
