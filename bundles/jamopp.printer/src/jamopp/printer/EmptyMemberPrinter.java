package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

public class EmptyMemberPrinter {

	static void printEmptyMember(BufferedWriter writer) throws IOException {
		writer.append(";\n\n");
	}

}
