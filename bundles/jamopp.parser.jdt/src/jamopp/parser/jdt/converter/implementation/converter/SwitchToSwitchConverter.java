package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.Switch;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class SwitchToSwitchConverter implements ToConverter<SwitchStatement, Switch> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;

	@Inject
	SwitchToSwitchConverter(UtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter,
			StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility) {
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
