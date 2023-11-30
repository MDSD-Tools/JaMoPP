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
		
		ToExpressionConverter toExpressionConverter= injector.getInstance(ToExpressionConverter.class);
		ToClassMemberConverter toClassMemberConverter= injector.getInstance(ToClassMemberConverter.class);
		UtilExpressionConverter utilExpressionConverter= injector.getInstance(UtilExpressionConverter.class);
		UtilJdtBindingConverter utilJdtBindingConverter= injector.getInstance(UtilJdtBindingConverter.class);
		ToAnnotationInstanceConverter toAnnotationInstanceConverter= injector.getInstance(ToAnnotationInstanceConverter.class);
		
		
		typeInstructionSeparationUtility.setStatementConverterUtility(injector.getInstance(UtilStatementConverter.class));
		
			
		
		toExpressionConverter.setToPrimaryExpressionConverter(injector.getInstance(ToPrimaryExpressionConverter.class));
		toExpressionConverter.setToAssignmentConverter(injector.getInstance(ToAssignmentConverter.class));
		toExpressionConverter.setToConditionalExpressionConverter(injector.getInstance(ToConditionalExpressionConverter.class));
		toExpressionConverter.setToEqualityExpressionConverter(injector.getInstance(ToEqualityExpressionConverter.class));
		toExpressionConverter.setToRelationExpressionConverter(injector.getInstance(ToRelationExpressionConverter.class));
		toExpressionConverter.setToShiftExpressionConverter(injector.getInstance(ToShiftExpressionConverter.class));
		toExpressionConverter.setToAdditiveExpressionConverter(injector.getInstance(ToAdditiveExpressionConverter.class));
		toExpressionConverter.setToMultiplicativeExpressionConverter(injector.getInstance(ToMultiplicativeExpressionConverter.class));
		toExpressionConverter.setToUnaryExpressionConverter(injector.getInstance(ToUnaryExpressionConverter.class));
		toExpressionConverter.setToMethodReferenceExpressionConverter(injector.getInstance(ToMethodReferenceExpressionConverter.class));
		toClassMemberConverter.setToConcreteClassifierConverter(injector.getInstance(ToConcreteClassifierConverter.class));
		
		
		utilExpressionConverter.setToExpressionConverter(toExpressionConverter);
		utilJdtBindingConverter.setJDTResolverUtility(jdtResolverUtility);
		toAnnotationInstanceConverter.setTypeInstructionSeparationUtility(typeInstructionSeparationUtility);

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
