package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.literals.BinaryIntegerLiteral;
import org.emftext.language.java.literals.BinaryLongLiteral;
import org.emftext.language.java.literals.BooleanLiteral;
import org.emftext.language.java.literals.CharacterLiteral;
import org.emftext.language.java.literals.DecimalDoubleLiteral;
import org.emftext.language.java.literals.DecimalFloatLiteral;
import org.emftext.language.java.literals.DecimalIntegerLiteral;
import org.emftext.language.java.literals.DecimalLongLiteral;
import org.emftext.language.java.literals.HexDoubleLiteral;
import org.emftext.language.java.literals.HexFloatLiteral;
import org.emftext.language.java.literals.HexIntegerLiteral;
import org.emftext.language.java.literals.HexLongLiteral;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.literals.NullLiteral;
import org.emftext.language.java.literals.OctalIntegerLiteral;
import org.emftext.language.java.literals.OctalLongLiteral;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.LiteralPrinterInt;

class LiteralPrinter implements Printer<Literal>, LiteralPrinterInt {

	@Override
	public void print(Literal element, BufferedWriter writer) throws IOException {
		if (element instanceof BooleanLiteral lit) {
			writer.append(Boolean.toString(lit.isValue()));
		} else if (element instanceof CharacterLiteral lit) {
			writer.append("'" + lit.getValue() + "'");
		} else if (element instanceof NullLiteral) {
			writer.append("null");
		} else if (element instanceof DecimalFloatLiteral lit) {
			writer.append(Float.toString(lit.getDecimalValue()) + "F");
		} else if (element instanceof HexFloatLiteral lit) {
			writer.append(Float.toHexString(lit.getHexValue()) + "F");
		} else if (element instanceof DecimalDoubleLiteral lit) {
			writer.append(Double.toString(lit.getDecimalValue()) + "D");
		} else if (element instanceof HexDoubleLiteral lit) {
			writer.append(Double.toHexString(lit.getHexValue()) + "D");
		} else if (element instanceof DecimalIntegerLiteral lit) {
			writer.append(lit.getDecimalValue().toString());
		} else if (element instanceof HexIntegerLiteral lit) {
			writer.append("0x" + lit.getHexValue().toString(16));
		} else if (element instanceof OctalIntegerLiteral lit) {
			writer.append("0" + lit.getOctalValue().toString(8));
		} else if (element instanceof BinaryIntegerLiteral lit) {
			writer.append("0b" + lit.getBinaryValue().toString(2));
		} else if (element instanceof DecimalLongLiteral lit) {
			writer.append(lit.getDecimalValue().toString() + "L");
		} else if (element instanceof HexLongLiteral lit) {
			writer.append("0x" + lit.getHexValue().toString(16) + "L");
		} else if (element instanceof OctalLongLiteral lit) {
			writer.append("0" + lit.getOctalValue().toString(8) + "L");
		} else if (element instanceof BinaryLongLiteral lit) {
			writer.append("0b" + lit.getBinaryValue().toString(2) + "L");
		}
	}

}
