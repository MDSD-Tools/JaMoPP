package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

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
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class InstantiationPrinterImpl implements Printer<Instantiation> {

	private final Printer<AnonymousClass> anonymousClassPrinter;
	private final Printer<Argumentable> argumentablePrinter;
	private final Printer<CallTypeArgumentable> callTypeArgumentablePrinter;
	private final Printer<Self> selfPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public InstantiationPrinterImpl(final Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			final Printer<TypeReference> typeReferencePrinter, final Printer<TypeArgumentable> typeArgumentablePrinter,
			final Printer<Argumentable> argumentablePrinter, final Printer<AnonymousClass> anonymousClassPrinter,
			final Printer<Self> selfPrinter) {
		this.callTypeArgumentablePrinter = callTypeArgumentablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.argumentablePrinter = argumentablePrinter;
		this.anonymousClassPrinter = anonymousClassPrinter;
		this.selfPrinter = selfPrinter;
	}

	@Override
	public void print(final Instantiation element, final BufferedWriter writer) throws IOException {
		if (element instanceof final NewConstructorCall call) {
			writer.append("new ");
			callTypeArgumentablePrinter.print(call, writer);
			writer.append(" ");
			typeReferencePrinter.print(call.getTypeReference(), writer);
			printTypeArgument(writer, call);
			argumentablePrinter.print(call, writer);
			if (call.getAnonymousClass() != null) {
				anonymousClassPrinter.print(call.getAnonymousClass(), writer);
			}
		} else {
			final ExplicitConstructorCall call = (ExplicitConstructorCall) element;
			callTypeArgumentablePrinter.print(call, writer);
			selfPrinter.print(call.getCallTarget(), writer);
			argumentablePrinter.print(call, writer);
		}
	}

	private void printTypeArgument(final BufferedWriter writer, final NewConstructorCall call) throws IOException {
		if (call instanceof NewConstructorCallWithInferredTypeArguments) {
			writer.append("<>");
		} else {
			typeArgumentablePrinter.print(call, writer);
		}
	}

}
