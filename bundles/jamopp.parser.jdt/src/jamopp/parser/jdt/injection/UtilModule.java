package jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;

import jamopp.parser.jdt.implementation.helper.UtilArrays;
import jamopp.parser.jdt.implementation.helper.UtilBindingInfoToConcreteClassifierConverter;
import jamopp.parser.jdt.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.implementation.helper.UtilLayout;
import jamopp.parser.jdt.implementation.helper.UtilNamedElement;
import jamopp.parser.jdt.implementation.helper.UtilReferenceWalker;
import jamopp.parser.jdt.implementation.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.implementation.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.implementation.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.implementation.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.interfaces.helper.IUtilArrays;
import jamopp.parser.jdt.interfaces.helper.IUtilBindingInfoToConcreteClassifierConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.IUtilReferenceWalker;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.IUtilTypeInstructionSeparation;

public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(IUtilArrays.class).to(UtilArrays.class);
		bind(IUtilBindingInfoToConcreteClassifierConverter.class)
				.to(UtilBindingInfoToConcreteClassifierConverter.class);
		bind(IUtilJdtResolver.class).to(UtilJdtResolver.class);
		bind(IUtilLayout.class).to(UtilLayout.class);
		bind(IUtilNamedElement.class).to(UtilNamedElement.class);
		bind(IUtilReferenceWalker.class).to(UtilReferenceWalker.class);
		bind(IUtilToArrayDimensionAfterAndSetConverter.class).to(UtilToArrayDimensionAfterAndSetConverter.class);
		bind(IUtilToArrayDimensionsAndSetConverter.class).to(UtilToArrayDimensionsAndSetConverter.class);
		bind(IUtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverter.class);
		bind(IUtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparation.class);
	}

}
