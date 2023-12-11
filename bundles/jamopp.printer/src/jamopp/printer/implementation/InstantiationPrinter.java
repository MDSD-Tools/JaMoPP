package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.instantiations.ExplicitConstructorCall;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.instantiations.NewConstructorCallWithInferredTypeArguments;

import jamopp.printer.interfaces.Printer;

class InstantiationPrinter implements Printer<Instantiation>{

	public void print(Instantiation element, BufferedWriter writer) throws IOException {
		if (element instanceof NewConstructorCall call) {
			writer.append("new ");
			CallTypeArgumentablePrinter.print(call, writer);
			writer.append(" ");
			TypeReferencePrinter.print(call.getTypeReference(), writer);
			if (call instanceof NewConstructorCallWithInferredTypeArguments) {
				writer.append("<>");
			} else {
				TypeArgumentablePrinter.print(call, writer);
			}
			ArgumentablePrinter.print(call, writer);
			if (call.getAnonymousClass() != null) {
				AnonymousClassPrinter.print(call.getAnonymousClass(), writer);
			}
		} else {
			ExplicitConstructorCall call = (ExplicitConstructorCall) element;
			CallTypeArgumentablePrinter.print(call, writer);
			SelfPrinter.print(call.getCallTarget(), writer);
			ArgumentablePrinter.print(call, writer);
		}
	}

}
