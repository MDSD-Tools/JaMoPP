package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;

public class ToMethodNameConverter implements Converter<IMethodBinding> {

	private static final String JAVA_LANG_OBJECT_CLONE = "java.lang.Object::clone()";
	private final Map<IBinding, String> nameCache;
	private final Converter<ITypeBinding> toTypeNameConverter;

	@Inject
	public ToMethodNameConverter(@Named("ToTypeNameConverter") final Converter<ITypeBinding> toTypeNameConverter,
			final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public String convert(final IMethodBinding binding) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			result = handleElse(binding);
		}
		return result;
	}

	private String handleElse(final IMethodBinding binding) {
		final IMethodBinding methodDeclaration = binding.getMethodDeclaration();
		final StringBuilder builder = new StringBuilder();
		builder.append(toTypeNameConverter.convert(methodDeclaration.getDeclaringClass())).append("::")
				.append(methodDeclaration.getName()).append('(');
		for (final ITypeBinding p : methodDeclaration.getParameterTypes()) {
			builder.append(toTypeNameConverter.convert(p));
			for (int i = 0; i < p.getDimensions(); i++) {
				builder.append("[]");
			}
		}
		builder.append(')');
		if (JAVA_LANG_OBJECT_CLONE.equals(builder.toString()) && methodDeclaration.getReturnType().isArray()) {
			builder.append("java.lang.Object");
		} else {
			builder.append(toTypeNameConverter.convert(methodDeclaration.getReturnType()));
		}
		final String name = builder.toString();
		nameCache.put(methodDeclaration, name);
		return name;
	}

}
