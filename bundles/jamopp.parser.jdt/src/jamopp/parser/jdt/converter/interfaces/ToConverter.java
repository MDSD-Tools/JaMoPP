package jamopp.parser.jdt.converter.interfaces;

public interface ToConverter<From, To> {

	public abstract To convert(From from);

}
