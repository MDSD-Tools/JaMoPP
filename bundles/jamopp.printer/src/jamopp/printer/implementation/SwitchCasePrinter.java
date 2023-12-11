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

class SwitchCasePrinter implements Printer<SwitchCase> {

	private final DefaultSwitchCasePrinter DefaultSwitchCasePrinter;
	private final NormalSwitchCasePrinter NormalSwitchCasePrinter;
	private final DefaultSwitchRulePrinter DefaultSwitchRulePrinter;
	private final NormalSwitchRulePrinter NormalSwitchRulePrinter;

	@Inject
	public SwitchCasePrinter(jamopp.printer.implementation.DefaultSwitchCasePrinter defaultSwitchCasePrinter,
			jamopp.printer.implementation.NormalSwitchCasePrinter normalSwitchCasePrinter,
			jamopp.printer.implementation.DefaultSwitchRulePrinter defaultSwitchRulePrinter,
			jamopp.printer.implementation.NormalSwitchRulePrinter normalSwitchRulePrinter) {
		super();
		DefaultSwitchCasePrinter = defaultSwitchCasePrinter;
		NormalSwitchCasePrinter = normalSwitchCasePrinter;
		DefaultSwitchRulePrinter = defaultSwitchRulePrinter;
		NormalSwitchRulePrinter = normalSwitchRulePrinter;
	}

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
