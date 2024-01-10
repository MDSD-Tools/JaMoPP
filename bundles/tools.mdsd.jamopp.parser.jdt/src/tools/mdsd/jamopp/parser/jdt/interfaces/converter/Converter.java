package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

public interface Converter<From, To> {

	To convert(From from);

}
