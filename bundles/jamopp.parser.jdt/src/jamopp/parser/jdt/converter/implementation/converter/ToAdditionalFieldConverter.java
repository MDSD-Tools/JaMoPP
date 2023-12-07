package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.members.AdditionalField;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToAdditionalFieldConverter implements Converter<VariableDeclarationFragment, AdditionalField> {

	private final IUtilJdtResolver iUtilJdtResolver;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final IUtilTypeInstructionSeparation toInstructionSeparation;
	private final IUtilLayout utilLayout;

	@Inject
	ToAdditionalFieldConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout,
			IUtilJdtResolver iUtilJdtResolver, IUtilTypeInstructionSeparation toInstructionSeparation,
			IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter) {
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
