package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.literals.Self;
import org.emftext.language.java.literals.Super;
import org.emftext.language.java.literals.This;

public class SelfPrinter {

	static void printSelf(Self element, BufferedWriter writer) throws IOException {
		if (element instanceof This) {
			writer.append("this");
		} else if (element instanceof Super) {
			writer.append("super");
		}
	}

}
