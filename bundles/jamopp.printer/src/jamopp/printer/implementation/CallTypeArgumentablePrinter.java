package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.CallTypeArgumentable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class CallTypeArgumentablePrinter implements Printer<CallTypeArgumentable> {

	private final TypeArgumentPrinter TypeArgumentPrinter;

	@Inject
	public CallTypeArgumentablePrinter(jamopp.printer.implementation.TypeArgumentPrinter typeArgumentPrinter) {
		super();
		TypeArgumentPrinter = typeArgumentPrinter;
	}

	public void print(CallTypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getCallTypeArguments().isEmpty()) {
			writer.append("<");
			TypeArgumentPrinter.print(element.getCallTypeArguments().get(0), writer);
			for (int index = 1; index < element.getCallTypeArguments().size(); index++) {
				writer.append(", ");
				TypeArgumentPrinter.print(element.getCallTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
