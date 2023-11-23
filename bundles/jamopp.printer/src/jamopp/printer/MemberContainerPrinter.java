package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.MemberContainer;

public class MemberContainerPrinter {

	static void printMemberContainer(MemberContainer element, BufferedWriter writer) throws IOException {
		for (Member mem : element.getMembers()) {
			MemberPrinter.printMember(mem, writer);
		}
	}

}
