package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.imports.ClassifierImport;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.PackageImport;
import tools.mdsd.jamopp.model.java.imports.StaticClassifierImport;
import tools.mdsd.jamopp.model.java.imports.StaticMemberImport;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ImportPrinterImpl implements Printer<Import> {

	private static void printClassifierImport(ClassifierImport element, BufferedWriter writer) throws IOException {
		writer.append(element.getNamespacesAsString() + "." + element.getClassifier().getName());
	}

	private static void printPackageImport(PackageImport element, BufferedWriter writer) throws IOException {
		writer.append(element.getNamespacesAsString());
		if (element.getClassifier() != null) {
			writer.append("." + element.getClassifier().getName());
		}
		writer.append(".*");
	}

	private static void printStaticClassifierImport(StaticClassifierImport element, BufferedWriter writer)
			throws IOException {
		writer.append("static " + element.getNamespacesAsString() + "." + element.getClassifier().getName() + ".*");
	}

	private static void printStaticMemberImport(StaticMemberImport element, BufferedWriter writer) throws IOException {
		writer.append("static " + element.getNamespacesAsString() + "." + element.getClassifier().getName() + "."
				+ element.getStaticMembers().get(0).getName());
	}

	@Override
	public void print(Import element, BufferedWriter writer) throws IOException {
		writer.append("import ");
		if (element instanceof ClassifierImport) {
			printClassifierImport((ClassifierImport) element, writer);
		} else if (element instanceof PackageImport) {
			printPackageImport((PackageImport) element, writer);
		} else if (element instanceof StaticClassifierImport) {
			printStaticClassifierImport((StaticClassifierImport) element, writer);
		} else {
			printStaticMemberImport((StaticMemberImport) element, writer);
		}
		writer.append(";\n");
	}

}
