package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.instantiations.ExplicitConstructorCall;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.instantiations.NewConstructorCallWithInferredTypeArguments;

public class InstantiationPrinter {

	static void printInstantiation(Instantiation element, BufferedWriter writer) throws IOException {
		if (element instanceof NewConstructorCall call) {
			writer.append("new ");
			CallTypeArgumentablePrinter.printCallTypeArgumentable(call, writer);
			writer.append(" ");
			TypeReferencePrinter.printTypeReference(call.getTypeReference(), writer);
			if (call instanceof NewConstructorCallWithInferredTypeArguments) {
				writer.append("<>");
			} else {
				TypeArgumentablePrinter.printTypeArgumentable(call, writer);
			}
			ArgumentablePrinter.printArgumentable(call, writer);
			if (call.getAnonymousClass() != null) {
				AnonymousClassPrinter.print(call.getAnonymousClass(), writer);
			}
		} else {
			ExplicitConstructorCall call = (ExplicitConstructorCall) element;
			CallTypeArgumentablePrinter.printCallTypeArgumentable(call, writer);
			SelfPrinter.printSelf(call.getCallTarget(), writer);
			ArgumentablePrinter.printArgumentable(call, writer);
		}
	}

}
