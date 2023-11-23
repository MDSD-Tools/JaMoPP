package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

class EmptyMemberPrinter {

	static void print(BufferedWriter writer) throws IOException {
		writer.append(";\n\n");
	}

}
