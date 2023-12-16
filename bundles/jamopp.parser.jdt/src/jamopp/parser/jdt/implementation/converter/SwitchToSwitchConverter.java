package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.Switch;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilToSwitchCasesAndSetConverter;

public class SwitchToSwitchConverter implements Converter<SwitchStatement, Switch> {

	private final StatementsFactory statementsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;

	@Inject
	SwitchToSwitchConverter(IUtilToSwitchCasesAndSetConverter toSwitchCasesAndSetConverter,
			StatementsFactory statementsFactory, IUtilLayout layoutInformationConverter,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility) {
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
