package jamopp.parser.jdt.converter.implementation;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.interfaces.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToSwitchCaseConverter {

	private final ExpressionsFactory expressionsFactory;
	private final StatementsFactory statementsFactory;;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final ToConcreteClassifierConverter classifierConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToModifierOrAnnotationInstanceConverter annotationInstanceConverter;
	private final ToOrdinaryParameterConverter toOrdinaryParameterConverter;
	private final Provider<ToReferenceConverterFromStatement> toReferenceConverterFromStatement;
	private final Provider<ToReferenceConverterFromExpression> toReferenceConverterFromExpression;

	private HashSet<org.emftext.language.java.statements.JumpLabel> currentJumpLabels = new HashSet<>();

	@Inject
	ToSwitchCaseConverter(UtilNamedElement utilNamedElement, ToTypeReferenceConverter toTypeReferenceConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ToExpressionConverter expressionConverterUtility, ToConcreteClassifierConverter classifierConverterUtility,
			ToModifierOrAnnotationInstanceConverter annotationInstanceConverter,
			ToOrdinaryParameterConverter toOrdinaryParameterConverter, StatementsFactory statementsFactory,
			ExpressionsFactory expressionsFactory,
			Provider<ToReferenceConverterFromStatement> toReferenceConverterFromStatement,
			Provider<ToReferenceConverterFromExpression> toReferenceConverterFromExpression) {
		this.expressionsFactory = expressionsFactory;
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.classifierConverterUtility = classifierConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.toReferenceConverterFromStatement = toReferenceConverterFromStatement;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}
	
	org.emftext.language.java.statements.SwitchCase convertToSwitchCase(SwitchCase switchCase) {
		org.emftext.language.java.statements.SwitchCase result = null;
		if (switchCase.isSwitchLabeledRule() && switchCase.isDefault()) {
			result = statementsFactory.createDefaultSwitchRule();
		} else if (switchCase.isSwitchLabeledRule() && !switchCase.isDefault()) {
			org.emftext.language.java.statements.NormalSwitchRule normalRule = statementsFactory
					.createNormalSwitchRule();
			normalRule.setCondition(expressionConverterUtility.convert((Expression) switchCase.expressions().get(0)));
			for (int index = 1; index < switchCase.expressions().size(); index++) {
				Expression expr = (Expression) switchCase.expressions().get(index);
				normalRule.getAdditionalConditions().add(expressionConverterUtility.convert(expr));
			}
			result = normalRule;
		} else if (!switchCase.isSwitchLabeledRule() && switchCase.isDefault()) {
			result = statementsFactory.createDefaultSwitchCase();
		} else { // !switchCase.isSwitchLabeledRule() && !switchCase.isDefault()
			org.emftext.language.java.statements.NormalSwitchCase normalCase = statementsFactory
					.createNormalSwitchCase();
			normalCase.setCondition(expressionConverterUtility.convert((Expression) switchCase.expressions().get(0)));
			for (int index = 1; index < switchCase.expressions().size(); index++) {
				Expression expr = (Expression) switchCase.expressions().get(index);
				normalCase.getAdditionalConditions().add(expressionConverterUtility.convert(expr));
			}
			result = normalCase;
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchCase);
		return result;
	}
	
}
