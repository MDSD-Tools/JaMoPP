package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.DefaultSwitchCase;
import tools.mdsd.jamopp.model.java.statements.DefaultSwitchRule;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchCase;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchRule;
import tools.mdsd.jamopp.model.java.statements.SwitchCase;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SwitchCasePrinterImpl implements Printer<SwitchCase> {

	private final Printer<DefaultSwitchCase> defaultSwitchCasePrinter;
	private final Printer<DefaultSwitchRule> defaultSwitchRulePrinter;
	private final Printer<NormalSwitchCase> normalSwitchCasePrinter;
	private final Printer<NormalSwitchRule> normalSwitchRulePrinter;

	@Inject
	public SwitchCasePrinterImpl(final Printer<DefaultSwitchCase> defaultSwitchCasePrinter,
			final Printer<NormalSwitchCase> normalSwitchCasePrinter,
			final Printer<DefaultSwitchRule> defaultSwitchRulePrinter,
			final Printer<NormalSwitchRule> normalSwitchRulePrinter) {
		this.defaultSwitchCasePrinter = defaultSwitchCasePrinter;
		this.normalSwitchCasePrinter = normalSwitchCasePrinter;
		this.defaultSwitchRulePrinter = defaultSwitchRulePrinter;
		this.normalSwitchRulePrinter = normalSwitchRulePrinter;
	}

	@Override
	public void print(final SwitchCase element, final BufferedWriter writer) throws IOException {
		if (element instanceof DefaultSwitchCase) {
			defaultSwitchCasePrinter.print((DefaultSwitchCase) element, writer);
		} else if (element instanceof NormalSwitchCase) {
			normalSwitchCasePrinter.print((NormalSwitchCase) element, writer);
		} else if (element instanceof DefaultSwitchRule) {
			defaultSwitchRulePrinter.print((DefaultSwitchRule) element, writer);
		} else {
			normalSwitchRulePrinter.print((NormalSwitchRule) element, writer);
		}
	}

}
