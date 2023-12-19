package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public abstract class Resolver<Clazz, BindingType extends IBinding> {

	private final HashMap<String, Clazz> bindings;
	private final HashMap<IBinding, String> nameCache;

	public Resolver(HashMap<IBinding, String> nameCache, HashMap<String, Clazz> bindings) {
		this.bindings = bindings;
		this.nameCache = nameCache;
	}

	public abstract Clazz getByBinding(BindingType binding);

	public abstract Clazz getByName(String name);

	public final HashMap<String, Clazz> getBindings() {
		return this.bindings;
	}

	public final void clearBindings() {
		this.bindings.clear();
	}

	protected String convertToTypeName(ITypeBinding binding) {
		if (binding == null) {
			return "";
		}
		if (binding.isTypeVariable()) {
			return binding.getName();
		}
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
		}
		String qualifiedName;
		if (binding.isMember()) {
			qualifiedName = convertToTypeName(binding.getDeclaringClass()) + "." + binding.getName();
		} else if (binding.isLocal()) {
			IBinding b = binding.getDeclaringMember();
			if (b instanceof IMethodBinding) {
				qualifiedName = convertToMethodName((IMethodBinding) b) + "." + binding.getKey();
			} else if (b instanceof IVariableBinding) {
				qualifiedName = convertToFieldName((IVariableBinding) b) + "." + binding.getKey();
			} else {
				qualifiedName = binding.getKey();
			}
			this.nameCache.put(binding, qualifiedName);
			return qualifiedName;
		} else {
			qualifiedName = binding.getQualifiedName();
		}
		if (qualifiedName.contains("<")) {
			qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf("<"));
		}
		this.nameCache.put(binding, qualifiedName);
		return qualifiedName;
	}

	private String convertToMethodName(IMethodBinding binding) {
		if (binding == null) {
			return "";
		}
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
		}
		binding = binding.getMethodDeclaration();
		StringBuilder builder = new StringBuilder();
		builder.append(convertToTypeName(binding.getDeclaringClass()));
		builder.append("::");
		builder.append(binding.getName());
		builder.append("(");
		for (ITypeBinding p : binding.getParameterTypes()) {
			builder.append(convertToTypeName(p));
			for (int i = 0; i < p.getDimensions(); i++) {
				builder.append("[]");
			}
		}
		builder.append(")");
		if ("java.lang.Object::clone()".equals(builder.toString()) && binding.getReturnType().isArray()) {
			builder.append("java.lang.Object");
		} else {
			builder.append(convertToTypeName(binding.getReturnType()));
		}
		String name = builder.toString();
		this.nameCache.put(binding, name);
		return name;
	}

	private String convertToFieldName(IVariableBinding binding) {
		if (binding == null || !binding.isField()) {
			return "";
		}
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
		}
		String name = convertToTypeName(binding.getDeclaringClass()) + "::" + binding.getName();
		this.nameCache.put(binding, name);
		return name;
	}

}
