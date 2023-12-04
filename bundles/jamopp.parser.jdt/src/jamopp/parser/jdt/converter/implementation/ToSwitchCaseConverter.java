package jamopp.parser.jdt.converter.implementation;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.helper.ToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToSwitchCaseConverter implements ToConverter<SwitchCase, org.emftext.language.java.statements.SwitchCase> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter expressionConverterUtility;

	@Inject
	ToSwitchCaseConverter(StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			ToExpressionConverter expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	@Override
	public org.emftext.language.java.statements.SwitchCase convert(SwitchCase switchCase) {
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