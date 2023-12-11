package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.EmptyMember;

import jamopp.printer.interfaces.EmptyPrinter;
import jamopp.printer.interfaces.Printer;

class EmptyMemberPrinter implements EmptyPrinter{

	public void print(BufferedWriter writer) throws IOException {
		writer.append(";\n\n");
	}

}
