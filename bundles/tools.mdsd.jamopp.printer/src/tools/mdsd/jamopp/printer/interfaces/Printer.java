package tools.mdsd.jamopp.printer.interfaces;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Printer<Element> {

	void print(Element element, BufferedWriter writer) throws IOException;

}
