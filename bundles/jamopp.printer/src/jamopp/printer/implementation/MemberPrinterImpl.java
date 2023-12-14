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
import com.google.inject.Provider;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.EmptyMemberPrinterInt;
import jamopp.printer.interfaces.printer.InterfaceMethodPrinterInt;
import jamopp.printer.interfaces.printer.MemberPrinterInt;

public class MemberPrinterImpl implements MemberPrinterInt {

	private final Provider<Printer<Block>> BlockPrinter;
	private final Provider<Printer<ClassMethod>> ClassMethodPrinter;
	private final Provider<Printer<ConcreteClassifier>> ConcreteClassifierPrinter;
	private final Provider<Printer<Constructor>> ConstructorPrinter;
	private final Provider<EmptyMemberPrinterInt> EmptyMemberPrinter;
	private final Provider<Printer<Field>> FieldPrinter;
	private final Provider<InterfaceMethodPrinterInt> InterfaceMethodPrinter;

	@Inject
	public MemberPrinterImpl(Provider<Printer<Field>> fieldPrinter, Provider<Printer<Constructor>> constructorPrinter,
			Provider<Printer<ClassMethod>> classMethodPrinter,
			Provider<InterfaceMethodPrinterInt> interfaceMethodPrinter,
			Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter, Provider<Printer<Block>> blockPrinter,
			Provider<EmptyMemberPrinterInt> emptyMemberPrinter) {
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
			FieldPrinter.get().print((Field) element, writer);
		} else if (element instanceof Constructor) {
			ConstructorPrinter.get().print((Constructor) element, writer);
		} else if (element instanceof ClassMethod) {
			ClassMethodPrinter.get().print((ClassMethod) element, writer);
		} else if (element instanceof InterfaceMethod) {
			InterfaceMethodPrinter.get().print((InterfaceMethod) element, writer);
		} else if (element instanceof ConcreteClassifier) {
			ConcreteClassifierPrinter.get().print((ConcreteClassifier) element, writer);
		} else if (element instanceof Block) {
			BlockPrinter.get().print((Block) element, writer);
		} else {
			EmptyMemberPrinter.get().print(writer);
		}
	}

}
