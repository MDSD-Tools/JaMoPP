package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;

public class ToTypeParameterNameConverter implements Converter<ITypeBinding> {

	private final Map<IBinding, String> nameCache;
	private final Converter<ITypeBinding> toTypeNameConverter;
	private final Converter<IMethodBinding> toMethodNameConverter;

	@Inject
	public ToTypeParameterNameConverter(@Named("ToTypeNameConverter") final Converter<ITypeBinding> toTypeNameConverter,
			final Converter<IMethodBinding> toMethodNameConverter, final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	@Override
	public String convert(final ITypeBinding binding) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			final StringBuilder name = new StringBuilder();
			if (binding.getDeclaringClass() != null) {
				name.append(toTypeNameConverter.convert(binding.getDeclaringClass()));
			} else if (binding.getDeclaringMethod() != null) {
				name.append(toMethodNameConverter.convert(binding.getDeclaringMethod()));
			}
			name.append('.').append(binding.getName());
			nameCache.put(binding, name.toString());
			result = name.toString();
		}
		return result;
	}

}
