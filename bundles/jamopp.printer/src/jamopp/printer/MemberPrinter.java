package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.statements.Block;

class MemberPrinter {

	static void print(Member element, BufferedWriter writer) throws IOException {
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
