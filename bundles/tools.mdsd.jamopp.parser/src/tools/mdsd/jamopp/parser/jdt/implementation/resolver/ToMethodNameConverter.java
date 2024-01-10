package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

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
		IMethodBinding methodDeclaration = binding.getMethodDeclaration();
		StringBuilder builder = new StringBuilder();
		builder.append(toTypeNameConverter.convertToTypeName(methodDeclaration.getDeclaringClass()));
		builder.append("::");
		builder.append(methodDeclaration.getName());
		builder.append("(");
		for (ITypeBinding p : methodDeclaration.getParameterTypes()) {
			builder.append(toTypeNameConverter.convertToTypeName(p));
			for (int i = 0; i < p.getDimensions(); i++) {
				builder.append("[]");
			}
		}
		builder.append(")");
		if ("java.lang.Object::clone()".equals(builder.toString()) && methodDeclaration.getReturnType().isArray()) {
			builder.append("java.lang.Object");
		} else {
			builder.append(toTypeNameConverter.convertToTypeName(methodDeclaration.getReturnType()));
		}
		String name = builder.toString();
		nameCache.put(methodDeclaration, name);
		return name;
	}

}
