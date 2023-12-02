package jamopp.parser.jdt;

abstract class ToConverter<From, To> {

	abstract To convert(From declaration);

}
