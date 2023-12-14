package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.NormalSwitchRule;
import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class SwitchCasePrinterImpl implements Printer<SwitchCase> {

	private final Printer<DefaultSwitchCase> DefaultSwitchCasePrinter;
	private final Printer<DefaultSwitchRule> DefaultSwitchRulePrinter;
	private final Printer<NormalSwitchCase> NormalSwitchCasePrinter;
	private final Printer<NormalSwitchRule> NormalSwitchRulePrinter;

	@Inject
	public SwitchCasePrinterImpl(Printer<DefaultSwitchCase> defaultSwitchCasePrinter,
			Printer<NormalSwitchCase> normalSwitchCasePrinter, Printer<DefaultSwitchRule> defaultSwitchRulePrinter,
			Printer<NormalSwitchRule> normalSwitchRulePrinter) {
		DefaultSwitchCasePrinter = defaultSwitchCasePrinter;
		NormalSwitchCasePrinter = normalSwitchCasePrinter;
		DefaultSwitchRulePrinter = defaultSwitchRulePrinter;
		NormalSwitchRulePrinter = normalSwitchRulePrinter;
	}

	@Override
	public void print(SwitchCase element, BufferedWriter writer) throws IOException {
		if (element instanceof DefaultSwitchCase) {
			DefaultSwitchCasePrinter.print((DefaultSwitchCase) element, writer);
		} else if (element instanceof NormalSwitchCase) {
			NormalSwitchCasePrinter.print((NormalSwitchCase) element, writer);
		} else if (element instanceof DefaultSwitchRule) {
			DefaultSwitchRulePrinter.print((DefaultSwitchRule) element, writer);
		} else {
			NormalSwitchRulePrinter.print((NormalSwitchRule) element, writer);
		}
	}

}
