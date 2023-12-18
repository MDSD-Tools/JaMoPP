package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.statements.Block;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MemberPrinterImpl implements Printer<Member> {

	private final Provider<Printer<Block>> blockPrinter;
	private final Provider<Printer<ClassMethod>> classMethodPrinter;
	private final Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter;
	private final Provider<Printer<Constructor>> constructorPrinter;
	private final Provider<EmptyPrinter> emptyMemberPrinter;
	private final Provider<Printer<Field>> fieldPrinter;
	private final Provider<Printer<InterfaceMethod>> interfaceMethodPrinter;

	@Inject
	public MemberPrinterImpl(Provider<Printer<Field>> fieldPrinter, Provider<Printer<Constructor>> constructorPrinter,
			Provider<Printer<ClassMethod>> classMethodPrinter,
			Provider<Printer<InterfaceMethod>> interfaceMethodPrinter,
			Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter, Provider<Printer<Block>> blockPrinter,
			@Named("EmptyMemberPrinter") Provider<EmptyPrinter> emptyMemberPrinter) {
		this.fieldPrinter = fieldPrinter;
		this.constructorPrinter = constructorPrinter;
		this.classMethodPrinter = classMethodPrinter;
		this.interfaceMethodPrinter = interfaceMethodPrinter;
		this.concreteClassifierPrinter = concreteClassifierPrinter;
		this.blockPrinter = blockPrinter;
		this.emptyMemberPrinter = emptyMemberPrinter;
	}

	@Override
	public void print(Member element, BufferedWriter writer) throws IOException {
		if (element instanceof Field) {
			this.fieldPrinter.get().print((Field) element, writer);
		} else if (element instanceof Constructor) {
			this.constructorPrinter.get().print((Constructor) element, writer);
		} else if (element instanceof ClassMethod) {
			this.classMethodPrinter.get().print((ClassMethod) element, writer);
		} else if (element instanceof InterfaceMethod) {
			this.interfaceMethodPrinter.get().print((InterfaceMethod) element, writer);
		} else if (element instanceof ConcreteClassifier) {
			this.concreteClassifierPrinter.get().print((ConcreteClassifier) element, writer);
		} else if (element instanceof Block) {
			this.blockPrinter.get().print((Block) element, writer);
		} else {
			this.emptyMemberPrinter.get().print(writer);
		}
	}

}