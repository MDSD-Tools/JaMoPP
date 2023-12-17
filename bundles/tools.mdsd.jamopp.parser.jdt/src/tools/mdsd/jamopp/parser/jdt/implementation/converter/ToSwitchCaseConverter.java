package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToSwitchCaseConverter implements Converter<SwitchCase, tools.mdsd.jamopp.model.java.statements.SwitchCase> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	ToSwitchCaseConverter(StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.SwitchCase convert(SwitchCase switchCase) {
		tools.mdsd.jamopp.model.java.statements.SwitchCase result = null;
		if (switchCase.isSwitchLabeledRule() && switchCase.isDefault()) {
			result = statementsFactory.createDefaultSwitchRule();
		} else if (switchCase.isSwitchLabeledRule() && !switchCase.isDefault()) {
			tools.mdsd.jamopp.model.java.statements.NormalSwitchRule normalRule = statementsFactory
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
			tools.mdsd.jamopp.model.java.statements.NormalSwitchCase normalCase = statementsFactory
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
