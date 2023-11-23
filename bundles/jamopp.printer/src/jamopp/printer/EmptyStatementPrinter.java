package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

public class EmptyStatementPrinter {

	static void printEmptyStatement(BufferedWriter writer) throws IOException {
		writer.append(";\n");
	}

}
