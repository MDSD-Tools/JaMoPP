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

import jamopp.printer.interfaces.Printer;

class ReferencePrinter implements Printer<Reference>{

	private final AnnotationInstancePrinter AnnotationInstancePrinter;
	private final NestedExpressionPrinter NestedExpressionPrinter;
	private final ReflectiveClassReferencePrinter ReflectiveClassReferencePrinter;
	private final PrimitiveTypeReferencePrinter PrimitiveTypeReferencePrinter;
	private final StringReferencePrinter StringReferencePrinter;
	private final SelfReferencePrinter SelfReferencePrinter;
	private final ArrayInstantiationPrinter ArrayInstantiationPrinter;
	private final InstantiationPrinter InstantiationPrinter;
	private final TextBlockReferencePrinter TextBlockReferencePrinter;
	private final ElementReferencePrinter ElementReferencePrinter;
	private final ArraySelectorPrinter ArraySelectorPrinter;
	
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
