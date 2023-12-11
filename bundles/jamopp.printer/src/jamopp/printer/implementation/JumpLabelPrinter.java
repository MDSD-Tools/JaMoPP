package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.JumpLabel;

import jamopp.printer.interfaces.Printer;

class JumpLabelPrinter implements Printer<JumpLabel>{

	public void print(JumpLabel element, BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		StatementPrinter.print(element.getStatement(), writer);
	}

}