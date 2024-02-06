package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;

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

	private final Map<Class<?>, String> mapping;

	@Inject
	public AssignmentOperatorPrinterImpl() {
		mapping = new HashMap<>();
		mapping.put(Assignment.class, " = ");
		mapping.put(AssignmentAnd.class, " &= ");
		mapping.put(AssignmentDivision.class, " /= ");
		mapping.put(AssignmentExclusiveOr.class, " ^= ");
		mapping.put(AssignmentMinus.class, " -= ");
		mapping.put(AssignmentModulo.class, " %= ");
		mapping.put(AssignmentMultiplication.class, " *= ");
		mapping.put(AssignmentLeftShift.class, " <<= ");
		mapping.put(AssignmentOr.class, " |= ");
		mapping.put(AssignmentPlus.class, " += ");
		mapping.put(AssignmentRightShift.class, " >>= ");
	}

	@Override
	public void print(final AssignmentOperator element, final BufferedWriter writer) throws IOException {
		for (final Entry<Class<?>, String> entry : mapping.entrySet()) {
			if (entry.getKey().isInstance(element)) {
				writer.append(entry.getValue());
				return;
			}
		}
		// else
		writer.append(" >>>= ");
	}

}
