package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.SwitchStatement;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.Switch;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class SwitchToSwitchConverter implements ToConverter<SwitchStatement, Switch> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter expressionConverterUtility;
	private final UtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter;

	@Inject
	SwitchToSwitchConverter(UtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter,
			StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			ToExpressionConverter expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toSwitchCasesAndSetConverter = toSwitchCasesAndSetConverter;
	}

	@Override
	public Switch convert(SwitchStatement switchSt) {
		org.emftext.language.java.statements.Switch result = statementsFactory.createSwitch();
		result.setVariable(expressionConverterUtility.convert(switchSt.getExpression()));
		toSwitchCasesAndSetConverter.convert(result, switchSt.statements());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchSt);
		return result;
	}

}
