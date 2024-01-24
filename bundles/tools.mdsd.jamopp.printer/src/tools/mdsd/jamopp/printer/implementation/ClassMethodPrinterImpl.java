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

	private static final String VALUES = "values";
	private static final String VALUE_OF = "valueOf";
	private static final String JAVA_LANG_STRING = "java.lang.String";

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<ExceptionThrower> exceptionThrowerPrinter;
	private final Printer<Parametrizable> parametrizablePrinter;
	private final Printer<Statement> statementPrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ClassMethodPrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<TypeParametrizable> typeParametrizablePrinter,
			final Printer<TypeReference> typeReferencePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter,
			final Printer<Parametrizable> parametrizablePrinter,
			final Printer<ExceptionThrower> exceptionThrowerPrinter, final Printer<Statement> statementPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.parametrizablePrinter = parametrizablePrinter;
		this.exceptionThrowerPrinter = exceptionThrowerPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final ClassMethod element, final BufferedWriter writer) throws IOException {
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

	private boolean shouldNotBePrinted(final ClassMethod element) {
		boolean returnValue = false;
		if (element.eContainer() instanceof Enumeration) {
			final boolean isStaticAndPublic = checkIfStaticAndPublic(element);
			if (isStaticAndPublic) {
				returnValue = VALUE_OF.equals(element.getName()) && element.getParameters().size() == 1
						&& element.getParameters().get(0).getTypeReference()
								.getTarget() instanceof final tools.mdsd.jamopp.model.java.classifiers.Class cla
						&& JAVA_LANG_STRING.equals(cla.getQualifiedName())
						|| VALUES.equals(element.getName()) && element.getParameters().isEmpty();
			}
		}
		return returnValue;
	}

	private boolean checkIfStaticAndPublic(final ClassMethod element) {
		boolean isStatic = false;
		boolean isPublic = false;
		for (final Modifier m : element.getModifiers()) {
			if (m instanceof Static) {
				isStatic = true;
			} else if (m instanceof Public) {
				isPublic = true;
			}
		}
		return isStatic && isPublic;
	}

}
