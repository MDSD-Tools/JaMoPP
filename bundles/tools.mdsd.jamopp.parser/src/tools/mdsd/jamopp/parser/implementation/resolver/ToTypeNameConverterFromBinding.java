package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class ToTypeNameConverterFromBinding implements ToStringConverter<ITypeBinding> {

	private final Map<IBinding, String> nameCache;
	private final Provider<ToStringConverter<IMethodBinding>> toMethodNameConverter;
	private final ToStringConverter<IVariableBinding> toFieldNameConverter;

	@Inject
	public ToTypeNameConverterFromBinding(final Provider<ToStringConverter<IMethodBinding>> toMethodNameConverter,
			final ToStringConverter<IVariableBinding> toFieldNameConverter, final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toMethodNameConverter = toMethodNameConverter;
		this.toFieldNameConverter = toFieldNameConverter;

	}

	@Override
	public String convert(final ITypeBinding binding) {
		String result;
		if (binding == null) {
			result = "";
		} else if (binding.isTypeVariable()) {
			result = binding.getName();
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else if (binding.isLocal()) {
			result = handleLocal(binding);
		} else {
			result = handleElse(binding);
		}
		return result;
	}

	private String handleElse(final ITypeBinding binding) {
		String qualifiedName;
		if (binding.isMember()) {
			qualifiedName = convert(binding.getDeclaringClass()) + "." + binding.getName();
		} else {
			qualifiedName = binding.getQualifiedName();
		}
		if (qualifiedName.contains("<")) {
			qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf('<'));
		}
		nameCache.put(binding, qualifiedName);
		return qualifiedName;
	}

	private String handleLocal(final ITypeBinding binding) {
		String qualifiedName;
		final IBinding iBinding = binding.getDeclaringMember();
		if (iBinding instanceof IMethodBinding) {
			qualifiedName = toMethodNameConverter.get().convert((IMethodBinding) iBinding) + "." + binding.getKey();
		} else if (iBinding instanceof IVariableBinding) {
			qualifiedName = toFieldNameConverter.convert((IVariableBinding) iBinding) + "." + binding.getKey();
		} else {
			qualifiedName = binding.getKey();
		}
		nameCache.put(binding, qualifiedName);
		return qualifiedName;
	}

}
