package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ToTypeNameConverter {

	private final HashMap<IBinding, String> nameCache;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public ToTypeNameConverter(UtilJdtResolverImpl utilJdtResolverImpl, HashMap<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
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
				qualifiedName = utilJdtResolverImpl.convertToMethodName((IMethodBinding) b) + "." + binding.getKey();
			} else if (b instanceof IVariableBinding) {
				qualifiedName = utilJdtResolverImpl.convertToFieldName((IVariableBinding) b) + "." + binding.getKey();
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
