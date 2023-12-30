package tools.mdsd.jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;

import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingInfoToConcreteClassifierConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionAfterAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionsAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToSwitchCasesAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilArraysImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilLayoutImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilNamedElementImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilReferenceWalkerImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilTypeInstructionSeparationImpl;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToConcreteClassifierConverterWithExtraInfo;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToSwitchCasesAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class HelperModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(UtilArrays.class).to(UtilArraysImpl.class);
		bind(ToConcreteClassifierConverterWithExtraInfo.class).to(BindingInfoToConcreteClassifierConverterImpl.class);
		bind(UtilLayout.class).to(UtilLayoutImpl.class);
		bind(UtilNamedElement.class).to(UtilNamedElementImpl.class);
		bind(UtilReferenceWalker.class).to(UtilReferenceWalkerImpl.class);
		bind(ToArrayDimensionAfterAndSetConverter.class).to(ToArrayDimensionAfterAndSetConverterImpl.class);
		bind(ToArrayDimensionsAndSetConverter.class).to(ToArrayDimensionsAndSetConverterImpl.class);
		bind(ToSwitchCasesAndSetConverter.class).to(ToSwitchCasesAndSetConverterImpl.class);
		bind(UtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparationImpl.class);
	}

}
