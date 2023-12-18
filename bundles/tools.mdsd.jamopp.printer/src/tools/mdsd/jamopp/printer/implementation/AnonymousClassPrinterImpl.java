package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.members.MemberContainer;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnonymousClassPrinterImpl implements Printer<AnonymousClass> {

	private final Printer<MemberContainer> memberContainerPrinter;

	@Inject
	public AnonymousClassPrinterImpl(Printer<MemberContainer> memberContainerPrinter) {
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(AnonymousClass element, BufferedWriter writer) throws IOException {
		writer.append("{\n");
		this.memberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
