package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.operators.OperatorsFactory;

class Injector {

	private static final OrdinaryCompilationUnitJDTASTVisitorAndConverter ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final UtilJDTResolver jdtResolverUtility;
	private static final TypeInstructionSeparationUtility typeInstructionSeparationUtility;

	static {
		ModifiersFactory factory = ModifiersFactory.eINSTANCE;
		ImportsFactory importsFactory = ImportsFactory.eINSTANCE;
		OperatorsFactory operatorsFactory = OperatorsFactory.eINSTANCE;
		ExpressionsFactory expressionsFactory = ExpressionsFactory.eINSTANCE;
		LiteralsFactory literalsFactory = LiteralsFactory.eINSTANCE;

		LayoutInformationConverter layoutInformationConverter = new LayoutInformationConverter();
		UtilNamedElement utilNamedElement = new UtilNamedElement();
		ToNumberLiteralConverter toNumberLiteralConverter = new ToNumberLiteralConverter(layoutInformationConverter);

		UtilExpressionConverter expressionConverterUtility = new UtilExpressionConverter();
		UtilJDTBindingConverter jdtBindingConverterUtility = new UtilJDTBindingConverter();
		jdtResolverUtility = new UtilJDTResolver(jdtBindingConverterUtility);

		UtilBaseConverter utilBaseConverter = new UtilBaseConverter(layoutInformationConverter,
				jdtResolverUtility, jdtBindingConverterUtility, expressionConverterUtility, utilNamedElement);

		typeInstructionSeparationUtility = new TypeInstructionSeparationUtility(jdtResolverUtility,
				expressionConverterUtility, utilBaseConverter);
		UtilClassifierConverter classifierConverterUtility = new UtilClassifierConverter(
				typeInstructionSeparationUtility, layoutInformationConverter, jdtResolverUtility,
				expressionConverterUtility, utilBaseConverter, utilNamedElement);

		UtilReferenceConverter referenceConverterUtility = new UtilReferenceConverter(layoutInformationConverter,
				jdtResolverUtility, expressionConverterUtility, classifierConverterUtility, utilBaseConverter,
				utilNamedElement);
		UtilStatementConverter statementConverterUtility = new UtilStatementConverter(referenceConverterUtility,
				layoutInformationConverter, jdtResolverUtility, expressionConverterUtility, classifierConverterUtility,
				utilBaseConverter, utilNamedElement);

		ordinaryCompilationUnitJDTASTVisitorAndConverter = new OrdinaryCompilationUnitJDTASTVisitorAndConverter(
				layoutInformationConverter, jdtResolverUtility, utilBaseConverter, factory, importsFactory,
				utilNamedElement, classifierConverterUtility);

		ToAssignmentConverter toAssignmentOperatorConverter = new ToAssignmentConverter(operatorsFactory);
		ToEqualityOperatorConverter toEqualityOperatorConverter = new ToEqualityOperatorConverter(operatorsFactory);
		ToRelationOperatorConverter toRelationOperatorConverter = new ToRelationOperatorConverter(operatorsFactory);
		ToShiftOperatorConverter toShiftOperatorConverter = new ToShiftOperatorConverter(operatorsFactory);
		ToAdditiveOperatorConverter toAdditiveOperatorConverter = new ToAdditiveOperatorConverter(operatorsFactory);
		ToUnaryOperatorConverter toUnaryOperatorConverter = new ToUnaryOperatorConverter(operatorsFactory);
		ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter = new ToMultiplicativeOperatorConverter();

		ToExpressionConverter toExpressionConverter = new ToExpressionConverter(expressionsFactory,
				statementConverterUtility, layoutInformationConverter, jdtResolverUtility, jdtBindingConverterUtility,
				classifierConverterUtility, utilBaseConverter);
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
				toExpressionConverter, referenceConverterUtility, layoutInformationConverter, utilBaseConverter);

		toExpressionConverter.setToPrimaryExpressionConverter(toPrimaryExpressionConverter);
		toExpressionConverter.setToAssignmentOperatorConverter(toAssignmentOperatorConverter);
		toExpressionConverter.setToConditionalExpressionConverter(toConditionalExpressionConverter);
		toExpressionConverter.setToEqualityExpressionConverter(toEqualityExpressionConverter);
		toExpressionConverter.setToRelationExpressionConverter(toRelationExpressionConverter);
		toExpressionConverter.setToShiftExpressionConverter(toShiftExpressionConverter);
		toExpressionConverter.setToAdditiveExpressionConverter(toAdditiveExpressionConverter);
		toExpressionConverter.setToMultiplicativeExpressionConverter(toMultiplicativeExpressionConverter);
		toExpressionConverter.setToUnaryExpressionConverter(toUnaryExpressionConverter);
		toExpressionConverter.setToMethodReferenceExpressionConverter(toMethodReferenceExpressionConverter);

		expressionConverterUtility.setToExpressionConverter(toExpressionConverter);

		typeInstructionSeparationUtility.setStatementConverterUtility(statementConverterUtility);
		jdtBindingConverterUtility.setJDTResolverUtility(jdtResolverUtility);
		utilBaseConverter.setTypeInstructionSeparationUtility(typeInstructionSeparationUtility);
	}

	static OrdinaryCompilationUnitJDTASTVisitorAndConverter getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
	}

	static UtilJDTResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	static TypeInstructionSeparationUtility getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

}
