package jamopp.parser.jdt.injector;

import org.emftext.language.java.containers.ContainersFactory;

import com.google.inject.Guice;

import jamopp.parser.jdt.implementation.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.implementation.visitor.VisitorAndConverterAbstractAndEmptyModelJDTAST;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

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
