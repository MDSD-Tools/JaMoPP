package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.AnonymousClass;

public class AnonymousClassPrinter {

	static void printAnonymousClass(AnonymousClass element, BufferedWriter writer) throws IOException {
		writer.append("{\n");
		MemberContainerPrinter.printMemberContainer(element, writer);
		writer.append("}\n");
	}

}