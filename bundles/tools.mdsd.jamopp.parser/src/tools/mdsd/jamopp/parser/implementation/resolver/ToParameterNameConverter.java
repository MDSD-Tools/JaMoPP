package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class ToParameterNameConverter {

	private final Map<IVariableBinding, Integer> varBindToUid;
	private final Map<IBinding, String> nameCache;
	private final UidManager uidManager;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ToParameterNameConverter(final Map<IVariableBinding, Integer> varBindToUid, final UidManager uidManager,
			final ToMethodNameConverter toMethodNameConverter, final Map<IBinding, String> nameCache) {
		this.varBindToUid = varBindToUid;
		this.nameCache = nameCache;
		this.uidManager = uidManager;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	public String convertToParameterName(final IVariableBinding binding, final boolean register) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			String prefix;
			if (binding.getDeclaringMethod() != null) {
				prefix = toMethodNameConverter.convertToMethodName(binding.getDeclaringMethod());
			} else if (varBindToUid.containsKey(binding)) {
				prefix = String.valueOf(varBindToUid.get(binding));
			} else {
				prefix = String.valueOf(uidManager.getUid());
				if (register) {
					varBindToUid.put(binding, uidManager.getUid());
				}
			}
			final String name = prefix + "::" + binding.getName() + "::" + binding.getVariableId() + binding.hashCode();
			nameCache.put(binding, name);
			result = name;
		}
		return result;
	}

}
