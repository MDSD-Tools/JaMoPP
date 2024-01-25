package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToAdditionalFieldConverter implements Converter<VariableDeclarationFragment, AdditionalField> {

	private final JdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final UtilLayout utilLayout;

	@Inject
	public ToAdditionalFieldConverter(final UtilNamedElement utilNamedElement, final UtilLayout utilLayout,
			final JdtResolver iUtilJdtResolver, final UtilTypeInstructionSeparation toInstructionSeparation,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.utilNamedElement = utilNamedElement;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.toInstructionSeparation = toInstructionSeparation;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AdditionalField convert(final VariableDeclarationFragment frag) {
		AdditionalField result;
		final IVariableBinding binding = frag.resolveBinding();
		if (binding != null) {
			result = iUtilJdtResolver.getAdditionalField(binding);
		} else {
			result = iUtilJdtResolver.getAdditionalField(frag.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(frag.getName(), result);
		frag.extraDimensions()
				.forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convert((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			toInstructionSeparation.addAdditionalField(frag.getInitializer(), result);
		}
		utilLayout.convertToMinimalLayoutInformation(result, frag);
		return result;
	}

}
