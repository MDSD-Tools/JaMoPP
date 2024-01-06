package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiation;
import tools.mdsd.jamopp.model.java.arrays.ArraySelector;
import tools.mdsd.jamopp.model.java.expressions.NestedExpression;
import tools.mdsd.jamopp.model.java.instantiations.Instantiation;
import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReflectiveClassReference;
import tools.mdsd.jamopp.model.java.references.SelfReference;
import tools.mdsd.jamopp.model.java.references.StringReference;
import tools.mdsd.jamopp.model.java.references.TextBlockReference;
import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ReferencePrinterImpl implements Printer<Reference> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;
	private final Printer<ArrayInstantiation> arrayInstantiationPrinter;
	private final Printer<ArraySelector> arraySelectorPrinter;
	private final Printer<ElementReference> elementReferencePrinter;
	private final Printer<Instantiation> instantiationPrinter;
	private final Printer<NestedExpression> nestedExpressionPrinter;
	private final Printer<PrimitiveTypeReference> primitiveTypeReferencePrinter;
	private final EmptyPrinter reflectiveClassReferencePrinter;
	private final Printer<SelfReference> selfReferencePrinter;
	private final Printer<StringReference> stringReferencePrinter;
	private final Printer<TextBlockReference> textBlockReferencePrinter;
	private final List<Mapping<?>> mappings;

	@Inject
	public ReferencePrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter,
			Printer<NestedExpression> nestedExpressionPrinter,
			@Named("ReflectiveClassReferencePrinter") EmptyPrinter reflectiveClassReferencePrinter,
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
		mappings = new ArrayList<>();
	}

	@Override
	public void print(Reference element, BufferedWriter writer) throws IOException {

		if (mappings.isEmpty()) {
			mappings.add(new Mapping<>(AnnotationInstance.class, annotationInstancePrinter));
			mappings.add(new Mapping<>(NestedExpression.class, nestedExpressionPrinter));
			mappings.add(new Mapping<>(PrimitiveTypeReference.class, primitiveTypeReferencePrinter));
			mappings.add(new Mapping<>(StringReference.class, stringReferencePrinter));
			mappings.add(new Mapping<>(SelfReference.class, selfReferencePrinter));
			mappings.add(new Mapping<>(ArrayInstantiation.class, arrayInstantiationPrinter));
			mappings.add(new Mapping<>(Instantiation.class, instantiationPrinter));
			mappings.add(new Mapping<>(TextBlockReference.class, textBlockReferencePrinter));
			mappings.add(new Mapping<>(ElementReference.class, elementReferencePrinter));
		}

		boolean printed = false;
		for (Mapping<?> mapping : mappings) {
			printed = mapping.checkAndPrint(element, writer);
			if (printed) {
				break;
			}
		}

		if (element instanceof ReflectiveClassReference && !printed) {
			reflectiveClassReferencePrinter.print(writer);
		}

		for (ArraySelector sel : element.getArraySelectors()) {
			arraySelectorPrinter.print(sel, writer);
		}
		if (element.getNext() != null) {
			writer.append(".");
			print(element.getNext(), writer);
		}
	}

}
