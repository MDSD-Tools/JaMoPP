package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ToTypeParameterNameConverter {

	private final Map<IBinding, String> nameCache;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ToTypeParameterNameConverter(ToTypeNameConverter toTypeNameConverter,
			ToMethodNameConverter toMethodNameConverter, Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	protected String convertToTypeParameterName(ITypeBinding binding) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String name = "";
		if (binding.getDeclaringClass() != null) {
			name += toTypeNameConverter.convertToTypeName(binding.getDeclaringClass());
		} else if (binding.getDeclaringMethod() != null) {
			name += toMethodNameConverter.convertToMethodName(binding.getDeclaringMethod());
		}
		name += "." + binding.getName();
		nameCache.put(binding, name);
		return name;
	}

}
