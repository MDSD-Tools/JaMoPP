package jamopp.parser.jdt;

import com.google.inject.Guice;
import com.google.inject.Injector;

class InjectorMine {

	private static final OrdinaryCompilationUnitJDTASTVisitorAndConverter ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final UtilJdtResolver jdtResolverUtility;
	private static final UtilTypeInstructionSeparation typeInstructionSeparationUtility;

	static {
		Injector injector = Guice.createInjector(new InjectorGuice());
		
		ordinaryCompilationUnitJDTASTVisitorAndConverter  = injector.getInstance(OrdinaryCompilationUnitJDTASTVisitorAndConverter.class);
		jdtResolverUtility= injector.getInstance(UtilJdtResolver.class);
		typeInstructionSeparationUtility= injector.getInstance(UtilTypeInstructionSeparation.class);
		
		
		injector.getInstance(ToClassMemberConverter.class).setToConcreteClassifierConverter(injector.getInstance(ToConcreteClassifierConverter.class));
		injector.getInstance(UtilExpressionConverter.class).setToExpressionConverter(injector.getInstance(ToExpressionConverter.class));
		injector.getInstance(UtilJdtBindingConverter.class).setJDTResolverUtility(jdtResolverUtility);
		injector.getInstance(ToAnnotationInstanceConverter.class).setTypeInstructionSeparationUtility(typeInstructionSeparationUtility);
		injector.getInstance(UtilTypeInstructionSeparation.class).setStatementConverterUtility(injector.getInstance(UtilStatementConverter.class));
	}

	static OrdinaryCompilationUnitJDTASTVisitorAndConverter getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
	}

	static UtilJdtResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	static UtilTypeInstructionSeparation getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

}
