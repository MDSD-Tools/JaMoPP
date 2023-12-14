package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.ModifierPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class BlockPrinter implements BlockPrinterInt {

	private final ModifierPrinterInt ModifierPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public BlockPrinter(ModifierPrinterInt modifierPrinter, StatementPrinterInt statementPrinter) {
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
