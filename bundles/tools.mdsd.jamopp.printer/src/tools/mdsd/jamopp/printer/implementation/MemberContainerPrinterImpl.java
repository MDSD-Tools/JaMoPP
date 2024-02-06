package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MemberContainerPrinterImpl implements Printer<MemberContainer> {

	private final Printer<Member> memberPrinter;

	@Inject
	public MemberContainerPrinterImpl(final Printer<Member> memberPrinter) {
		this.memberPrinter = memberPrinter;
	}

	@Override
	public void print(final MemberContainer element, final BufferedWriter writer) throws IOException {
		for (final Member mem : element.getMembers()) {
			memberPrinter.print(mem, writer);
		}
	}

}
