package tools.mdsd.jamopp.parser.injection;

import com.google.inject.AbstractModule;

import tools.mdsd.jamopp.parser.implementation.helper.UtilArraysImpl;
import tools.mdsd.jamopp.parser.implementation.helper.UtilLayoutImpl;
import tools.mdsd.jamopp.parser.implementation.helper.UtilNamedElementImpl;
import tools.mdsd.jamopp.parser.implementation.helper.UtilReferenceWalkerImpl;
import tools.mdsd.jamopp.parser.implementation.helper.UtilTypeInstructionSeparationImpl;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilReferenceWalker;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;

public class HelperModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(UtilArrays.class).to(UtilArraysImpl.class);
		bind(UtilLayout.class).to(UtilLayoutImpl.class);
		bind(UtilNamedElement.class).to(UtilNamedElementImpl.class);
		bind(UtilReferenceWalker.class).to(UtilReferenceWalkerImpl.class);
		bind(UtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparationImpl.class);
	}

}
