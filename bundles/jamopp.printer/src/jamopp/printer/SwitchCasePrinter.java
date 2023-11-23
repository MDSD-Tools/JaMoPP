package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.NormalSwitchRule;
import org.emftext.language.java.statements.SwitchCase;

public class SwitchCasePrinter {

	static void printSwitchCase(SwitchCase element, BufferedWriter writer) throws IOException {
		if (element instanceof DefaultSwitchCase) {
			DefaultSwitchCasePrinter.printDefaultSwitchCase((DefaultSwitchCase) element, writer);
		} else if (element instanceof NormalSwitchCase) {
			NormalSwitchCasePrinter.printNormalSwitchCase((NormalSwitchCase) element, writer);
		} else if (element instanceof DefaultSwitchRule) {
			DefaultSwitchRulePrinter.printDefaultSwitchRule((DefaultSwitchRule) element, writer);
		} else {
			NormalSwitchRulePrinter.printNormalSwitchRule((NormalSwitchRule) element, writer);
		}
	}

}
