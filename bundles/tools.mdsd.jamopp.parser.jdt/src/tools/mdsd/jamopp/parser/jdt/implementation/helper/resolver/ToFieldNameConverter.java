package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ToFieldNameConverter {

	private final HashMap<IBinding, String> nameCache;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	public ToFieldNameConverter(UtilJdtResolverImpl utilJdtResolverImpl, HashMap<IBinding, String> nameCache) {
		this.nameCache = nameCache;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	public String convertToFieldName(IVariableBinding binding) {
		if (binding == null || !binding.isField()) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String name = utilJdtResolverImpl.convertToTypeName(binding.getDeclaringClass()) + "::" + binding.getName();
		nameCache.put(binding, name);
		return name;
	}

}
