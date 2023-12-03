package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.members.Field;

import com.google.inject.Inject;

import jamopp.parser.jdt.binding.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToFieldConverter extends ToConverter<FieldDeclaration, Field> {

	private final UtilJdtResolver utilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final ToAdditionalFieldConverter toAdditionalFieldConverter;
	private final UtilLayout utilLayout;

	@Inject
	ToFieldConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToTypeReferenceConverter toTypeReferenceConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			UtilTypeInstructionSeparation toInstructionSeparation,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			ToAdditionalFieldConverter toAdditionalFieldConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.utilNamedElement = utilNamedElement;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toInstructionSeparation = toInstructionSeparation;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toAdditionalFieldConverter = toAdditionalFieldConverter;
		this.utilLayout = utilLayout;
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
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(fieldDecl.getType(), result);
		firstFragment.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
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
