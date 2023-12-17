package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.Assignment;
import tools.mdsd.jamopp.model.java.operators.AssignmentAnd;
import tools.mdsd.jamopp.model.java.operators.AssignmentDivision;
import tools.mdsd.jamopp.model.java.operators.AssignmentExclusiveOr;
import tools.mdsd.jamopp.model.java.operators.AssignmentLeftShift;
import tools.mdsd.jamopp.model.java.operators.AssignmentMinus;
import tools.mdsd.jamopp.model.java.operators.AssignmentModulo;
import tools.mdsd.jamopp.model.java.operators.AssignmentMultiplication;
import tools.mdsd.jamopp.model.java.operators.AssignmentOperator;
import tools.mdsd.jamopp.model.java.operators.AssignmentOr;
import tools.mdsd.jamopp.model.java.operators.AssignmentPlus;
import tools.mdsd.jamopp.model.java.operators.AssignmentRightShift;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AssignmentOperatorPrinterImpl implements Printer<AssignmentOperator> {

	@Override
	public void print(AssignmentOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Assignment) {
			writer.append(" = ");
		} else if (element instanceof AssignmentAnd) {
			writer.append(" &= ");
		} else if (element instanceof AssignmentDivision) {
			writer.append(" /= ");
		} else if (element instanceof AssignmentExclusiveOr) {
			writer.append(" ^= ");
		} else if (element instanceof AssignmentMinus) {
			writer.append(" -= ");
		} else if (element instanceof AssignmentModulo) {
			writer.append(" %= ");
		} else if (element instanceof AssignmentMultiplication) {
			writer.append(" *= ");
		} else if (element instanceof AssignmentLeftShift) {
			writer.append(" <<= ");
		} else if (element instanceof AssignmentOr) {
			writer.append(" |= ");
		} else if (element instanceof AssignmentPlus) {
			writer.append(" += ");
		} else if (element instanceof AssignmentRightShift) {
			writer.append(" >>= ");
		} else {
			writer.append(" >>>= ");
		}
	}

}
