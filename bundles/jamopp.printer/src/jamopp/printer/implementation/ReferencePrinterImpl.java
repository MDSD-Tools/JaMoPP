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

	private final Printer<AnnotationInstance> annotationInstancePrinter;
	private final Printer<ArrayInstantiation> arrayInstantiationPrinter;
	private final Printer<ArraySelector> arraySelectorPrinter;
	private final Printer<ElementReference> elementReferencePrinter;
	private final Printer<Instantiation> instantiationPrinter;
	private final Printer<NestedExpression> nestedExpressionPrinter;
	private final Printer<PrimitiveTypeReference> primitiveTypeReferencePrinter;
	private final ReflectiveClassReferencePrinterInt reflectiveClassReferencePrinter;
	private final Printer<SelfReference> selfReferencePrinter;
	private final Printer<StringReference> stringReferencePrinter;
	private final Printer<TextBlockReference> textBlockReferencePrinter;

	@Inject
	public ReferencePrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter,
			Printer<NestedExpression> nestedExpressionPrinter,
			ReflectiveClassReferencePrinterInt reflectiveClassReferencePrinter,
			Printer<PrimitiveTypeReference> primitiveTypeReferencePrinter,
			Printer<StringReference> stringReferencePrinter, Printer<SelfReference> selfReferencePrinter,
			Printer<ArrayInstantiation> arrayInstantiationPrinter, Printer<Instantiation> instantiationPrinter,
			Printer<TextBlockReference> textBlockReferencePrinter, Printer<ElementReference> elementReferencePrinter,
			Printer<ArraySelector> arraySelectorPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.nestedExpressionPrinter = nestedExpressionPrinter;
		this.reflectiveClassReferencePrinter = reflectiveClassReferencePrinter;
		this.primitiveTypeReferencePrinter = primitiveTypeReferencePrinter;
		this.stringReferencePrinter = stringReferencePrinter;
		this.selfReferencePrinter = selfReferencePrinter;
		this.arrayInstantiationPrinter = arrayInstantiationPrinter;
		this.instantiationPrinter = instantiationPrinter;
		this.textBlockReferencePrinter = textBlockReferencePrinter;
		this.elementReferencePrinter = elementReferencePrinter;
		this.arraySelectorPrinter = arraySelectorPrinter;
	}

	@Override
	public void print(Reference element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			this.annotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else if (element instanceof NestedExpression) {
			this.nestedExpressionPrinter.print((NestedExpression) element, writer);
		} else if (element instanceof ReflectiveClassReference) {
			this.reflectiveClassReferencePrinter.print(writer);
		} else if (element instanceof PrimitiveTypeReference) {
			this.primitiveTypeReferencePrinter.print((PrimitiveTypeReference) element, writer);
		} else if (element instanceof StringReference) {
			this.stringReferencePrinter.print((StringReference) element, writer);
		} else if (element instanceof SelfReference) {
			this.selfReferencePrinter.print((SelfReference) element, writer);
		} else if (element instanceof ArrayInstantiation) {
			this.arrayInstantiationPrinter.print((ArrayInstantiation) element, writer);
		} else if (element instanceof Instantiation) {
			this.instantiationPrinter.print((Instantiation) element, writer);
		} else if (element instanceof TextBlockReference) {
			this.textBlockReferencePrinter.print((TextBlockReference) element, writer);
		} else {
			this.elementReferencePrinter.print((ElementReference) element, writer);
		}
		for (ArraySelector sel : element.getArraySelectors()) {
			this.arraySelectorPrinter.print(sel, writer);
		}
		if (element.getNext() != null) {
			writer.append(".");
			print(element.getNext(), writer);
		}
	}

}
