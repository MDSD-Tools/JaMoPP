package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

public class ToParameterNameConverter {

	private final HashMap<IVariableBinding, Integer> varBindToUid;
	private final HashMap<IBinding, String> nameCache;
	private final UidManager uidManager;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ToParameterNameConverter(HashMap<IVariableBinding, Integer> varBindToUid,
			HashMap<IBinding, String> nameCache, UidManager uidManager, ToMethodNameConverter toMethodNameConverter) {
		this.varBindToUid = varBindToUid;
		this.nameCache = nameCache;
		this.uidManager = uidManager;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	public String convertToParameterName(IVariableBinding binding, boolean register) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String prefix = "";
		if (binding.getDeclaringMethod() != null) {
			prefix = toMethodNameConverter.convertToMethodName(binding.getDeclaringMethod());
		} else if (varBindToUid.containsKey(binding)) {
			prefix = varBindToUid.get(binding) + "";
		} else {
			prefix = uidManager.getUid() + "";
			if (register) {
				varBindToUid.put(binding, uidManager.getUid());
			}
		}
		String name = prefix + "::" + binding.getName() + "::" + binding.getVariableId() + binding.hashCode();
		nameCache.put(binding, name);
		return name;
	}

}
