package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Statement;

import jamopp.printer.interfaces.Printer;

class BlockPrinter implements Printer<Block>{

	private final ModifierPrinter ModifierPrinter;
	private final StatementPrinter StatementPrinter;
	
	public void print(Block element, BufferedWriter writer) throws IOException {
		for (Modifier m : element.getModifiers()) {
			ModifierPrinter.print(m, writer);
		}
		writer.append("{\n");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
		writer.append("}\n");
	}

}
