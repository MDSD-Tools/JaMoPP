package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.GreaterThan;
import org.emftext.language.java.operators.GreaterThanOrEqual;
import org.emftext.language.java.operators.LessThan;
import org.emftext.language.java.operators.LessThanOrEqual;
import org.emftext.language.java.operators.RelationOperator;

import jamopp.printer.interfaces.Printer;

class RelationOperatorPrinter implements Printer<RelationOperator>{

	@Override
	public void print(RelationOperator element, BufferedWriter writer) throws IOException {
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
