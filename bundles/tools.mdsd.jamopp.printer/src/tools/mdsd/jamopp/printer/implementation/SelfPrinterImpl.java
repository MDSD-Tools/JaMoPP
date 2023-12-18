package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.literals.Self;
import tools.mdsd.jamopp.model.java.literals.Super;
import tools.mdsd.jamopp.model.java.literals.This;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SelfPrinterImpl implements Printer<Self> {

	@Override
	public void print(Self element, BufferedWriter writer) throws IOException {
		if (element instanceof This) {
			writer.append("this");
		} else if (element instanceof Super) {
			writer.append("super");
		}
	}

}
