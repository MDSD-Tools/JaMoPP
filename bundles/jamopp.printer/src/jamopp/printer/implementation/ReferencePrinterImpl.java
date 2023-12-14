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
import jamopp.printer.interfaces.ReflectiveClassReferencePrinterInt;

public class ReferencePrinterImpl implements Printer<Reference> {

	private final Printer<AnnotationInstance> AnnotationInstancePrinter;
	private final Printer<ArrayInstantiation> ArrayInstantiationPrinter;
	private final Printer<ArraySelector> ArraySelectorPrinter;
	private final Printer<ElementReference> ElementReferencePrinter;
	private final Printer<Instantiation> InstantiationPrinter;
	private final Printer<NestedExpression> NestedExpressionPrinter;
	private final Printer<PrimitiveTypeReference> PrimitiveTypeReferencePrinter;
	private final ReflectiveClassReferencePrinterInt ReflectiveClassReferencePrinter;
	private final Printer<SelfReference> SelfReferencePrinter;
	private final Printer<StringReference> StringReferencePrinter;
	private final Printer<TextBlockReference> TextBlockReferencePrinter;

	@Inject
	public ReferencePrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter,
			Printer<NestedExpression> nestedExpressionPrinter,
			ReflectiveClassReferencePrinterInt reflectiveClassReferencePrinter,
			Printer<PrimitiveTypeReference> primitiveTypeReferencePrinter,
			Printer<StringReference> stringReferencePrinter, Printer<SelfReference> selfReferencePrinter,
			Printer<ArrayInstantiation> arrayInstantiationPrinter, Printer<Instantiation> instantiationPrinter,
			Printer<TextBlockReference> textBlockReferencePrinter, Printer<ElementReference> elementReferencePrinter,
			Printer<ArraySelector> arraySelectorPrinter) {
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
