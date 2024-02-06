package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.modifiers.Abstract;
import tools.mdsd.jamopp.model.java.modifiers.Default;
import tools.mdsd.jamopp.model.java.modifiers.Final;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.modifiers.Native;
import tools.mdsd.jamopp.model.java.modifiers.Private;
import tools.mdsd.jamopp.model.java.modifiers.Protected;
import tools.mdsd.jamopp.model.java.modifiers.Public;
import tools.mdsd.jamopp.model.java.modifiers.Static;
import tools.mdsd.jamopp.model.java.modifiers.Strictfp;
import tools.mdsd.jamopp.model.java.modifiers.Synchronized;
import tools.mdsd.jamopp.model.java.modifiers.Transient;
import tools.mdsd.jamopp.model.java.modifiers.Volatile;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ModifierPrinterImpl implements Printer<Modifier> {

	private final Map<Class<?>, String> mapping;

	@Inject
	public ModifierPrinterImpl() {
		mapping = new HashMap<>();
		mapping.put(Abstract.class, "abstract ");
		mapping.put(Final.class, "final ");
		mapping.put(Native.class, "native ");
		mapping.put(Protected.class, "protected ");
		mapping.put(Private.class, "private ");
		mapping.put(Public.class, "public ");
		mapping.put(Static.class, "static ");
		mapping.put(Strictfp.class, "strictfp ");
		mapping.put(Synchronized.class, "synchronized ");
		mapping.put(Transient.class, "transient ");
		mapping.put(Volatile.class, "volatile ");
		mapping.put(Default.class, "default ");
	}

	@Override
	public void print(final Modifier element, final BufferedWriter writer) throws IOException {

		for (final Entry<Class<?>, String> entry : mapping.entrySet()) {
			final Class<?> key = entry.getKey();
			final String val = entry.getValue();

			if (key.isInstance(element)) {
				writer.append(val);
			}
		}
	}

}
