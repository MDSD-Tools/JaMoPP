package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

class EmptyStatementPrinter {

	static void print(BufferedWriter writer) throws IOException {
		writer.append(";\n");
	}

}
