package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.instantiations.ExplicitConstructorCall;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.instantiations.NewConstructorCallWithInferredTypeArguments;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.InstantiationPrinterInt;

class InstantiationPrinter implements Printer<Instantiation>, InstantiationPrinterInt {

	private final CallTypeArgumentablePrinter CallTypeArgumentablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArgumentablePrinter ArgumentablePrinter;
	private final AnonymousClassPrinter AnonymousClassPrinter;
	private final SelfPrinter SelfPrinter;

	@Inject
	public InstantiationPrinter(jamopp.printer.implementation.CallTypeArgumentablePrinter callTypeArgumentablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			jamopp.printer.implementation.ArgumentablePrinter argumentablePrinter,
			jamopp.printer.implementation.AnonymousClassPrinter anonymousClassPrinter,
			jamopp.printer.implementation.SelfPrinter selfPrinter) {
		super();
		CallTypeArgumentablePrinter = callTypeArgumentablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArgumentablePrinter = argumentablePrinter;
		AnonymousClassPrinter = anonymousClassPrinter;
		SelfPrinter = selfPrinter;
	}

	@Override
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
