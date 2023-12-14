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
import jamopp.printer.interfaces.printer.DefaultSwitchRulePrinterInt;
import jamopp.printer.interfaces.printer.NormalSwitchCasePrinterInt;
import jamopp.printer.interfaces.printer.NormalSwitchRulePrinterInt;
import jamopp.printer.interfaces.printer.SwitchCasePrinterInt;

public class SwitchCasePrinterImpl implements SwitchCasePrinterInt {

	private final Printer<DefaultSwitchCase> DefaultSwitchCasePrinter;
	private final DefaultSwitchRulePrinterInt DefaultSwitchRulePrinter;
	private final NormalSwitchCasePrinterInt NormalSwitchCasePrinter;
	private final NormalSwitchRulePrinterInt NormalSwitchRulePrinter;

	@Inject
	public SwitchCasePrinterImpl(Printer<DefaultSwitchCase> defaultSwitchCasePrinter,
			NormalSwitchCasePrinterInt normalSwitchCasePrinter, DefaultSwitchRulePrinterInt defaultSwitchRulePrinter,
			NormalSwitchRulePrinterInt normalSwitchRulePrinter) {
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
