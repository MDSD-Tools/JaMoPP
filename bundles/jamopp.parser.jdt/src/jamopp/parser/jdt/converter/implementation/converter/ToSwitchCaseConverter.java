package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToSwitchCaseConverter implements ToConverter<SwitchCase, org.emftext.language.java.statements.SwitchCase> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;

	@Inject
	ToSwitchCaseConverter(StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility) {
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
