package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.emftext.language.java.containers.JavaRoot;

import com.google.inject.Guice;
import com.google.inject.Injector;

import jamopp.printer.implementation.JavaRootPrinter;
import jamopp.printer.injection.ModulePrinterInjection;

/**
 * This public class provides methods to print JaMoPP model instances.
 */
public final class JaMoPPPrinter {

	private final static JavaRootPrinter javaRootPrinter;

	static {
		Injector injector = Guice.createInjector(new ModulePrinterInjection());
		javaRootPrinter = injector.getInstance(JavaRootPrinter.class);
	}

	/**
	 * Private constructor to avoid instantiation.
	 */
	private JaMoPPPrinter() {
	}

	/**
	 * Prints a model instance into an OutputStream.
	 *
	 * @param root   the model instance to print.
	 * @param output the output for printing.
	 */
	public static void print(JavaRoot root, OutputStream output) {
		try (OutputStreamWriter outWriter = new OutputStreamWriter(output, StandardCharsets.UTF_8);
				BufferedWriter buffWriter = new BufferedWriter(outWriter)) {
			javaRootPrinter.print(root, buffWriter);
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
		try (BufferedWriter writer = Files.newBufferedWriter(file)) {
			javaRootPrinter.print(root, writer);
		} catch (IOException e) {
		}
	}
}
