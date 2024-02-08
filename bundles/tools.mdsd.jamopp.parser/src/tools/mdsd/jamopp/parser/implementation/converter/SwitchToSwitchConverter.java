package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchStatement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToSwitchCasesAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class SwitchToSwitchConverter implements Converter<SwitchStatement, Switch> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public SwitchToSwitchConverter(final ToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter,
			final StatementsFactory statementsFactory, final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toSwitchCasesAndSetConverter = toSwitchCasesAndSetConverter;
	}

	@Override
	public Switch convert(final SwitchStatement switchSt) {
		final Switch result = statementsFactory.createSwitch();
		result.setVariable(expressionConverterUtility.convert(switchSt.getExpression()));
		toSwitchCasesAndSetConverter.convert(result, switchSt.statements());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchSt);
		return result;
	}

}
