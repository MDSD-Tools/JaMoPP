package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class PrimitiveTypePrinterImpl implements Printer<PrimitiveType> {

	private final Printer<Annotable> annotablePrinter;
	private final Map<Class<?>, String> mappings;

	@Inject
	public PrimitiveTypePrinterImpl(final Printer<Annotable> annotablePrinter) {
		this.annotablePrinter = annotablePrinter;
		mappings = new HashMap<>();
		mappings.put(tools.mdsd.jamopp.model.java.types.Boolean.class, "boolean");
		mappings.put(tools.mdsd.jamopp.model.java.types.Byte.class, "byte");
		mappings.put(tools.mdsd.jamopp.model.java.types.Char.class, "char");
		mappings.put(tools.mdsd.jamopp.model.java.types.Double.class, "double");
		mappings.put(tools.mdsd.jamopp.model.java.types.Float.class, "float");
		mappings.put(tools.mdsd.jamopp.model.java.types.Int.class, "int");
		mappings.put(tools.mdsd.jamopp.model.java.types.Long.class, "long");
		mappings.put(tools.mdsd.jamopp.model.java.types.Short.class, "short");
		mappings.put(tools.mdsd.jamopp.model.java.types.Void.class, "void");
	}

	@Override
	public void print(final PrimitiveType element, final BufferedWriter writer) throws IOException {
		annotablePrinter.print(element, writer);
		for (final Entry<Class<?>, String> entry : mappings.entrySet()) {
			final Class<?> key = entry.getKey();
			final String val = entry.getValue();
			if (key.isInstance(element)) {
				writer.append(val);
				break;
			}
		}
	}

}
