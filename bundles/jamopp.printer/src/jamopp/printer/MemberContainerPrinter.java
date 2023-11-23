package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.MemberContainer;

class MemberContainerPrinter {

	static void print(MemberContainer element, BufferedWriter writer) throws IOException {
		for (Member mem : element.getMembers()) {
			MemberPrinter.print(mem, writer);
		}
	}

}
