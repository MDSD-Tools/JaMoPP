package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchStatement;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.statements.Switch;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToSwitchCasesAndSetConverter;

public class SwitchToSwitchConverter implements Converter<SwitchStatement, Switch> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	SwitchToSwitchConverter(UtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter,
			StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toSwitchCasesAndSetConverter = toSwitchCasesAndSetConverter;
	}

	@Override
	public Switch convert(SwitchStatement switchSt) {
		tools.mdsd.jamopp.model.java.statements.Switch result = statementsFactory.createSwitch();
		result.setVariable(expressionConverterUtility.convert(switchSt.getExpression()));
		toSwitchCasesAndSetConverter.convert(result, switchSt.statements());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchSt);
		return result;
	}

}
