package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.members.AdditionalField;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToAdditionalFieldConverter implements ToConverter<VariableDeclarationFragment, AdditionalField> {

	private final UtilJdtResolver utilJdtResolver;
	private final IUtilNamedElement utilNamedElement;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final IUtilTypeInstructionSeparation toInstructionSeparation;
	private final IUtilLayout utilLayout;

	@Inject
	ToAdditionalFieldConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout,
			UtilJdtResolver utilJdtResolver, IUtilTypeInstructionSeparation toInstructionSeparation,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.utilNamedElement = utilNamedElement;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.toInstructionSeparation = toInstructionSeparation;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AdditionalField convert(VariableDeclarationFragment frag) {
		AdditionalField result;
		IVariableBinding binding = frag.resolveBinding();
		if (binding != null) {
			result = utilJdtResolver.getAdditionalField(binding);
		} else {
			result = utilJdtResolver.getAdditionalField(frag.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(frag.getName(), result);
		frag.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			toInstructionSeparation.addAdditionalField(frag.getInitializer(), result);
		}
		utilLayout.convertToMinimalLayoutInformation(result, frag);
		return result;
	}

}
