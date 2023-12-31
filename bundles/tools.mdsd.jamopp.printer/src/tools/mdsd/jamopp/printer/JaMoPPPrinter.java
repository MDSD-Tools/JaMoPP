package tools.mdsd.jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.inject.Guice;
import com.google.inject.Key;

import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.printer.injection.ModulePrinterInjection;
import tools.mdsd.jamopp.printer.interfaces.Printer;

/**
 * This public class provides methods to print JaMoPP model instances.
 */
public final class JaMoPPPrinter {

	private final static Printer<JavaRoot> JAVA_ROOT_PRINTER;

	static {
		JAVA_ROOT_PRINTER = Guice.createInjector(new ModulePrinterInjection())
				.getInstance(new Key<Printer<JavaRoot>>() {
				});
	}

	/**
	 * Prints a model instance into an OutputStream.
	 *
	 * @param root   the model instance to print.
	 * @param output the output for printing.
	 */
	public static void print(JavaRoot root, OutputStream output) {
		try (var outWriter = new OutputStreamWriter(output, StandardCharsets.UTF_8);
				var buffWriter = new BufferedWriter(outWriter)) {
			JAVA_ROOT_PRINTER.print(root, buffWriter);
		} catch (IOException e) {
		}
	}

	/**
	 * Prints a model instance into a file.
	 *
	 * @param root the model instance to print.
	 * @param file the file for printing.
	 */
	public static void print(JavaRoot root, Path file) {
		try (var writer = Files.newBufferedWriter(file)) {
			JAVA_ROOT_PRINTER.print(root, writer);
		} catch (IOException e) {
		}
	}

	private JaMoPPPrinter() {
		// Private constructor to avoid instantiation.
	}

}
