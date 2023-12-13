package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.MemberContainer;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class MemberContainerPrinter implements Printer<MemberContainer> {

	private final MemberPrinter MemberPrinter;

	@Inject
	public MemberContainerPrinter(jamopp.printer.implementation.MemberPrinter memberPrinter) {
		super();
		MemberPrinter = memberPrinter;
	}

	@Override
	public void print(MemberContainer element, BufferedWriter writer) throws IOException {
		for (Member mem : element.getMembers()) {
			MemberPrinter.print(mem, writer);
		}
	}

}
