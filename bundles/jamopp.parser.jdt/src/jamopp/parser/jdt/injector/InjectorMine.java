package jamopp.parser.jdt.injector;

import org.emftext.language.java.containers.ContainersFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import jamopp.parser.jdt.converter.ToAnnotationInstanceConverter;
import jamopp.parser.jdt.converter.ToClassMemberConverter;
import jamopp.parser.jdt.converter.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.ToExpressionConverter;
import jamopp.parser.jdt.other.UtilJdtResolver;
import jamopp.parser.jdt.other.UtilStatementConverter;
import jamopp.parser.jdt.other.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.visitor.VisitorAndConverterOrdinaryCompilationUnitJDTAST;

public class InjectorMine {

	private static final VisitorAndConverterOrdinaryCompilationUnitJDTAST ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final UtilJdtResolver jdtResolverUtility;
	private static final UtilTypeInstructionSeparation typeInstructionSeparationUtility;
	private static final ContainersFactory containersFactory;

	static {
		Injector injector = Guice.createInjector(new InjectorGuice());
		
		ordinaryCompilationUnitJDTASTVisitorAndConverter  = injector.getInstance(VisitorAndConverterOrdinaryCompilationUnitJDTAST.class);
		jdtResolverUtility= injector.getInstance(UtilJdtResolver.class);
		typeInstructionSeparationUtility= injector.getInstance(UtilTypeInstructionSeparation.class);
		containersFactory = injector.getInstance(ContainersFactory.class);
		
		injector.getInstance(ToClassMemberConverter.class).setToConcreteClassifierConverter(injector.getInstance(ToConcreteClassifierConverter.class));
		injector.getInstance(ToAnnotationInstanceConverter.class).setTypeInstructionSeparationUtility(typeInstructionSeparationUtility);
		injector.getInstance(UtilTypeInstructionSeparation.class).setStatementConverterUtility(injector.getInstance(UtilStatementConverter.class));
	}

	public static VisitorAndConverterOrdinaryCompilationUnitJDTAST getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
	}

	public static UtilJdtResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	public static UtilTypeInstructionSeparation getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

	public static ContainersFactory getContainersFactory() {
		return containersFactory;
	}

}
