package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.instantiations.ExplicitConstructorCall;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.instantiations.NewConstructorCallWithInferredTypeArguments;
import org.emftext.language.java.references.Argumentable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnonymousClassPrinterInt;
import jamopp.printer.interfaces.printer.ArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.CallTypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.InstantiationPrinterInt;
import jamopp.printer.interfaces.printer.SelfPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class InstantiationPrinterImpl implements InstantiationPrinterInt {

	private final Printer<CallTypeArgumentable> CallTypeArgumentablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final Printer<Argumentable> ArgumentablePrinter;
	private final Printer<AnonymousClass> AnonymousClassPrinter;
	private final SelfPrinterInt SelfPrinter;

	@Inject
	public InstantiationPrinterImpl(Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			Printer<Argumentable> argumentablePrinter, Printer<AnonymousClass> anonymousClassPrinter,
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
