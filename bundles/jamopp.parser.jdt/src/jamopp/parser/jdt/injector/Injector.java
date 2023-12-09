package jamopp.parser.jdt.injector;

import org.emftext.language.java.containers.ContainersFactory;

import com.google.inject.Guice;

import jamopp.parser.jdt.converter.implementation.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.visitor.AbstractVisitor;
import jamopp.parser.jdt.visitor.VisitorAndConverterAbstractAndEmptyModelJDTAST;

public class Injector {

	private static final AbstractVisitor visitor;
	private static final IUtilJdtResolver jdtResolverUtility;
	private static final IUtilTypeInstructionSeparation typeInstructionSeparationUtility;
	private static final ContainersFactory containersFactory;

	static {
		com.google.inject.Injector injector = Guice.createInjector(new UtilModule(), new FactoryModule(),
				new ConverterModule(), new HandlerModule(), new VisitorModule());

		visitor = injector.getInstance(VisitorAndConverterAbstractAndEmptyModelJDTAST.class);
		jdtResolverUtility = injector.getInstance(IUtilJdtResolver.class);
		typeInstructionSeparationUtility = injector.getInstance(UtilTypeInstructionSeparation.class);
		containersFactory = injector.getInstance(ContainersFactory.class);

	}

	public static AbstractVisitor getVisitor() {
		return visitor;
	}

	public static IUtilJdtResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	public static IUtilTypeInstructionSeparation getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

	public static ContainersFactory getContainersFactory() {
		return containersFactory;
	}

}
