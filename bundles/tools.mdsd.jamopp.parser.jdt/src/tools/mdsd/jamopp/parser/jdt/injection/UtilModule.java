package tools.mdsd.jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilArraysImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilBindingInfoToConcreteClassifierConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilLayoutImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilNamedElementImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilReferenceWalkerImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilToArrayDimensionAfterAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilToArrayDimensionsAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilToSwitchCasesAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilTypeInstructionSeparationImpl;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToSwitchCasesAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(UtilArrays.class).to(UtilArraysImpl.class);
		bind(UtilBindingInfoToConcreteClassifierConverter.class)
				.to(UtilBindingInfoToConcreteClassifierConverterImpl.class);
		bind(UtilJdtResolver.class).to(UtilJdtResolverImpl.class).in(Singleton.class);
		bind(UtilLayout.class).to(UtilLayoutImpl.class);
		bind(UtilNamedElement.class).to(UtilNamedElementImpl.class);
		bind(UtilReferenceWalker.class).to(UtilReferenceWalkerImpl.class);
		bind(UtilToArrayDimensionAfterAndSetConverter.class).to(UtilToArrayDimensionAfterAndSetConverterImpl.class);
		bind(UtilToArrayDimensionsAndSetConverter.class).to(UtilToArrayDimensionsAndSetConverterImpl.class);
		bind(UtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverterImpl.class);
		bind(UtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparationImpl.class);
	}

}
