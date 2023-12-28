package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ToParameterNameConverter {

	private final HashMap<IVariableBinding, Integer> varBindToUid;
	private final HashMap<IBinding, String> nameCache;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public ToParameterNameConverter(HashMap<IVariableBinding, Integer> varBindToUid,
			UtilJdtResolverImpl utilJdtResolverImpl, HashMap<IBinding, String> nameCache) {
		this.varBindToUid = varBindToUid;
		this.nameCache = nameCache;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	public String convertToParameterName(IVariableBinding binding, boolean register, int uid) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String prefix = "";
		if (binding.getDeclaringMethod() != null) {
			prefix = utilJdtResolverImpl.convertToMethodName(binding.getDeclaringMethod());
		} else if (varBindToUid.containsKey(binding)) {
			prefix = varBindToUid.get(binding) + "";
		} else {
			prefix = uid + "";
			if (register) {
				varBindToUid.put(binding, uid);
			}
		}
		String name = prefix + "::" + binding.getName() + "::" + binding.getVariableId() + binding.hashCode();
		nameCache.put(binding, name);
		return name;
	}

}
