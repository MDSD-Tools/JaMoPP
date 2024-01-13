package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ToMethodNameConverter {

	private static final String JAVA_LANG_OBJECT_CLONE = "java.lang.Object::clone()";
	private final Map<IBinding, String> nameCache;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public ToMethodNameConverter(ToTypeNameConverter toTypeNameConverter, Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	public String convertToMethodName(IMethodBinding binding) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			IMethodBinding methodDeclaration = binding.getMethodDeclaration();
			StringBuilder builder = new StringBuilder();
			builder.append(toTypeNameConverter.convertToTypeName(methodDeclaration.getDeclaringClass())).append("::")
					.append(methodDeclaration.getName()).append('(');
			for (ITypeBinding p : methodDeclaration.getParameterTypes()) {
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
			String name = builder.toString();
			nameCache.put(methodDeclaration, name);
			result = name;
		}
		return result;
	}

}
