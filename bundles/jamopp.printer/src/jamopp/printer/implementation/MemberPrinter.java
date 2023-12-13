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

class MemberPrinter implements Printer<Member> {

	private final FieldPrinter FieldPrinter;
	private final ConstructorPrinter ConstructorPrinter;
	private final ClassMethodPrinter ClassMethodPrinter;
	private final InterfaceMethodPrinter InterfaceMethodPrinter;
	private final ConcreteClassifierPrinter ConcreteClassifierPrinter;
	private final BlockPrinter BlockPrinter;
	private final EmptyMemberPrinter EmptyMemberPrinter;

	@Inject
	public MemberPrinter(jamopp.printer.implementation.FieldPrinter fieldPrinter,
			jamopp.printer.implementation.ConstructorPrinter constructorPrinter,
			jamopp.printer.implementation.ClassMethodPrinter classMethodPrinter,
			jamopp.printer.implementation.InterfaceMethodPrinter interfaceMethodPrinter,
			jamopp.printer.implementation.ConcreteClassifierPrinter concreteClassifierPrinter,
			jamopp.printer.implementation.BlockPrinter blockPrinter,
			jamopp.printer.implementation.EmptyMemberPrinter emptyMemberPrinter) {
		super();
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
