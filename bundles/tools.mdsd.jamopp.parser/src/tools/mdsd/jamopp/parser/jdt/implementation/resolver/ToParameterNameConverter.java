package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class ToParameterNameConverter {

	private final Map<IVariableBinding, Integer> varBindToUid;
	private final Map<IBinding, String> nameCache;
	private final UidManager uidManager;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ToParameterNameConverter(Map<IVariableBinding, Integer> varBindToUid, UidManager uidManager,
			ToMethodNameConverter toMethodNameConverter, Map<IBinding, String> nameCache) {
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
