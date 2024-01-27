package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Provider;

import org.eclipse.emf.ecore.EObject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

/**
 * Maps a class to a printer. The printer must be able to print an instance of
 * this class. The idea is, that you put a set of mappings in a list, iterate
 * through the list and only print an element, if it is an instance of the
 * class. See e.g. {@link StatementPrinterImpl}.
 *
 * @param <K> Ensures, that the printer can print an instance of the class.
 */
public class Mapping<K> {

	private final Class<K> clazz;
	private final Printer<K> printer;

	public Mapping(final Class<K> clazz, final Printer<K> printer) {
		this.clazz = clazz;
		this.printer = printer;
	}

	public Mapping(final Class<K> clazz, final Provider<Printer<K>> printer) {
		this.clazz = clazz;
		this.printer = printer.get();
	}

	/**
	 * Prints the element in the buffered writer, if the element is an instance of
	 * clazz.
	 *
	 * @param element Element to print
	 * @param writer  Writer in which to print
	 * @return true, if something was printed, otherwise else
	 * @throws IOException if the printer throws an IOException while printing
	 */
	public boolean checkAndPrint(final EObject element, final BufferedWriter writer) throws IOException {
		boolean printed = false;
		if (clazz.isInstance(element)) {
			printer.print(clazz.cast(element), writer);
			printed = true;
		}
		return printed;
	}

}
