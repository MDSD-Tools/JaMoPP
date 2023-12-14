package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.instantiations.ExplicitConstructorCall;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.instantiations.NewConstructorCallWithInferredTypeArguments;
import org.emftext.language.java.literals.Self;
import org.emftext.language.java.references.Argumentable;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InstantiationPrinterImpl implements Printer<Instantiation> {

	private final Printer<AnonymousClass> AnonymousClassPrinter;
	private final Printer<Argumentable> ArgumentablePrinter;
	private final Printer<CallTypeArgumentable> CallTypeArgumentablePrinter;
	private final Printer<Self> SelfPrinter;
	private final Printer<TypeArgumentable> TypeArgumentablePrinter;
	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public InstantiationPrinterImpl(Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<TypeArgumentable> typeArgumentablePrinter,
			Printer<Argumentable> argumentablePrinter, Printer<AnonymousClass> anonymousClassPrinter,
			Printer<Self> selfPrinter) {
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
			var call = (ExplicitConstructorCall) element;
			CallTypeArgumentablePrinter.print(call, writer);
			SelfPrinter.print(call.getCallTarget(), writer);
			ArgumentablePrinter.print(call, writer);
		}
	}

}
