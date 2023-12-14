package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.AnonymousClass;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AnonymousClassPrinterInt;
import jamopp.printer.interfaces.printer.MemberContainerPrinterInt;

public class AnonymousClassPrinterImpl implements AnonymousClassPrinterInt {

	private final MemberContainerPrinterInt MemberContainerPrinter;

	@Inject
	public AnonymousClassPrinterImpl(MemberContainerPrinterInt memberContainerPrinter) {
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(AnonymousClass element, BufferedWriter writer) throws IOException {
		writer.append("{\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
