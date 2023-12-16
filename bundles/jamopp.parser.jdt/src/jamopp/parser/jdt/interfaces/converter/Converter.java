package jamopp.parser.jdt.interfaces.converter;

public interface Converter<From, To> {

	public abstract To convert(From from);

}
