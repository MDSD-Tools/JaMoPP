package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.generics.TypeArgument;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class CallTypeArgumentablePrinterImpl implements Printer<CallTypeArgumentable> {

	private final Printer<TypeArgument> TypeArgumentPrinter;

	@Inject
	public CallTypeArgumentablePrinterImpl(Printer<TypeArgument> typeArgumentPrinter) {
		TypeArgumentPrinter = typeArgumentPrinter;
	}

	@Override
	public void print(CallTypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getCallTypeArguments().isEmpty()) {
			writer.append("<");
			TypeArgumentPrinter.print(element.getCallTypeArguments().get(0), writer);
			for (var index = 1; index < element.getCallTypeArguments().size(); index++) {
				writer.append(", ");
				TypeArgumentPrinter.print(element.getCallTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
