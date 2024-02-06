package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;

public class ToFieldNameConverter implements Converter<IVariableBinding> {

	private final Map<IBinding, String> nameCache;
	private final Provider<Converter<ITypeBinding>> toTypeNameConverter;

	@Inject
	public ToFieldNameConverter(
			@Named("ToTypeNameConverter") final Provider<Converter<ITypeBinding>> toTypeNameConverter,
			final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public String convert(final IVariableBinding binding) {
		String result;
		if (binding == null || !binding.isField()) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			final String name = toTypeNameConverter.get().convert(binding.getDeclaringClass()) + "::"
					+ binding.getName();
			nameCache.put(binding, name);
			result = name;
		}
		return result;
	}

}
