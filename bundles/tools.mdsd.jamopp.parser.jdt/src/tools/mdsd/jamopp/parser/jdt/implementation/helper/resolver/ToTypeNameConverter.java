package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ToTypeNameConverter {

	private final HashMap<IBinding, String> nameCache;
	private final Provider<ToMethodNameConverter> toMethodNameConverter;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public ToTypeNameConverter(HashMap<IBinding, String> nameCache,
			Provider<ToMethodNameConverter> toMethodNameConverter, ToFieldNameConverter toFieldNameConverter) {
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

}
