package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Constructor;

public class ConstructorPrinter {

	static void printConstructor(Constructor element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeParametrizablePrinter.printTypeParametrizable(element, writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.printParametrizable(element, writer);
		ExceptionThrowerPrinter.printExceptionThrower(element, writer);
		BlockPrinter.printBlock(element.getBlock(), writer);
	}

}
