package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.operators.OperatorsFactory;

class Injector {

	private static final OrdinaryCompilationUnitJDTASTVisitorAndConverter ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final UtilJDTResolver jdtResolverUtility;
	private static final UtilTypeInstructionSeparation typeInstructionSeparationUtility;

	static {

		ModifiersFactory factory = ModifiersFactory.eINSTANCE;
		ImportsFactory importsFactory = ImportsFactory.eINSTANCE;
		OperatorsFactory operatorsFactory = OperatorsFactory.eINSTANCE;
		ExpressionsFactory expressionsFactory = ExpressionsFactory.eINSTANCE;
		LiteralsFactory literalsFactory = LiteralsFactory.eINSTANCE;

		UtilLayout layoutInformationConverter = new UtilLayout();
		UtilNamedElement utilNamedElement = new UtilNamedElement();
		UtilExpressionConverter expressionConverterUtility = new UtilExpressionConverter();
		UtilJDTBindingConverter jdtBindingConverterUtility = new UtilJDTBindingConverter();

		jdtResolverUtility = new UtilJDTResolver(jdtBindingConverterUtility);

		ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter = new ToMultiplicativeOperatorConverter();
		ToAssignmentConverter toAssignmentOperatorConverter = new ToAssignmentConverter(operatorsFactory);
		ToEqualityOperatorConverter toEqualityOperatorConverter = new ToEqualityOperatorConverter(operatorsFactory);
		ToRelationOperatorConverter toRelationOperatorConverter = new ToRelationOperatorConverter(operatorsFactory);
		ToShiftOperatorConverter toShiftOperatorConverter = new ToShiftOperatorConverter(operatorsFactory);
		ToAdditiveOperatorConverter toAdditiveOperatorConverter = new ToAdditiveOperatorConverter(operatorsFactory);
		ToUnaryOperatorConverter toUnaryOperatorConverter = new ToUnaryOperatorConverter(operatorsFactory);
		ToNumberLiteralConverter toNumberLiteralConverter = new ToNumberLiteralConverter(layoutInformationConverter);
		ToModifierConverter toModifierConverter = new ToModifierConverter(layoutInformationConverter);
		ToClassifierReferenceConverter toClassifierReferenceConverter = new ToClassifierReferenceConverter(
				jdtResolverUtility);

		ToClassifierOrNamespaceClassifierReferenceConverter toClassifierOrNamespaceClassifierReferenceConverter = new ToClassifierOrNamespaceClassifierReferenceConverter(
				utilNamedElement, toClassifierReferenceConverter);
		ToAnnotationInstanceConverter toAnnotationInstanceConverter = new ToAnnotationInstanceConverter(
				utilNamedElement, layoutInformationConverter, jdtResolverUtility);

		ToArrayInitialisierConverter toArrayInitialisierConverter = new ToArrayInitialisierConverter(
				expressionConverterUtility, layoutInformationConverter, toAnnotationInstanceConverter);
		ToAnnotationValueConverter toAnnotationValueConverter = new ToAnnotationValueConverter(
				expressionConverterUtility, toClassifierOrNamespaceClassifierReferenceConverter,
				toArrayInitialisierConverter, toAnnotationInstanceConverter);
		ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter = new ToArrayDimensionAfterAndSetConverter(
				layoutInformationConverter, toAnnotationInstanceConverter);
		ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter = new ToModifierOrAnnotationInstanceConverter(
				toModifierConverter, toAnnotationInstanceConverter);
		ToTypeReferenceConverter toTypeReferenceConverter = new ToTypeReferenceConverter(toClassifierOrNamespaceClassifierReferenceConverter, toArrayDimensionAfterAndSetConverter, layoutInformationConverter, jdtBindingConverterUtility, toAnnotationInstanceConverter, toClassifierReferenceConverter);

		typeInstructionSeparationUtility = new UtilTypeInstructionSeparation(jdtResolverUtility,
				expressionConverterUtility, toAnnotationValueConverter);
		UtilClassifierConverter classifierConverterUtility = new UtilClassifierConverter(typeInstructionSeparationUtility, layoutInformationConverter, jdtResolverUtility, expressionConverterUtility, toClassifierOrNamespaceClassifierReferenceConverter, utilNamedElement, toTypeReferenceConverter, toModifierOrAnnotationInstanceConverter, toArrayDimensionAfterAndSetConverter, toAnnotationInstanceConverter, toModifierConverter, toClassifierReferenceConverter);

		UtilReferenceConverter referenceConverterUtility = new UtilReferenceConverter(layoutInformationConverter,
				jdtResolverUtility, expressionConverterUtility, classifierConverterUtility,
				toClassifierOrNamespaceClassifierReferenceConverter, utilNamedElement, toTypeReferenceConverter,
				toArrayInitialisierConverter, toAnnotationInstanceConverter);
		UtilStatementConverter statementConverterUtility = new UtilStatementConverter(utilNamedElement,
				toTypeReferenceConverter, toModifierOrAnnotationInstanceConverter, toArrayDimensionAfterAndSetConverter,
				referenceConverterUtility, layoutInformationConverter, jdtResolverUtility, expressionConverterUtility,
				classifierConverterUtility, toModifierOrAnnotationInstanceConverter);

		ordinaryCompilationUnitJDTASTVisitorAndConverter = new OrdinaryCompilationUnitJDTASTVisitorAndConverter(
				layoutInformationConverter, jdtResolverUtility, toClassifierOrNamespaceClassifierReferenceConverter,
				factory, importsFactory, utilNamedElement, toAnnotationInstanceConverter, classifierConverterUtility);

		ToExpressionConverter toExpressionConverter = new ToExpressionConverter(expressionsFactory,
				statementConverterUtility, layoutInformationConverter, jdtResolverUtility, jdtBindingConverterUtility,
				classifierConverterUtility, toClassifierOrNamespaceClassifierReferenceConverter,
				toTypeReferenceConverter, toArrayDimensionAfterAndSetConverter);

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
				toExpressionConverter, referenceConverterUtility, layoutInformationConverter, toTypeReferenceConverter);

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
		toAnnotationInstanceConverter.setTypeInstructionSeparationUtility(typeInstructionSeparationUtility);
	}

	static OrdinaryCompilationUnitJDTASTVisitorAndConverter getOrdinaryCompilationUnitJDTASTVisitorAndConverter() {
		return ordinaryCompilationUnitJDTASTVisitorAndConverter;
	}

	static UtilJDTResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	static UtilTypeInstructionSeparation getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

}
