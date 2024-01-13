package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;

public class ToTypeNameConverter {

	private final Map<IBinding, String> nameCache;
	private final Provider<ToMethodNameConverter> toMethodNameConverter;
	private final ToFieldNameConverter toFieldNameConverter;
	private final Map<Class<?>, Function<TypeReference, String>> mapping;

	@Inject
	public ToTypeNameConverter(Provider<ToMethodNameConverter> toMethodNameConverter,
			ToFieldNameConverter toFieldNameConverter, Map<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toMethodNameConverter = toMethodNameConverter;
		this.toFieldNameConverter = toFieldNameConverter;
		mapping = new HashMap<>();

		mapping.put(ClassifierReference.class, convRef -> convertClassifierReference((ClassifierReference) convRef));
		mapping.put(NamespaceClassifierReference.class,
				convRef -> convertNamespaceClassifierReference((NamespaceClassifierReference) convRef));
		mapping.put(tools.mdsd.jamopp.model.java.types.Boolean.class, convRef -> "boolean");
		mapping.put(tools.mdsd.jamopp.model.java.types.Byte.class, convRef -> "byte");
		mapping.put(tools.mdsd.jamopp.model.java.types.Char.class, convRef -> "char");
		mapping.put(tools.mdsd.jamopp.model.java.types.Double.class, convRef -> "double");
		mapping.put(tools.mdsd.jamopp.model.java.types.Float.class, convRef -> "float");
		mapping.put(tools.mdsd.jamopp.model.java.types.Int.class, convRef -> "int");
		mapping.put(tools.mdsd.jamopp.model.java.types.Long.class, convRef -> "long");
		mapping.put(tools.mdsd.jamopp.model.java.types.Short.class, convRef -> "short");
		mapping.put(tools.mdsd.jamopp.model.java.types.Void.class, convRef -> "void");
	}

	public String convertToTypeName(ITypeBinding binding) {
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
			String qualifiedName;
			if (binding.isMember()) {
				qualifiedName = convertToTypeName(binding.getDeclaringClass()) + "." + binding.getName();
			} else {
				qualifiedName = binding.getQualifiedName();
			}
			if (qualifiedName.contains("<")) {
				qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf('<'));
			}
			nameCache.put(binding, qualifiedName);
			result = qualifiedName;
		}
		return result;
	}

	private String handleLocal(ITypeBinding binding) {
		String qualifiedName;
		IBinding iBinding = binding.getDeclaringMember();
		if (iBinding instanceof IMethodBinding) {
			qualifiedName = toMethodNameConverter.get().convertToMethodName((IMethodBinding) iBinding) + "."
					+ binding.getKey();
		} else if (iBinding instanceof IVariableBinding) {
			qualifiedName = toFieldNameConverter.convertToFieldName((IVariableBinding) iBinding) + "."
					+ binding.getKey();
		} else {
			qualifiedName = binding.getKey();
		}
		nameCache.put(binding, qualifiedName);
		return qualifiedName;
	}

	public String convertToTypeName(TypeReference ref) {
		String result = null;
		for (Entry<Class<?>, Function<TypeReference, String>> entry : mapping.entrySet()) {
			Class<?> key = entry.getKey();
			Function<TypeReference, String> val = entry.getValue();
			if (key.isInstance(ref)) {
				result = val.apply(ref);
				break;
			}
		}
		return result;
	}

	private String convertNamespaceClassifierReference(NamespaceClassifierReference nRef) {
		String result;
		if (nRef.getClassifierReferences().isEmpty()) {
			result = nRef.getNamespacesAsString();
		} else {
			result = convertToTypeName(nRef.getClassifierReferences().get(nRef.getClassifierReferences().size() - 1));
		}
		return result;
	}

	private String convertClassifierReference(ClassifierReference convRef) {
		String result;
		if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) {
			result = ((tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) convRef.getTarget())
					.getQualifiedName();
		} else if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.types.InferableType) {
			result = "var";
		} else {
			result = ((tools.mdsd.jamopp.model.java.generics.TypeParameter) convRef.getTarget()).getName();
		}
		return result;
	}

}
