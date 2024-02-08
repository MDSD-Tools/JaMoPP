package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.GreaterThan;
import tools.mdsd.jamopp.model.java.operators.GreaterThanOrEqual;
import tools.mdsd.jamopp.model.java.operators.LessThan;
import tools.mdsd.jamopp.model.java.operators.LessThanOrEqual;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class RelationOperatorPrinterImpl implements Printer<RelationOperator> {

	@Override
	public void print(final RelationOperator element, final BufferedWriter writer) throws IOException {
		if (element instanceof GreaterThan) {
			writer.append(" > ");
		} else if (element instanceof GreaterThanOrEqual) {
			writer.append(" >= ");
		} else if (element instanceof LessThan) {
			writer.append(" < ");
		} else if (element instanceof LessThanOrEqual) {
			writer.append(" <= ");
		}
	}

}
