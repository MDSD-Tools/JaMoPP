package tools.mdsd.jamopp.parser.interfaces.resolver;

public interface ConverterWithBoolean<B> {

	String convertToParameterName(B binding, boolean register);

}