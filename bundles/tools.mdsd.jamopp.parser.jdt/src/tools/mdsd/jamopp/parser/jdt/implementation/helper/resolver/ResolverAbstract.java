package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public abstract class ResolverAbstract<Clazz, BindingType extends IBinding> implements Resolver<Clazz, BindingType> {

	private final HashMap<String, Clazz> bindings;
	private final HashMap<IBinding, String> nameCache;

	public ResolverAbstract(HashMap<IBinding, String> nameCache, HashMap<String, Clazz> bindings) {
		this.bindings = bindings;
		this.nameCache = nameCache;
	}

	protected String convertToFieldName(IVariableBinding binding) {
		if (binding == null || !binding.isField()) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		var name = convertToTypeName(binding.getDeclaringClass()) + "::" + binding.getName();
		nameCache.put(binding, name);
		return name;
	}

	public String convertToMethodName(IMethodBinding binding) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		binding = binding.getMethodDeclaration();
		var builder = new StringBuilder();
		builder.append(convertToTypeName(binding.getDeclaringClass()));
		builder.append("::");
		builder.append(binding.getName());
		builder.append("(");
		for (ITypeBinding p : binding.getParameterTypes()) {
			builder.append(convertToTypeName(p));
			for (var i = 0; i < p.getDimensions(); i++) {
				builder.append("[]");
			}
		}
		builder.append(")");
		if ("java.lang.Object::clone()".equals(builder.toString()) && binding.getReturnType().isArray()) {
			builder.append("java.lang.Object");
		} else {
			builder.append(convertToTypeName(binding.getReturnType()));
		}
		var name = builder.toString();
		nameCache.put(binding, name);
		return name;
	}

	protected String convertToTypeName(ITypeBinding binding) {
		if (binding == null) {
			return "";
		}
		if (binding.isTypeVariable()) {
			return binding.getName();
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String qualifiedName;
		if (binding.isMember()) {
			qualifiedName = convertToTypeName(binding.getDeclaringClass()) + "." + binding.getName();
		} else if (binding.isLocal()) {
			var b = binding.getDeclaringMember();
			if (b instanceof IMethodBinding) {
				qualifiedName = convertToMethodName((IMethodBinding) b) + "." + binding.getKey();
			} else if (b instanceof IVariableBinding) {
				qualifiedName = convertToFieldName((IVariableBinding) b) + "." + binding.getKey();
			} else {
				qualifiedName = binding.getKey();
			}
			nameCache.put(binding, qualifiedName);
			return qualifiedName;
		} else {
			qualifiedName = binding.getQualifiedName();
		}
		if (qualifiedName.contains("<")) {
			qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf("<"));
		}
		nameCache.put(binding, qualifiedName);
		return qualifiedName;
	}

	protected String convertToTypeParameterName(ITypeBinding binding) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String name = "";
		if (binding.getDeclaringClass() != null) {
			name += convertToTypeName(binding.getDeclaringClass());
		} else if (binding.getDeclaringMethod() != null) {
			name += convertToMethodName(binding.getDeclaringMethod());
		}
		name += "." + binding.getName();
		nameCache.put(binding, name);
		return name;
	}

	@SuppressWarnings("unchecked")
	protected <T extends tools.mdsd.jamopp.model.java.members.Method> T checkMethod(
			tools.mdsd.jamopp.model.java.members.Method mem, IMethodBinding binding) {
		if (mem.getName().equals(binding.getName())) {
			T meth = (T) mem;
			if ("clone".equals(meth.getName())) {
				return meth;
			}
			int receiveOffset = 0;
			if (binding.getDeclaredReceiverType() != null) {
				receiveOffset = 1;
			}
			if (binding.getParameterTypes().length + receiveOffset == meth.getParameters().size()) {
				if (receiveOffset == 1
						&& (!(meth.getParameters()
								.get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
								|| !convertToTypeName(binding.getDeclaredReceiverType())
										.equals(convertToTypeName(meth.getParameters().get(0).getTypeReference())))
						|| !convertToTypeName(binding.getReturnType())
								.equals(convertToTypeName(meth.getTypeReference()))) {
					return null;
				}
				for (int i = 0; i < binding.getParameterTypes().length; i++) {
					ITypeBinding currentParamType = binding.getParameterTypes()[i];
					tools.mdsd.jamopp.model.java.parameters.Parameter currentParam = meth.getParameters()
							.get(i + receiveOffset);
					if (!convertToTypeName(currentParamType).equals(convertToTypeName(currentParam.getTypeReference()))
							|| currentParamType.getDimensions() != currentParam.getArrayDimension()) {
						return null;
					}
				}
				return meth;
			}
		}
		return null;
	}

	protected String convertToTypeName(tools.mdsd.jamopp.model.java.types.TypeReference ref) {
		if (ref instanceof tools.mdsd.jamopp.model.java.types.ClassifierReference convRef) {
			if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) {
				return ((tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) convRef.getTarget())
						.getQualifiedName();
			}
			if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.types.InferableType) {
				return "var";
			}
			return ((tools.mdsd.jamopp.model.java.generics.TypeParameter) convRef.getTarget()).getName();
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference nRef) {
			if (!nRef.getClassifierReferences().isEmpty()) {
				return convertToTypeName(nRef.getClassifierReferences().get(nRef.getClassifierReferences().size() - 1));
			}
			return nRef.getNamespacesAsString();
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Boolean) {
			return "boolean";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Byte) {
			return "byte";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Char) {
			return "char";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Double) {
			return "double";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Float) {
			return "float";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Int) {
			return "int";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Long) {
			return "long";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Short) {
			return "short";
		}
		return "void";
	}

	@Override
	public final HashMap<String, Clazz> getBindings() {
		return bindings;
	}

	@Override
	public abstract Clazz getByBinding(BindingType binding);

	@Override
	public abstract Clazz getByName(String name);

}
