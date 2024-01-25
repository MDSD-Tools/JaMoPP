package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ToTypeParameterNameConverter {

	private final Map<IBinding, String> nameCache;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ToTypeParameterNameConverter(final ToTypeNameConverter toTypeNameConverter,
			final ToMethodNameConverter toMethodNameConverter, final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	protected String convertToTypeParameterName(final ITypeBinding binding) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			final StringBuilder name = new StringBuilder();
			if (binding.getDeclaringClass() != null) {
				name.append(toTypeNameConverter.convertToTypeName(binding.getDeclaringClass()));
			} else if (binding.getDeclaringMethod() != null) {
				name.append(toMethodNameConverter.convertToMethodName(binding.getDeclaringMethod()));
			}
			name.append('.').append(binding.getName());
			nameCache.put(binding, name.toString());
			result = name.toString();
		}
		return result;
	}

}
