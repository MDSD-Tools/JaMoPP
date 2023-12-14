package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayInstantiation;
import org.emftext.language.java.arrays.ArraySelector;
import org.emftext.language.java.expressions.NestedExpression;
import org.emftext.language.java.instantiations.Instantiation;
import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.PrimitiveTypeReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReflectiveClassReference;
import org.emftext.language.java.references.SelfReference;
import org.emftext.language.java.references.StringReference;
import org.emftext.language.java.references.TextBlockReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotationInstancePrinterInt;
import jamopp.printer.interfaces.printer.ArrayInstantiationPrinterInt;
import jamopp.printer.interfaces.printer.ArraySelectorPrinterInt;
import jamopp.printer.interfaces.printer.ElementReferencePrinterInt;
import jamopp.printer.interfaces.printer.InstantiationPrinterInt;
import jamopp.printer.interfaces.printer.NestedExpressionPrinterInt;
import jamopp.printer.interfaces.printer.PrimitiveTypeReferencePrinterInt;
import jamopp.printer.interfaces.printer.ReferencePrinterInt;
import jamopp.printer.interfaces.printer.ReflectiveClassReferencePrinterInt;
import jamopp.printer.interfaces.printer.SelfReferencePrinterInt;
import jamopp.printer.interfaces.printer.StringReferencePrinterInt;
import jamopp.printer.interfaces.printer.TextBlockReferencePrinterInt;

public class ReferencePrinter implements ReferencePrinterInt {

	private final AnnotationInstancePrinterInt AnnotationInstancePrinter;
	private final NestedExpressionPrinterInt NestedExpressionPrinter;
	private final ReflectiveClassReferencePrinterInt ReflectiveClassReferencePrinter;
	private final PrimitiveTypeReferencePrinterInt PrimitiveTypeReferencePrinter;
	private final StringReferencePrinterInt StringReferencePrinter;
	private final SelfReferencePrinterInt SelfReferencePrinter;
	private final ArrayInstantiationPrinterInt ArrayInstantiationPrinter;
	private final InstantiationPrinterInt InstantiationPrinter;
	private final TextBlockReferencePrinterInt TextBlockReferencePrinter;
	private final ElementReferencePrinterInt ElementReferencePrinter;
	private final ArraySelectorPrinterInt ArraySelectorPrinter;

	@Inject
	public ReferencePrinter(AnnotationInstancePrinterInt annotationInstancePrinter,
			NestedExpressionPrinterInt nestedExpressionPrinter,
			ReflectiveClassReferencePrinterInt reflectiveClassReferencePrinter,
			PrimitiveTypeReferencePrinterInt primitiveTypeReferencePrinter,
			StringReferencePrinterInt stringReferencePrinter, SelfReferencePrinterInt selfReferencePrinter,
			ArrayInstantiationPrinterInt arrayInstantiationPrinter, InstantiationPrinterInt instantiationPrinter,
			TextBlockReferencePrinterInt textBlockReferencePrinter, ElementReferencePrinterInt elementReferencePrinter,
			ArraySelectorPrinterInt arraySelectorPrinter) {
		AnnotationInstancePrinter = annotationInstancePrinter;
		NestedExpressionPrinter = nestedExpressionPrinter;
		ReflectiveClassReferencePrinter = reflectiveClassReferencePrinter;
		PrimitiveTypeReferencePrinter = primitiveTypeReferencePrinter;
		StringReferencePrinter = stringReferencePrinter;
		SelfReferencePrinter = selfReferencePrinter;
		ArrayInstantiationPrinter = arrayInstantiationPrinter;
		InstantiationPrinter = instantiationPrinter;
		TextBlockReferencePrinter = textBlockReferencePrinter;
		ElementReferencePrinter = elementReferencePrinter;
		ArraySelectorPrinter = arraySelectorPrinter;
	}

	@Override
	public void print(Reference element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else if (element instanceof NestedExpression) {
			NestedExpressionPrinter.print((NestedExpression) element, writer);
		} else if (element instanceof ReflectiveClassReference) {
			ReflectiveClassReferencePrinter.print(writer);
		} else if (element instanceof PrimitiveTypeReference) {
			PrimitiveTypeReferencePrinter.print((PrimitiveTypeReference) element, writer);
		} else if (element instanceof StringReference) {
			StringReferencePrinter.print((StringReference) element, writer);
		} else if (element instanceof SelfReference) {
			SelfReferencePrinter.print((SelfReference) element, writer);
		} else if (element instanceof ArrayInstantiation) {
			ArrayInstantiationPrinter.print((ArrayInstantiation) element, writer);
		} else if (element instanceof Instantiation) {
			InstantiationPrinter.print((Instantiation) element, writer);
		} else if (element instanceof TextBlockReference) {
			TextBlockReferencePrinter.print((TextBlockReference) element, writer);
		} else {
			ElementReferencePrinter.print((ElementReference) element, writer);
		}
		for (ArraySelector sel : element.getArraySelectors()) {
			ArraySelectorPrinter.print(sel, writer);
		}
		if (element.getNext() != null) {
			writer.append(".");
			print(element.getNext(), writer);
		}
	}

}
