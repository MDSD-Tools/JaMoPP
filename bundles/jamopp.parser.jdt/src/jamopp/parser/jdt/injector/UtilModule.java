package jamopp.parser.jdt.injector;

import com.google.inject.AbstractModule;

import jamopp.parser.jdt.converter.implementation.helper.UtilArrays;
import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilNamedElement;
import jamopp.parser.jdt.converter.implementation.helper.UtilReferenceWalker;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.implementation.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.implementation.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilArrays;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilReferenceWalker;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(IUtilArrays.class).to(UtilArrays.class);
		bind(IUtilLayout.class).to(UtilLayout.class);
		bind(IUtilNamedElement.class).to(UtilNamedElement.class);
		bind(IUtilReferenceWalker.class).to(UtilReferenceWalker.class);
		bind(IUtilToArrayDimensionAfterAndSetConverter.class).to(UtilToArrayDimensionAfterAndSetConverter.class);
		bind(IUtilToArrayDimensionsAndSetConverter.class).to(UtilToArrayDimensionsAndSetConverter.class);
		bind(IUtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverter.class);
		bind(IUtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparation.class);
	}

}
