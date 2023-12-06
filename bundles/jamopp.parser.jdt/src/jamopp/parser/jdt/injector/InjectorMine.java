package jamopp.parser.jdt.injector;

import org.emftext.language.java.containers.ContainersFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.visitor.VisitorAndConverterOrdinaryCompilationUnitJDTAST;

public class InjectorMine {

	private static final VisitorAndConverterOrdinaryCompilationUnitJDTAST ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final UtilJdtResolver jdtResolverUtility;
	private static final IUtilTypeInstructionSeparation typeInstructionSeparationUtility;
	private static final ContainersFactory containersFactory;

	static {
		Injector injector = Guice.createInjector(new InjectorGuice());

		ordinaryCompilationUnitJDTASTVisitorAndConverter = injector
				.getInstance(VisitorAndConverterOrdinaryCompilationUnitJDTAST.class);
		jdtResolverUtility = injector.getInstance(UtilJdtResolver.class);
		typeInstructionSeparationUtility = injector.getInstance(UtilTypeInstructionSeparation.class);
		containersFactory = injector.getInstance(ContainersFactory.class);

	}

	public static VisitorAndConverterOrdinaryCompilationUnitJDTAST getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
	}

	public static UtilJdtResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	public static IUtilTypeInstructionSeparation getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

	public static ContainersFactory getContainersFactory() {
		return containersFactory;
	}

}
