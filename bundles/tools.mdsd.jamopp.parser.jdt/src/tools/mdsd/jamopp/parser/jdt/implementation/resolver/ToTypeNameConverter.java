package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.types.TypeReference;

public class ToTypeNameConverter {

	private final HashMap<IBinding, String> nameCache;
	private final Provider<ToMethodNameConverter> toMethodNameConverter;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public ToTypeNameConverter(Provider<ToMethodNameConverter> toMethodNameConverter,
			ToFieldNameConverter toFieldNameConverter, HashMap<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.toMethodNameConverter = toMethodNameConverter;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	public String convertToTypeName(ITypeBinding binding) {
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
			IBinding b = binding.getDeclaringMember();
			if (b instanceof IMethodBinding) {
				qualifiedName = toMethodNameConverter.get().convertToMethodName((IMethodBinding) b) + "."
						+ binding.getKey();
			} else if (b instanceof IVariableBinding) {
				qualifiedName = toFieldNameConverter.convertToFieldName((IVariableBinding) b) + "." + binding.getKey();
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

	public String convertToTypeName(TypeReference ref) {
		if (ref instanceof tools.mdsd.jamopp.model.java.types.ClassifierReference convRef) {
			if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) {
				return ((tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) convRef.getTarget())
						.getQualifiedName();
			}
			if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.types.InferableType) {
				return "var";
			}
			return ((tools.mdsd.jamopp.model.java.generics.TypeParameter) convRef.getTarget()).getName();
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference nRef) {
			if (!nRef.getClassifierReferences().isEmpty()) {
				return convertToTypeName(nRef.getClassifierReferences().get(nRef.getClassifierReferences().size() - 1));
			}
			return nRef.getNamespacesAsString();
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Boolean) {
			return "boolean";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Byte) {
			return "byte";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Char) {
			return "char";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Double) {
			return "double";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Float) {
			return "float";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Int) {
			return "int";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Long) {
			return "long";
		} else if (ref instanceof tools.mdsd.jamopp.model.java.types.Short) {
			return "short";
		} else {
			return "void";
		}
	}

}
