package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class BlockPrinterImpl implements Printer<Block> {

	private final Printer<Modifier> modifierPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public BlockPrinterImpl(final Printer<Modifier> modifierPrinter, final Printer<Statement> statementPrinter) {
		this.modifierPrinter = modifierPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final Block element, final BufferedWriter writer) throws IOException {
		for (final Modifier m : element.getModifiers()) {
			modifierPrinter.print(m, writer);
		}
		writer.append("{\n");
		for (final Statement s : element.getStatements()) {
			statementPrinter.print(s, writer);
		}
		writer.append("}\n");
	}

}
