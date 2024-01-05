package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.ExceptionThrower;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.modifiers.Public;
import tools.mdsd.jamopp.model.java.modifiers.Static;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ClassMethodPrinterImpl implements Printer<ClassMethod> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<ExceptionThrower> exceptionThrowerPrinter;
	private final Printer<Parametrizable> parametrizablePrinter;
	private final Printer<Statement> statementPrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ClassMethodPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Parametrizable> parametrizablePrinter,
			Printer<ExceptionThrower> exceptionThrowerPrinter, Printer<Statement> statementPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.parametrizablePrinter = parametrizablePrinter;
		this.exceptionThrowerPrinter = exceptionThrowerPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(ClassMethod element, BufferedWriter writer) throws IOException {
		if (shouldNotBePrinted(element)) {
			return;
		}
		annotableAndModifiablePrinter.print(element, writer);
		typeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		typeReferencePrinter.print(element.getTypeReference(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		parametrizablePrinter.print(element, writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		exceptionThrowerPrinter.print(element, writer);
		writer.append(" ");
		statementPrinter.print(element.getStatement(), writer);
		writer.append("\n");
	}

	private boolean shouldNotBePrinted(ClassMethod element) {
		if (element.eContainer() instanceof Enumeration) {
			var isStatic = false;
			var isPublic = false;
			for (Modifier m : element.getModifiers()) {
				if (m instanceof Static) {
					isStatic = true;
				} else if (m instanceof Public) {
					isPublic = true;
				}
			}
			if (isStatic && isPublic) {
				if ("valueOf".equals(element.getName()) && element.getParameters().size() == 1) {
					var t = element.getParameters().get(0).getTypeReference().getTarget();
					if (t instanceof tools.mdsd.jamopp.model.java.classifiers.Class cla
							&& "java.lang.String".equals(cla.getQualifiedName())) {
						return true;
					}
				} else if ("values".equals(element.getName()) && element.getParameters().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

}
