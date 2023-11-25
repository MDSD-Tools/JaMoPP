package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.operators.OperatorsFactory;

public class Injector {

	private static OrdinaryCompilationUnitJDTASTVisitorAndConverter ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static JDTResolverUtility jdtResolverUtility;
	private static TypeInstructionSeparationUtility typeInstructionSeparationUtility;

	static {
		ModifiersFactory factory = ModifiersFactory.eINSTANCE;
		ImportsFactory importsFactory = ImportsFactory.eINSTANCE;
		OperatorsFactory operatorsFactory = OperatorsFactory.eINSTANCE;
		ExpressionsFactory expressionsFactory = ExpressionsFactory.eINSTANCE;
		LiteralsFactory literalsFactory = LiteralsFactory.eINSTANCE;

		LayoutInformationConverter layoutInformationConverter = new LayoutInformationConverter();
		ToNumberLiteralConverter toNumberLiteralConverter = new ToNumberLiteralConverter(layoutInformationConverter);

		ExpressionConverterUtility expressionConverterUtility = new ExpressionConverterUtility();
		JDTBindingConverterUtility jdtBindingConverterUtility = new JDTBindingConverterUtility();
		JDTResolverUtility jdtResolverUtility = new JDTResolverUtility(jdtBindingConverterUtility);

		BaseConverterUtility baseConverterUtility = new BaseConverterUtility(layoutInformationConverter,
				jdtResolverUtility, jdtBindingConverterUtility);

		AnnotationInstanceOrModifierConverterUtility annotationInstanceOrModifierConverterUtility = new AnnotationInstanceOrModifierConverterUtility(
				layoutInformationConverter, jdtResolverUtility, expressionConverterUtility, baseConverterUtility);

		TypeInstructionSeparationUtility typeInstructionSeparationUtility = new TypeInstructionSeparationUtility(
				jdtResolverUtility, expressionConverterUtility, annotationInstanceOrModifierConverterUtility);
		ClassifierConverterUtility classifierConverterUtility = new ClassifierConverterUtility(
				typeInstructionSeparationUtility, layoutInformationConverter, jdtResolverUtility,
				expressionConverterUtility, baseConverterUtility, annotationInstanceOrModifierConverterUtility);

		ReferenceConverterUtility referenceConverterUtility = new ReferenceConverterUtility(layoutInformationConverter,
				jdtResolverUtility, expressionConverterUtility, classifierConverterUtility, baseConverterUtility,
				annotationInstanceOrModifierConverterUtility);
		StatementConverterUtility statementConverterUtility = new StatementConverterUtility(referenceConverterUtility,
				layoutInformationConverter, jdtResolverUtility, expressionConverterUtility, classifierConverterUtility,
				baseConverterUtility, annotationInstanceOrModifierConverterUtility);

		ordinaryCompilationUnitJDTASTVisitorAndConverter = new OrdinaryCompilationUnitJDTASTVisitorAndConverter(
				layoutInformationConverter, jdtResolverUtility, baseConverterUtility, factory, importsFactory,
				annotationInstanceOrModifierConverterUtility, classifierConverterUtility);

		ToAssignmentConverter toAssignmentOperatorConverter = new ToAssignmentConverter(operatorsFactory);
		ToEqualityOperatorConverter toEqualityOperatorConverter = new ToEqualityOperatorConverter(operatorsFactory);
		ToRelationOperatorConverter toRelationOperatorConverter = new ToRelationOperatorConverter(operatorsFactory);
		ToShiftOperatorConverter toShiftOperatorConverter = new ToShiftOperatorConverter(operatorsFactory);
		ToAdditiveOperatorConverter toAdditiveOperatorConverter = new ToAdditiveOperatorConverter(operatorsFactory);
		ToUnaryOperatorConverter toUnaryOperatorConverter = new ToUnaryOperatorConverter(operatorsFactory);
		ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter = new ToMultiplicativeOperatorConverter();

		ToExpressionConverter toExpressionConverter = new ToExpressionConverter(expressionsFactory,
				statementConverterUtility, layoutInformationConverter, jdtResolverUtility, jdtBindingConverterUtility,
				classifierConverterUtility, baseConverterUtility);
		ToConditionalExpressionConverter toConditionalExpressionConverter = new ToConditionalExpressionConverter(
				toExpressionConverter, layoutInformationConverter);

		ToPrimaryExpressionConverter toPrimaryExpressionConverter = new ToPrimaryExpressionConverter(literalsFactory,
				toNumberLiteralConverter, referenceConverterUtility, layoutInformationConverter);
		ToEqualityExpressionConverter toEqualityExpressionConverter = new ToEqualityExpressionConverter(
				toExpressionConverter, toEqualityOperatorConverter, layoutInformationConverter);
		ToRelationExpressionConverter toRelationExpressionConverter = new ToRelationExpressionConverter(
				toRelationOperatorConverter, toExpressionConverter, layoutInformationConverter);
		ToShiftExpressionConverter toShiftExpressionConverter = new ToShiftExpressionConverter(toShiftOperatorConverter,
				toExpressionConverter, layoutInformationConverter, expressionsFactory);
		ToAdditiveExpressionConverter toAdditiveExpressionConverter = new ToAdditiveExpressionConverter(
				toExpressionConverter, toAdditiveOperatorConverter, layoutInformationConverter);
		ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter = new ToMultiplicativeExpressionConverter(
				toMultiplicativeOperatorConverter, toExpressionConverter, layoutInformationConverter);
		ToUnaryExpressionConverter toUnaryExpressionConverter = new ToUnaryExpressionConverter(toUnaryOperatorConverter,
				toExpressionConverter, layoutInformationConverter, expressionsFactory);
		ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter = new ToMethodReferenceExpressionConverter(
				toExpressionConverter, referenceConverterUtility, layoutInformationConverter, baseConverterUtility);

		toExpressionConverter.setToAssignmentOperatorConverter(toAssignmentOperatorConverter);
		toExpressionConverter.setToConditionalExpressionConverter(toConditionalExpressionConverter);
		toExpressionConverter.setToEqualityExpressionConverter(toEqualityExpressionConverter);
		toExpressionConverter.setToRelationExpressionConverter(toRelationExpressionConverter);
		toExpressionConverter.setToShiftExpressionConverter(toShiftExpressionConverter);
		toExpressionConverter.setToAdditiveExpressionConverter(toAdditiveExpressionConverter);
		toExpressionConverter.setToMultiplicativeExpressionConverter(toMultiplicativeExpressionConverter);
		toExpressionConverter.setToUnaryExpressionConverter(toUnaryExpressionConverter);
		toExpressionConverter.setToMethodReferenceExpressionConverter(toMethodReferenceExpressionConverter);

		expressionConverterUtility.setToConditionalExpressionConverter(toConditionalExpressionConverter);
		expressionConverterUtility.setToExpressionConverter(toExpressionConverter);

		baseConverterUtility
				.setAnnotationInstanceOrModifierConverterUtility(annotationInstanceOrModifierConverterUtility);
		typeInstructionSeparationUtility.setStatementConverterUtility(statementConverterUtility);
		jdtBindingConverterUtility.setJDTResolverUtility(jdtResolverUtility);
		annotationInstanceOrModifierConverterUtility.setTypeInstructionSeparationUtility(typeInstructionSeparationUtility);
	}

	static OrdinaryCompilationUnitJDTASTVisitorAndConverter getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
	}

	static JDTResolverUtility getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	static TypeInstructionSeparationUtility getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

}
