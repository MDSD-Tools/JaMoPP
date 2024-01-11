package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class ToFieldNameConverter {

	private final Map<IBinding, String> nameCache;
	private final Provider<ToTypeNameConverter> toTypeNameConverter;

	@Inject
	public ToFieldNameConverter(Provider<ToTypeNameConverter> toTypeNameConverter, Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	public String convertToFieldName(IVariableBinding binding) {
		if (binding == null || !binding.isField()) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String name = toTypeNameConverter.get().convertToTypeName(binding.getDeclaringClass()) + "::"
				+ binding.getName();
		nameCache.put(binding, name);
		return name;
	}

}
