package jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;

import jamopp.parser.jdt.implementation.helper.UtilArraysImpl;
import jamopp.parser.jdt.implementation.helper.UtilBindingInfoToConcreteClassifierConverterImpl;
import jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;
import jamopp.parser.jdt.implementation.helper.UtilLayoutImpl;
import jamopp.parser.jdt.implementation.helper.UtilNamedElementImpl;
import jamopp.parser.jdt.implementation.helper.UtilReferenceWalkerImpl;
import jamopp.parser.jdt.implementation.helper.UtilToArrayDimensionAfterAndSetConverterImpl;
import jamopp.parser.jdt.implementation.helper.UtilToArrayDimensionsAndSetConverterImpl;
import jamopp.parser.jdt.implementation.helper.UtilToSwitchCasesAndSetConverterImpl;
import jamopp.parser.jdt.implementation.helper.UtilTypeInstructionSeparationImpl;
import jamopp.parser.jdt.interfaces.helper.UtilArrays;
import jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(UtilArrays.class).to(UtilArraysImpl.class);
		bind(UtilBindingInfoToConcreteClassifierConverter.class)
				.to(UtilBindingInfoToConcreteClassifierConverterImpl.class);
		bind(UtilJdtResolver.class).to(UtilJdtResolverImpl.class);
		bind(UtilLayout.class).to(UtilLayoutImpl.class);
		bind(UtilNamedElement.class).to(UtilNamedElementImpl.class);
		bind(UtilReferenceWalker.class).to(UtilReferenceWalkerImpl.class);
		bind(UtilToArrayDimensionAfterAndSetConverter.class).to(UtilToArrayDimensionAfterAndSetConverterImpl.class);
		bind(UtilToArrayDimensionsAndSetConverter.class).to(UtilToArrayDimensionsAndSetConverterImpl.class);
		bind(UtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverterImpl.class);
		bind(UtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparationImpl.class);
	}

}
