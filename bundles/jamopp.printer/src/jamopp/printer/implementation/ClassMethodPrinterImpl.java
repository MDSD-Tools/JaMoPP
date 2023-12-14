package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.ExceptionThrower;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.modifiers.Public;
import org.emftext.language.java.modifiers.Static;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;
import jamopp.printer.interfaces.printer.TypeParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ClassMethodPrinterImpl implements Printer<ClassMethod> {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<ExceptionThrower> ExceptionThrowerPrinter;
	private final ParametrizablePrinterInt ParametrizablePrinter;
	private final StatementPrinterInt StatementPrinter;
	private final TypeParametrizablePrinterInt TypeParametrizablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public ClassMethodPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			TypeParametrizablePrinterInt typeParametrizablePrinter, TypeReferencePrinterInt typeReferencePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, ParametrizablePrinterInt parametrizablePrinter,
			Printer<ExceptionThrower> exceptionThrowerPrinter, StatementPrinterInt statementPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeParametrizablePrinter = typeParametrizablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ParametrizablePrinter = parametrizablePrinter;
		ExceptionThrowerPrinter = exceptionThrowerPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(ClassMethod element, BufferedWriter writer) throws IOException {
		if (element.eContainer() instanceof Enumeration) {
			var isStatic = false;
			var isPublic = false;
			for (Modifier m : element.getModifiers()) {
				if (m instanceof Static) {
					isStatic = true;
				} else if (m instanceof Public) {
					isPublic = true;
				}
			}
			if (isStatic && isPublic) {
				if ("valueOf".equals(element.getName()) && element.getParameters().size() == 1) {
					var t = element.getParameters().get(0).getTypeReference().getTarget();
					if ((t instanceof org.emftext.language.java.classifiers.Class cla) && "java.lang.String".equals(cla.getQualifiedName())) {
						return;
					}
				} else if ("values".equals(element.getName()) && element.getParameters().isEmpty()) {
					return;
				}
			}
		}
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.print(element, writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		ExceptionThrowerPrinter.print(element, writer);
		writer.append(" ");
		StatementPrinter.print(element.getStatement(), writer);
		writer.append("\n");
	}

}
