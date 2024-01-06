package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;

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

	private final List<Pair<?>> mapping;

	@Inject
	public LiteralPrinterImpl() {
		mapping = new ArrayList<>();
		mapping.add(new Pair<>(BooleanLiteral.class, lit -> Boolean.toString(lit.isValue())));
		mapping.add(new Pair<>(CharacterLiteral.class, lit -> "'" + lit.getValue() + "'"));
		mapping.add(new Pair<>(NullLiteral.class, lit -> "null"));
		mapping.add(new Pair<>(DecimalFloatLiteral.class, lit -> Float.toString(lit.getDecimalValue()) + "F"));
		mapping.add(new Pair<>(HexFloatLiteral.class, lit -> Float.toHexString(lit.getHexValue()) + "F"));
		mapping.add(new Pair<>(DecimalDoubleLiteral.class, lit -> Double.toString(lit.getDecimalValue()) + "D"));
		mapping.add(new Pair<>(HexDoubleLiteral.class, lit -> Double.toHexString(lit.getHexValue()) + "D"));
		mapping.add(new Pair<>(DecimalIntegerLiteral.class, lit -> lit.getDecimalValue().toString()));
		mapping.add(new Pair<>(HexIntegerLiteral.class, lit -> "0x" + lit.getHexValue().toString(16)));
		mapping.add(new Pair<>(OctalIntegerLiteral.class, lit -> "0" + lit.getOctalValue().toString(8)));
		mapping.add(new Pair<>(BinaryIntegerLiteral.class, lit -> "0b" + lit.getBinaryValue().toString(2)));
		mapping.add(new Pair<>(DecimalLongLiteral.class, lit -> lit.getDecimalValue().toString() + "L"));
		mapping.add(new Pair<>(HexLongLiteral.class, lit -> "0x" + lit.getHexValue().toString(16) + "L"));
		mapping.add(new Pair<>(OctalLongLiteral.class, lit -> "0" + lit.getOctalValue().toString(8) + "L"));
		mapping.add(new Pair<>(BinaryLongLiteral.class, lit -> "0b" + lit.getBinaryValue().toString(2) + "L"));
	}

	@Override
	public void print(Literal element, BufferedWriter writer) throws IOException {
		for (Pair<?> mapping : mapping) {
			if (mapping.getClazz().isInstance(element)) {
				writer.append(mapping.convert(element));
				return;
			}
		}
	}

	private static class Pair<K extends Literal> {

		private final Class<K> clazz;
		private final Function<K, String> fun;

		Pair(Class<K> clazz, Function<K, String> fun) {
			this.clazz = clazz;
			this.fun = fun;
		}

		Class<K> getClazz() {
			return clazz;
		}

		String convert(Literal literal) {
			return fun.apply(clazz.cast(literal));
		}
	}

}
