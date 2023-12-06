package jamopp.parser.jdt.converter.interfaces.converter;

public interface ToConverter<From, To> {

	public abstract To convert(From from);

}
