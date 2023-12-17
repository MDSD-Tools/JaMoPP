package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.Abstract;
import org.emftext.language.java.modifiers.Default;
import org.emftext.language.java.modifiers.Final;
import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.modifiers.Native;
import org.emftext.language.java.modifiers.Private;
import org.emftext.language.java.modifiers.Protected;
import org.emftext.language.java.modifiers.Public;
import org.emftext.language.java.modifiers.Static;
import org.emftext.language.java.modifiers.Strictfp;
import org.emftext.language.java.modifiers.Synchronized;
import org.emftext.language.java.modifiers.Transient;
import org.emftext.language.java.modifiers.Volatile;

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
