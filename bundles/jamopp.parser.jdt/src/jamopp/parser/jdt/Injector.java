package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.operators.OperatorsFactory;

class Injector {

	private static final OrdinaryCompilationUnitJDTASTVisitorAndConverter ordinaryCompilationUnitJDTASTVisitorAndConverter;
	private static final UtilJdtResolver jdtResolverUtility;
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

		jdtResolverUtility = new UtilJdtResolver(jdtBindingConverterUtility);

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
		ToTypeReferenceConverter toTypeReferenceConverter = new ToTypeReferenceConverter(
				toClassifierOrNamespaceClassifierReferenceConverter, toArrayDimensionAfterAndSetConverter,
				layoutInformationConverter, jdtBindingConverterUtility, toAnnotationInstanceConverter,
				toClassifierReferenceConverter);

		ToOrdinaryParameterConverter toOrdinaryParameterConverter = new ToOrdinaryParameterConverter(utilNamedElement,
				toTypeReferenceConverter, toModifierOrAnnotationInstanceConverter, toArrayDimensionAfterAndSetConverter,
				layoutInformationConverter, jdtResolverUtility);

		typeInstructionSeparationUtility = new UtilTypeInstructionSeparation(jdtResolverUtility,
				expressionConverterUtility, toAnnotationValueConverter);

		InNamespaceClassifierReferenceWrapper inNamespaceClassifierReferenceWrapper = new InNamespaceClassifierReferenceWrapper();
		ToBlockConverter toBlockConverter = new ToBlockConverter(toModifierConverter, typeInstructionSeparationUtility);
		ToTypeParameterConverter toTypeParameterConverter = new ToTypeParameterConverter(utilNamedElement,
				layoutInformationConverter, jdtResolverUtility, toTypeReferenceConverter,
				toAnnotationInstanceConverter);
		ToInterfaceMethodConverter toInterfaceMethodConverter = new ToInterfaceMethodConverter(
				typeInstructionSeparationUtility, utilNamedElement, layoutInformationConverter, jdtResolverUtility,
				toTypeReferenceConverter, toModifierOrAnnotationInstanceConverter);
		ToAdditionalFieldConverter toAdditionalFieldConverter = new ToAdditionalFieldConverter(utilNamedElement,
				layoutInformationConverter, jdtResolverUtility, typeInstructionSeparationUtility,
				toArrayDimensionAfterAndSetConverter);
		ToFieldConverter toFieldConverter = new ToFieldConverter(utilNamedElement, layoutInformationConverter,
				jdtResolverUtility, toTypeReferenceConverter, toModifierOrAnnotationInstanceConverter,
				typeInstructionSeparationUtility, toArrayDimensionAfterAndSetConverter, toAdditionalFieldConverter);
		ToReceiverParameterConverter toReceiverParameterConverter = new ToReceiverParameterConverter(
				toTypeReferenceConverter, toClassifierReferenceConverter);
		ToParameterConverter toParameterConverter = new ToParameterConverter(utilNamedElement,
				layoutInformationConverter, jdtResolverUtility, toTypeReferenceConverter, toOrdinaryParameterConverter,
				toModifierOrAnnotationInstanceConverter, toArrayDimensionAfterAndSetConverter,
				toAnnotationInstanceConverter);
		ToClassMethodOrConstructorConverter toClassMethodOrConstructorConverter = new ToClassMethodOrConstructorConverter(
				typeInstructionSeparationUtility, utilNamedElement, layoutInformationConverter,
				toTypeReferenceConverter, toTypeParameterConverter, toReceiverParameterConverter, toParameterConverter,
				toModifierOrAnnotationInstanceConverter, toArrayDimensionAfterAndSetConverter, jdtResolverUtility,
				inNamespaceClassifierReferenceWrapper);

		ToClassMemberConverter toClassMemberConverter = new ToClassMemberConverter(toBlockConverter,
				toClassMethodOrConstructorConverter, toInterfaceMethodConverter, toFieldConverter);
		ToAnonymousClassConverter toAnonymClassConverter = new ToAnonymousClassConverter(layoutInformationConverter,
				jdtResolverUtility, toClassMemberConverter);
		ToEnumConstantConverter toEnumConstantConverter = new ToEnumConstantConverter(utilNamedElement,
				layoutInformationConverter, jdtResolverUtility, expressionConverterUtility, toAnonymClassConverter,
				toAnnotationInstanceConverter);
		ToEnumConverter toEnumConverter = new ToEnumConverter(jdtResolverUtility, toTypeReferenceConverter,
				toEnumConstantConverter, toClassMemberConverter);

		ToInterfaceMethodOrConstructorConverter toInterfaceMethodOrConstructorConverter = new ToInterfaceMethodOrConstructorConverter(
				typeInstructionSeparationUtility, utilNamedElement, layoutInformationConverter, jdtResolverUtility,
				toTypeReferenceConverter, toTypeParameterConverter, toReceiverParameterConverter, toParameterConverter,
				toModifierOrAnnotationInstanceConverter, toClassMethodOrConstructorConverter,
				toArrayDimensionAfterAndSetConverter, inNamespaceClassifierReferenceWrapper);
		ToInterfaceMemberConverter toInterfaceMemberConverter = new ToInterfaceMemberConverter(
				toInterfaceMethodOrConstructorConverter, toClassMemberConverter);
		ToClassOrInterfaceConverter toClassOrInterfaceConverter = new ToClassOrInterfaceConverter(jdtResolverUtility,
				toTypeReferenceConverter, toTypeParameterConverter, toInterfaceMemberConverter, toClassMemberConverter);
		ToConcreteClassifierConverter toConcreteClassifierConverter = new ToConcreteClassifierConverter(
				toModifierOrAnnotationInstanceConverter, layoutInformationConverter, jdtResolverUtility,
				utilNamedElement, toInterfaceMemberConverter, toEnumConverter, toClassOrInterfaceConverter);

		toClassMemberConverter.setToConcreteClassifierConverter(toConcreteClassifierConverter);

		UtilReferenceConverter referenceConverterUtility = new UtilReferenceConverter(layoutInformationConverter,
				jdtResolverUtility, expressionConverterUtility, utilNamedElement, toTypeReferenceConverter,
				toArrayInitialisierConverter, toAnnotationInstanceConverter, toAnonymClassConverter);
		UtilStatementConverter statementConverterUtility = new UtilStatementConverter(utilNamedElement,
				toTypeReferenceConverter, toModifierOrAnnotationInstanceConverter, toArrayDimensionAfterAndSetConverter,
				referenceConverterUtility, layoutInformationConverter, jdtResolverUtility, expressionConverterUtility,
				toConcreteClassifierConverter, toModifierOrAnnotationInstanceConverter, toOrdinaryParameterConverter);

		ordinaryCompilationUnitJDTASTVisitorAndConverter = new OrdinaryCompilationUnitJDTASTVisitorAndConverter(
				layoutInformationConverter, jdtResolverUtility, toClassifierOrNamespaceClassifierReferenceConverter,
				factory, importsFactory, utilNamedElement, toAnnotationInstanceConverter,
				toConcreteClassifierConverter);

		ToExpressionConverter toExpressionConverter = new ToExpressionConverter(toTypeReferenceConverter,
				toOrdinaryParameterConverter, statementConverterUtility, layoutInformationConverter, jdtResolverUtility,
				jdtBindingConverterUtility, expressionsFactory);

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

	static UtilJdtResolver getJDTResolverUtility() {
		return jdtResolverUtility;
	}

	static UtilTypeInstructionSeparation getTypeInstructionSeparationUtility() {
		return typeInstructionSeparationUtility;
	}

}
