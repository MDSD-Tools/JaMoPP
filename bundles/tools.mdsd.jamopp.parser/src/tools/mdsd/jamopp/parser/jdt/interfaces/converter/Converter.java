package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

/**
 * Converts an instance of F to an instance of T.
 *
 * @param <F>   From
 * @param <T>To
 */
public interface Converter<F, T> {

	T convert(F from);

}
