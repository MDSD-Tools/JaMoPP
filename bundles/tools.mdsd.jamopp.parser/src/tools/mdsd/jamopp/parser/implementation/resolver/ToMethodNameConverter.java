package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ToMethodNameConverter {

	private static final String JAVA_LANG_OBJECT_CLONE = "java.lang.Object::clone()";
	private final Map<IBinding, String> nameCache;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public ToMethodNameConverter(final ToTypeNameConverter toTypeNameConverter, final Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	public String convertToMethodName(final IMethodBinding binding) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			final IMethodBinding methodDeclaration = binding.getMethodDeclaration();
			final StringBuilder builder = new StringBuilder();
			builder.append(toTypeNameConverter.convertToTypeName(methodDeclaration.getDeclaringClass())).append("::")
					.append(methodDeclaration.getName()).append('(');
			for (final ITypeBinding p : methodDeclaration.getParameterTypes()) {
				builder.append(toTypeNameConverter.convertToTypeName(p));
				for (int i = 0; i < p.getDimensions(); i++) {
					builder.append("[]");
				}
			}
			builder.append(')');
			if (JAVA_LANG_OBJECT_CLONE.equals(builder.toString()) && methodDeclaration.getReturnType().isArray()) {
				builder.append("java.lang.Object");
			} else {
				builder.append(toTypeNameConverter.convertToTypeName(methodDeclaration.getReturnType()));
			}
			final String name = builder.toString();
			nameCache.put(methodDeclaration, name);
			result = name;
		}
		return result;
	}

}
