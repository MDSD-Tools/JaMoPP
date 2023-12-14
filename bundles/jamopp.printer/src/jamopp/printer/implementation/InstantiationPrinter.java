package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.instantiations.ExplicitConstructorCall;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.instantiations.NewConstructorCallWithInferredTypeArguments;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnonymousClassPrinterInt;
import jamopp.printer.interfaces.printer.ArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.CallTypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.InstantiationPrinterInt;
import jamopp.printer.interfaces.printer.SelfPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class InstantiationPrinter implements InstantiationPrinterInt {

	private final CallTypeArgumentablePrinterInt CallTypeArgumentablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final ArgumentablePrinterInt ArgumentablePrinter;
	private final AnonymousClassPrinterInt AnonymousClassPrinter;
	private final SelfPrinterInt SelfPrinter;

	@Inject
	public InstantiationPrinter(CallTypeArgumentablePrinterInt callTypeArgumentablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			ArgumentablePrinterInt argumentablePrinter, AnonymousClassPrinterInt anonymousClassPrinter,
			SelfPrinterInt selfPrinter) {
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
