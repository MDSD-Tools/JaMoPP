package jamopp.parser.jdt.converter;

public abstract class ToConverter<From, To> {

	public abstract To convert(From declaration);

}
