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

import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.ClassMethodPrinterInt;
import jamopp.printer.interfaces.printer.ConcreteClassifierPrinterInt;
import jamopp.printer.interfaces.printer.ConstructorPrinterInt;
import jamopp.printer.interfaces.printer.EmptyMemberPrinterInt;
import jamopp.printer.interfaces.printer.FieldPrinterInt;
import jamopp.printer.interfaces.printer.InterfaceMethodPrinterInt;
import jamopp.printer.interfaces.printer.MemberPrinterInt;

public class MemberPrinterImpl implements MemberPrinterInt {

	private final Provider<FieldPrinterInt> FieldPrinter;
	private final Provider<ConstructorPrinterInt> ConstructorPrinter;
	private final Provider<ClassMethodPrinterInt> ClassMethodPrinter;
	private final Provider<InterfaceMethodPrinterInt> InterfaceMethodPrinter;
	private final Provider<ConcreteClassifierPrinterInt> ConcreteClassifierPrinter;
	private final Provider<BlockPrinterInt> BlockPrinter;
	private final Provider<EmptyMemberPrinterInt> EmptyMemberPrinter;

	@Inject
	public MemberPrinterImpl(Provider<FieldPrinterInt> fieldPrinter, Provider<ConstructorPrinterInt> constructorPrinter,
			Provider<ClassMethodPrinterInt> classMethodPrinter,
			Provider<InterfaceMethodPrinterInt> interfaceMethodPrinter,
			Provider<ConcreteClassifierPrinterInt> concreteClassifierPrinter, Provider<BlockPrinterInt> blockPrinter,
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
