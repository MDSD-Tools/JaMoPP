package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.members.Field;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.helper.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToFieldConverter implements ToConverter<FieldDeclaration, Field> {

	private final UtilJdtResolver utilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final IUtilTypeInstructionSeparation toInstructionSeparation;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final ToAdditionalFieldConverter toAdditionalFieldConverter;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;

	@Inject
	ToFieldConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToTypeReferenceConverter toTypeReferenceConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			IUtilTypeInstructionSeparation toInstructionSeparation,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			ToAdditionalFieldConverter toAdditionalFieldConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.utilJdtResolver = utilJdtResolver;
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
			result = utilJdtResolver.getField(binding);
		} else {
			result = utilJdtResolver.getField(firstFragment.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(firstFragment.getName(), result);
		fieldDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(fieldDecl.getType()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(fieldDecl.getType(), result);
		firstFragment.extraDimensions()
				.forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
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
