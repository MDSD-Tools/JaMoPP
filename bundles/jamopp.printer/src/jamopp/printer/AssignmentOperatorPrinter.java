package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Assignment;
import org.emftext.language.java.operators.AssignmentAnd;
import org.emftext.language.java.operators.AssignmentDivision;
import org.emftext.language.java.operators.AssignmentExclusiveOr;
import org.emftext.language.java.operators.AssignmentLeftShift;
import org.emftext.language.java.operators.AssignmentMinus;
import org.emftext.language.java.operators.AssignmentModulo;
import org.emftext.language.java.operators.AssignmentMultiplication;
import org.emftext.language.java.operators.AssignmentOperator;
import org.emftext.language.java.operators.AssignmentOr;
import org.emftext.language.java.operators.AssignmentPlus;
import org.emftext.language.java.operators.AssignmentRightShift;

public class AssignmentOperatorPrinter {

	static void printAssignmentOperator(AssignmentOperator element, BufferedWriter writer) throws IOException {
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
