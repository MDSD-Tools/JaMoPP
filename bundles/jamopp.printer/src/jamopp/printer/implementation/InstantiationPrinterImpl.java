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

	private final Printer<AnonymousClass> anonymousClassPrinter;
	private final Printer<Argumentable> argumentablePrinter;
	private final Printer<CallTypeArgumentable> callTypeArgumentablePrinter;
	private final Printer<Self> selfPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public InstantiationPrinterImpl(Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<TypeArgumentable> typeArgumentablePrinter,
			Printer<Argumentable> argumentablePrinter, Printer<AnonymousClass> anonymousClassPrinter,
			Printer<Self> selfPrinter) {
		this.callTypeArgumentablePrinter = callTypeArgumentablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.argumentablePrinter = argumentablePrinter;
		this.anonymousClassPrinter = anonymousClassPrinter;
		this.selfPrinter = selfPrinter;
	}

	@Override
	public void print(Instantiation element, BufferedWriter writer) throws IOException {
		if (element instanceof NewConstructorCall call) {
			writer.append("new ");
			this.callTypeArgumentablePrinter.print(call, writer);
			writer.append(" ");
			this.typeReferencePrinter.print(call.getTypeReference(), writer);
			if (call instanceof NewConstructorCallWithInferredTypeArguments) {
				writer.append("<>");
			} else {
				this.typeArgumentablePrinter.print(call, writer);
			}
			this.argumentablePrinter.print(call, writer);
			if (call.getAnonymousClass() != null) {
				this.anonymousClassPrinter.print(call.getAnonymousClass(), writer);
			}
		} else {
			var call = (ExplicitConstructorCall) element;
			this.callTypeArgumentablePrinter.print(call, writer);
			this.selfPrinter.print(call.getCallTarget(), writer);
			this.argumentablePrinter.print(call, writer);
		}
	}

}
