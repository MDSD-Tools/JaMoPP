package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.generics.CallTypeArgumentable;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.instantiations.ExplicitConstructorCall;
import tools.mdsd.jamopp.model.java.instantiations.Instantiation;
import tools.mdsd.jamopp.model.java.instantiations.NewConstructorCall;
import tools.mdsd.jamopp.model.java.instantiations.NewConstructorCallWithInferredTypeArguments;
import tools.mdsd.jamopp.model.java.literals.Self;
import tools.mdsd.jamopp.model.java.references.Argumentable;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

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
