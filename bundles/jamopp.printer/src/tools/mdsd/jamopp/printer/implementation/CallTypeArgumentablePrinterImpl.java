package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.generics.TypeArgument;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CallTypeArgumentablePrinterImpl implements Printer<CallTypeArgumentable> {

	private final Printer<TypeArgument> typeArgumentPrinter;

	@Inject
	public CallTypeArgumentablePrinterImpl(Printer<TypeArgument> typeArgumentPrinter) {
		this.typeArgumentPrinter = typeArgumentPrinter;
	}

	@Override
	public void print(CallTypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getCallTypeArguments().isEmpty()) {
			writer.append("<");
			this.typeArgumentPrinter.print(element.getCallTypeArguments().get(0), writer);
			for (var index = 1; index < element.getCallTypeArguments().size(); index++) {
				writer.append(", ");
				this.typeArgumentPrinter.print(element.getCallTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
