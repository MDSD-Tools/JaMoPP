package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.literals.Self;
import org.emftext.language.java.literals.Super;
import org.emftext.language.java.literals.This;

import jamopp.printer.interfaces.printer.SelfPrinterInt;

public class SelfPrinter implements SelfPrinterInt{

	@Override
	public void print(Self element, BufferedWriter writer) throws IOException {
		if (element instanceof This) {
			writer.append("this");
		} else if (element instanceof Super) {
			writer.append("super");
		}
	}

}
