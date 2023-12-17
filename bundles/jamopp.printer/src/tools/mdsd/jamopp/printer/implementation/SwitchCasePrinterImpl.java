package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.NormalSwitchRule;
import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SwitchCasePrinterImpl implements Printer<SwitchCase> {

	private final Printer<DefaultSwitchCase> defaultSwitchCasePrinter;
	private final Printer<DefaultSwitchRule> defaultSwitchRulePrinter;
	private final Printer<NormalSwitchCase> normalSwitchCasePrinter;
	private final Printer<NormalSwitchRule> normalSwitchRulePrinter;

	@Inject
	public SwitchCasePrinterImpl(Printer<DefaultSwitchCase> defaultSwitchCasePrinter,
			Printer<NormalSwitchCase> normalSwitchCasePrinter, Printer<DefaultSwitchRule> defaultSwitchRulePrinter,
			Printer<NormalSwitchRule> normalSwitchRulePrinter) {
		this.defaultSwitchCasePrinter = defaultSwitchCasePrinter;
		this.normalSwitchCasePrinter = normalSwitchCasePrinter;
		this.defaultSwitchRulePrinter = defaultSwitchRulePrinter;
		this.normalSwitchRulePrinter = normalSwitchRulePrinter;
	}

	@Override
	public void print(SwitchCase element, BufferedWriter writer) throws IOException {
		if (element instanceof DefaultSwitchCase) {
			this.defaultSwitchCasePrinter.print((DefaultSwitchCase) element, writer);
		} else if (element instanceof NormalSwitchCase) {
			this.normalSwitchCasePrinter.print((NormalSwitchCase) element, writer);
		} else if (element instanceof DefaultSwitchRule) {
			this.defaultSwitchRulePrinter.print((DefaultSwitchRule) element, writer);
		} else {
			this.normalSwitchRulePrinter.print((NormalSwitchRule) element, writer);
		}
	}

}
