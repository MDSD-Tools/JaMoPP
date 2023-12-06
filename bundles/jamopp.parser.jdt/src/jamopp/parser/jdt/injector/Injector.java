package jamopp.parser.jdt.injector;

import org.emftext.language.java.containers.ContainersFactory;

import com.google.inject.Guice;

import jamopp.parser.jdt.converter.implementation.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.visitor.VisitorAndConverterOrdinaryCompilationUnitJDTAST;

public class Injector {

	private static final VisitorAndConverterOrdinaryCompilationUnitJDTAST ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final IUtilJdtResolver jdtResolverUtility;
	private static final IUtilTypeInstructionSeparation typeInstructionSeparationUtility;
	private static final ContainersFactory containersFactory;

	static {
		com.google.inject.Injector injector = Guice.createInjector(new UtilModule(), new FactoryModule(),
				new ConverterModule(), new HandlerModule());

		ordinaryCompilationUnitJDTASTVisitorAndConverter = injector
				.getInstance(VisitorAndConverterOrdinaryCompilationUnitJDTAST.class);
		jdtResolverUtility = injector.getInstance(IUtilJdtResolver.class);
		typeInstructionSeparationUtility = injector.getInstance(UtilTypeInstructionSeparation.class);
		containersFactory = injector.getInstance(ContainersFactory.class);

	}

	public static VisitorAndConverterOrdinaryCompilationUnitJDTAST getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
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
