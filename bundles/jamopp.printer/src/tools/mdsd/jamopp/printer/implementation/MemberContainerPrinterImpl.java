package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.MemberContainer;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MemberContainerPrinterImpl implements Printer<MemberContainer> {

	private final Printer<Member> memberPrinter;

	@Inject
	public MemberContainerPrinterImpl(Printer<Member> memberPrinter) {
		this.memberPrinter = memberPrinter;
	}

	@Override
	public void print(MemberContainer element, BufferedWriter writer) throws IOException {
		for (Member mem : element.getMembers()) {
			this.memberPrinter.print(mem, writer);
		}
	}

}
