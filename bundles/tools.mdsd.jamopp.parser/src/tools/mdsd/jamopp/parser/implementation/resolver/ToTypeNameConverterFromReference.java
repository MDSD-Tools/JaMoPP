package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class ToTypeNameConverterFromReference implements ToStringConverter<TypeReference> {

	private final Map<Class<?>, Function<TypeReference, String>> mapping;

	public ToTypeNameConverterFromReference() {
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

	@Override
	public String convert(final TypeReference ref) {
		String result = null;
		for (final Entry<Class<?>, Function<TypeReference, String>> entry : mapping.entrySet()) {
			final Class<?> key = entry.getKey();
			final Function<TypeReference, String> val = entry.getValue();
			if (key.isInstance(ref)) {
				result = val.apply(ref);
				break;
			}
		}
		return result;
	}

	private String convertClassifierReference(final ClassifierReference convRef) {
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

	private String convertNamespaceClassifierReference(final NamespaceClassifierReference nRef) {
		String result;
		if (nRef.getClassifierReferences().isEmpty()) {
			result = nRef.getNamespacesAsString();
		} else {
			result = convert(nRef.getClassifierReferences().get(nRef.getClassifierReferences().size() - 1));
		}
		return result;
	}

}
