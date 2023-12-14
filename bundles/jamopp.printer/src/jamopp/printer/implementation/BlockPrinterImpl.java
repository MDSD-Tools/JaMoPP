package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class BlockPrinterImpl implements Printer<Block> {

	private final Printer<Modifier> ModifierPrinter;
	private final Printer<Statement> StatementPrinter;

	@Inject
	public BlockPrinterImpl(Printer<Modifier> modifierPrinter, Printer<Statement> statementPrinter) {
		ModifierPrinter = modifierPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
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
