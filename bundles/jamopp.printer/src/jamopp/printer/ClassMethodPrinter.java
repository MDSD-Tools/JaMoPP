package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Class;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.modifiers.Public;
import org.emftext.language.java.modifiers.Static;
import org.emftext.language.java.types.Type;

public class ClassMethodPrinter {

	static void printClassMethod(ClassMethod element, BufferedWriter writer) throws IOException {
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
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		TypeParametrizablePrinter.printTypeParametrizable(element, writer);
		writer.append(" ");
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.printParametrizable(element, writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
		ExceptionThrowerPrinter.printExceptionThrower(element, writer);
		writer.append(" ");
		StatementPrinter.printStatement(element.getStatement(), writer);
		writer.append("\n");
	}

}
