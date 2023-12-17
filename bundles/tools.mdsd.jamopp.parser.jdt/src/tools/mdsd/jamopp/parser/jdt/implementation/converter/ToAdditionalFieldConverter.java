package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import tools.mdsd.jamopp.model.java.members.AdditionalField;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class ToAdditionalFieldConverter implements Converter<VariableDeclarationFragment, AdditionalField> {

	private final UtilJdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final UtilLayout utilLayout;

	@Inject
	ToAdditionalFieldConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout,
			UtilJdtResolver iUtilJdtResolver, UtilTypeInstructionSeparation toInstructionSeparation,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
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
			result = iUtilJdtResolver.getAdditionalField(binding);
		} else {
			result = iUtilJdtResolver.getAdditionalField(frag.getName().getIdentifier());
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
