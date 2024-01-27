package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class ToFieldNameConverter {

	private final Map<IBinding, String> nameCache;
	private final Provider<ToTypeNameConverter> toTypeNameConverter;

	@Inject
	public ToFieldNameConverter(final Provider<ToTypeNameConverter> toTypeNameConverter,
			final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	public String convertToFieldName(final IVariableBinding binding) {
		String result;
		if (binding == null || !binding.isField()) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			final String name = toTypeNameConverter.get().convertToTypeName(binding.getDeclaringClass()) + "::"
					+ binding.getName();
			nameCache.put(binding, name);
			result = name;
		}
		return result;
	}

}
