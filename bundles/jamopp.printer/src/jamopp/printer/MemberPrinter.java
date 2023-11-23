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

public class MemberPrinter {

	static void printMember(Member element, BufferedWriter writer) throws IOException {
		if (element instanceof Field) {
			FieldPrinter.printField((Field) element, writer);
		} else if (element instanceof Constructor) {
			ConstructorPrinter.printConstructor((Constructor) element, writer);
		} else if (element instanceof ClassMethod) {
			ClassMethodPrinter.printClassMethod((ClassMethod) element, writer);
		} else if (element instanceof InterfaceMethod) {
			InterfaceMethodPrinter.printInterfaceMethod((InterfaceMethod) element, writer);
		} else if (element instanceof ConcreteClassifier) {
			ConcreteClassifierPrinter.printConcreteClassifier((ConcreteClassifier) element, writer);
		} else if (element instanceof Block) {
			BlockPrinter.printBlock((Block) element, writer);
		} else {
			EmptyMemberPrinter.printEmptyMember(writer);
		}
	}

}
