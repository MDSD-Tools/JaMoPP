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

	private final List<Mapping<?>> mappings;

	@Inject
	public LiteralPrinterImpl() {
		mappings = new ArrayList<>();
		mappings.add(new Mapping<>(BooleanLiteral.class, lit -> Boolean.toString(lit.isValue())));
		mappings.add(new Mapping<>(CharacterLiteral.class, lit -> "'" + lit.getValue() + "'"));
		mappings.add(new Mapping<>(NullLiteral.class, lit -> "null"));
		mappings.add(new Mapping<>(DecimalFloatLiteral.class, lit -> Float.toString(lit.getDecimalValue()) + "F"));
		mappings.add(new Mapping<>(HexFloatLiteral.class, lit -> Float.toHexString(lit.getHexValue()) + "F"));
		mappings.add(new Mapping<>(DecimalDoubleLiteral.class, lit -> Double.toString(lit.getDecimalValue()) + "D"));
		mappings.add(new Mapping<>(HexDoubleLiteral.class, lit -> Double.toHexString(lit.getHexValue()) + "D"));
		mappings.add(new Mapping<>(DecimalIntegerLiteral.class, lit -> lit.getDecimalValue().toString()));
		mappings.add(new Mapping<>(HexIntegerLiteral.class, lit -> "0x" + lit.getHexValue().toString(16)));
		mappings.add(new Mapping<>(OctalIntegerLiteral.class, lit -> "0" + lit.getOctalValue().toString(8)));
		mappings.add(new Mapping<>(BinaryIntegerLiteral.class, lit -> "0b" + lit.getBinaryValue().toString(2)));
		mappings.add(new Mapping<>(DecimalLongLiteral.class, lit -> lit.getDecimalValue().toString() + "L"));
		mappings.add(new Mapping<>(HexLongLiteral.class, lit -> "0x" + lit.getHexValue().toString(16) + "L"));
		mappings.add(new Mapping<>(OctalLongLiteral.class, lit -> "0" + lit.getOctalValue().toString(8) + "L"));
		mappings.add(new Mapping<>(BinaryLongLiteral.class, lit -> "0b" + lit.getBinaryValue().toString(2) + "L"));
	}

	@Override
	public void print(Literal element, BufferedWriter writer) throws IOException {
		for (Mapping<?> pair : mappings) {
			if (pair.getClazz().isInstance(element)) {
				writer.append(pair.convert(element));
				return;
			}
		}
	}

	private static class Mapping<K extends Literal> {

		private final Class<K> clazz;
		private final Function<K, String> fun;

		private Mapping(Class<K> clazz, Function<K, String> fun) {
			this.clazz = clazz;
			this.fun = fun;
		}

		private Class<K> getClazz() {
			return clazz;
		}

		private String convert(Literal literal) {
			return fun.apply(clazz.cast(literal));
		}
	}

}
