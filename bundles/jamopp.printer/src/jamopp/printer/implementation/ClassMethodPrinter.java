package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Class;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.modifiers.Public;
import org.emftext.language.java.modifiers.Static;
import org.emftext.language.java.types.Type;

import jamopp.printer.interfaces.Printer;

class ClassMethodPrinter implements Printer<ClassMethod>{

	public void print(ClassMethod element, BufferedWriter writer) throws IOException {
		if (element.eContainer() instanceof Enumeration) {
			boolean isStatic = false;
			boolean isPublic = false;
			for (Modifier m : element.getModifiers()) {
				if (m instanceof Static) {
					isStatic = true;
				} else if (m instanceof Public) {
					isPublic = true;
				}
			}
			if (isStatic && isPublic) {
				if ("valueOf".equals(element.getName()) && element.getParameters().size() == 1) {
					Type t = element.getParameters().get(0).getTypeReference().getTarget();
					if (t instanceof org.emftext.language.java.classifiers.Class cla) {
						if ("java.lang.String".equals(cla.getQualifiedName())) {
							return;
						}
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