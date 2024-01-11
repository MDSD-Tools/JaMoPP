package tools.mdsd.jamopp.printer.interfaces;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Printer<E> {

	void print(E element, BufferedWriter writer) throws IOException;

}
