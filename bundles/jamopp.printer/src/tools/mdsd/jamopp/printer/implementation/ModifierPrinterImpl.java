package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modifiers.Abstract;
import tools.mdsd.jamopp.model.java.modifiers.Default;
import tools.mdsd.jamopp.model.java.modifiers.Final;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.modifiers.Native;
import tools.mdsd.jamopp.model.java.modifiers.Private;
import tools.mdsd.jamopp.model.java.modifiers.Protected;
import tools.mdsd.jamopp.model.java.modifiers.Public;
import tools.mdsd.jamopp.model.java.modifiers.Static;
import tools.mdsd.jamopp.model.java.modifiers.Strictfp;
import tools.mdsd.jamopp.model.java.modifiers.Synchronized;
import tools.mdsd.jamopp.model.java.modifiers.Transient;
import tools.mdsd.jamopp.model.java.modifiers.Volatile;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ModifierPrinterImpl implements Printer<Modifier> {

	@Override
	public void print(Modifier element, BufferedWriter writer) throws IOException {
		if (element instanceof Abstract) {
			writer.append("abstract ");
		} else if (element instanceof Final) {
			writer.append("final ");
		} else if (element instanceof Native) {
			writer.append("native ");
		} else if (element instanceof Protected) {
			writer.append("protected ");
		} else if (element instanceof Private) {
			writer.append("private ");
		} else if (element instanceof Public) {
			writer.append("public ");
		} else if (element instanceof Static) {
			writer.append("static ");
		} else if (element instanceof Strictfp) {
			writer.append("strictfp ");
		} else if (element instanceof Synchronized) {
			writer.append("synchronized ");
		} else if (element instanceof Transient) {
			writer.append("transient ");
		} else if (element instanceof Volatile) {
			writer.append("volatile ");
		} else if (element instanceof Default) {
			writer.append("default ");
		}
	}

}
