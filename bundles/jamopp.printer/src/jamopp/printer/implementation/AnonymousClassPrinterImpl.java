package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.MemberContainer;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AnonymousClassPrinterImpl implements Printer<AnonymousClass> {

	private final Printer<MemberContainer> MemberContainerPrinter;

	@Inject
	public AnonymousClassPrinterImpl(Printer<MemberContainer> memberContainerPrinter) {
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(AnonymousClass element, BufferedWriter writer) throws IOException {
		writer.append("{\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
