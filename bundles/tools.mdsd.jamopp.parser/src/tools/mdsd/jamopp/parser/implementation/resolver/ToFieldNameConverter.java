package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class ToFieldNameConverter implements ToStringConverter<IVariableBinding> {

	private final Map<IBinding, String> nameCache;
	private final Provider<ToStringConverter<ITypeBinding>> toTypeNameConverter;

	@Inject
	public ToFieldNameConverter(
			@Named("ToTypeNameConverterFromBinding") final Provider<ToStringConverter<ITypeBinding>> toTypeNameConverter,
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
