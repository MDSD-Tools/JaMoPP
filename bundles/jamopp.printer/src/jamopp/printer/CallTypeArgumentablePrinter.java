package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.CallTypeArgumentable;

class CallTypeArgumentablePrinter {

	static void print(CallTypeArgumentable element, BufferedWriter writer)
			throws IOException {
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
