package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import javax.inject.Inject;

public class ToMethodNameConverter {

	private final HashMap<IBinding, String> nameCache;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public ToMethodNameConverter(ToTypeNameConverter toTypeNameConverter, HashMap<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	public String convertToMethodName(IMethodBinding binding) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		binding = binding.getMethodDeclaration();
		StringBuilder builder = new StringBuilder();
		builder.append(toTypeNameConverter.convertToTypeName(binding.getDeclaringClass()));
		builder.append("::");
		builder.append(binding.getName());
		builder.append("(");
		for (ITypeBinding p : binding.getParameterTypes()) {
			builder.append(toTypeNameConverter.convertToTypeName(p));
			for (int i = 0; i < p.getDimensions(); i++) {
				builder.append("[]");
			}
		}
		builder.append(")");
		if ("java.lang.Object::clone()".equals(builder.toString()) && binding.getReturnType().isArray()) {
			builder.append("java.lang.Object");
		} else {
			builder.append(toTypeNameConverter.convertToTypeName(binding.getReturnType()));
		}
		String name = builder.toString();
		nameCache.put(binding, name);
		return name;
	}

}
