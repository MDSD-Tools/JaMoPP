package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.statements.Block;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.ClassMethodPrinterInt;
import jamopp.printer.interfaces.printer.ConcreteClassifierPrinterInt;
import jamopp.printer.interfaces.printer.ConstructorPrinterInt;
import jamopp.printer.interfaces.printer.EmptyMemberPrinterInt;
import jamopp.printer.interfaces.printer.FieldPrinterInt;
import jamopp.printer.interfaces.printer.InterfaceMethodPrinterInt;
import jamopp.printer.interfaces.printer.MemberPrinterInt;

public class MemberPrinter implements MemberPrinterInt {

	private final FieldPrinterInt FieldPrinter;
	private final ConstructorPrinterInt ConstructorPrinter;
	private final ClassMethodPrinterInt ClassMethodPrinter;
	private final InterfaceMethodPrinterInt InterfaceMethodPrinter;
	private final ConcreteClassifierPrinterInt ConcreteClassifierPrinter;
	private final BlockPrinterInt BlockPrinter;
	private final EmptyMemberPrinterInt EmptyMemberPrinter;

	@Inject
	public MemberPrinter(FieldPrinterInt fieldPrinter, ConstructorPrinterInt constructorPrinter,
			ClassMethodPrinterInt classMethodPrinter, InterfaceMethodPrinterInt interfaceMethodPrinter,
			ConcreteClassifierPrinterInt concreteClassifierPrinter, BlockPrinterInt blockPrinter,
			EmptyMemberPrinterInt emptyMemberPrinter) {
		FieldPrinter = fieldPrinter;
		ConstructorPrinter = constructorPrinter;
		ClassMethodPrinter = classMethodPrinter;
		InterfaceMethodPrinter = interfaceMethodPrinter;
		ConcreteClassifierPrinter = concreteClassifierPrinter;
		BlockPrinter = blockPrinter;
		EmptyMemberPrinter = emptyMemberPrinter;
	}

	@Override
	public void print(Member element, BufferedWriter writer) throws IOException {
		if (element instanceof Field) {
			FieldPrinter.print((Field) element, writer);
		} else if (element instanceof Constructor) {
			ConstructorPrinter.print((Constructor) element, writer);
		} else if (element instanceof ClassMethod) {
			ClassMethodPrinter.print((ClassMethod) element, writer);
		} else if (element instanceof InterfaceMethod) {
			InterfaceMethodPrinter.print((InterfaceMethod) element, writer);
		} else if (element instanceof ConcreteClassifier) {
			ConcreteClassifierPrinter.print((ConcreteClassifier) element, writer);
		} else if (element instanceof Block) {
			BlockPrinter.print((Block) element, writer);
		} else {
			EmptyMemberPrinter.print(writer);
		}
	}

}
