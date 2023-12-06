package jamopp.parser.jdt.injector;

import com.google.inject.AbstractModule;
import jamopp.parser.jdt.converter.implementation.converter.StatementToStatementConverterImpl;
import jamopp.parser.jdt.converter.implementation.converter.ToConcreteClassifierConverterImpl;
import jamopp.parser.jdt.converter.implementation.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.implementation.helper.UtilToSwitchCasesAndSetConverterImpl;
import jamopp.parser.jdt.converter.implementation.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.converter.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class InjectorGuice extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		bind(StatementToStatementConverter.class).to(StatementToStatementConverterImpl.class);
		bind(ToConcreteClassifierConverter.class).to(ToConcreteClassifierConverterImpl.class);
		bind(UtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverterImpl.class);
		bind(IUtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparation.class);
	}

}
