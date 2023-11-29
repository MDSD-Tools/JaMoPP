package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.members.AdditionalField;

class ToAdditionalFieldConverter {

	private final UtilJdtResolver utilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final UtilLayout utilLayout;

	ToAdditionalFieldConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout,
			UtilJdtResolver utilJdtResolver, UtilTypeInstructionSeparation toInstructionSeparation,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.utilNamedElement = utilNamedElement;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toInstructionSeparation = toInstructionSeparation;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	AdditionalField convertToAdditionalField(VariableDeclarationFragment frag) {
		AdditionalField result;
		IVariableBinding binding = frag.resolveBinding();
		if (binding != null) {
			result = utilJdtResolver.getAdditionalField(binding);
		} else {
			result = utilJdtResolver.getAdditionalField(frag.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(frag.getName(), result);
		frag.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			toInstructionSeparation.addAdditionalField(frag.getInitializer(), result);
		}
		utilLayout.convertToMinimalLayoutInformation(result, frag);
		return result;
	}

}
