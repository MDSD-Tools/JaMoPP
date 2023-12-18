package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.literals.BinaryIntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.BinaryLongLiteral;
import tools.mdsd.jamopp.model.java.literals.BooleanLiteral;
import tools.mdsd.jamopp.model.java.literals.CharacterLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalDoubleLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalFloatLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalIntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.DecimalLongLiteral;
import tools.mdsd.jamopp.model.java.literals.HexDoubleLiteral;
import tools.mdsd.jamopp.model.java.literals.HexFloatLiteral;
import tools.mdsd.jamopp.model.java.literals.HexIntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.HexLongLiteral;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.literals.NullLiteral;
import tools.mdsd.jamopp.model.java.literals.OctalIntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.OctalLongLiteral;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class LiteralPrinterImpl implements Printer<Literal> {

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
