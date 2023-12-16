package jamopp.parser.jdt.converter.interfaces.converter;

public interface Converter<From, To> {

	public abstract To convert(From from);

}
