package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ConverterWithBoolean;
import tools.mdsd.jamopp.parser.interfaces.resolver.UidManager;

public class ToParameterNameConverter implements ConverterWithBoolean<IVariableBinding> {

	private final Map<IVariableBinding, Integer> varBindToUid;
	private final Map<IBinding, String> nameCache;
	private final UidManager uidManagerImpl;
	private final ToStringConverter<IMethodBinding> toMethodNameConverter;

	@Inject
	public ToParameterNameConverter(final Map<IVariableBinding, Integer> varBindToUid, final UidManager uidManagerImpl,
			final ToStringConverter<IMethodBinding> toMethodNameConverter, final Map<IBinding, String> nameCache) {
		this.varBindToUid = varBindToUid;
		this.nameCache = nameCache;
		this.uidManagerImpl = uidManagerImpl;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	@Override
	public String convertToParameterName(final IVariableBinding binding, final boolean register) {
		String result;
		if (binding == null) {
			result = "";
		} else if (nameCache.containsKey(binding)) {
			result = nameCache.get(binding);
		} else {
			String prefix;
			if (binding.getDeclaringMethod() != null) {
				prefix = toMethodNameConverter.convert(binding.getDeclaringMethod());
			} else if (varBindToUid.containsKey(binding)) {
				prefix = String.valueOf(varBindToUid.get(binding));
			} else {
				prefix = String.valueOf(uidManagerImpl.getUid());
				if (register) {
					varBindToUid.put(binding, uidManagerImpl.getUid());
				}
			}
			final String name = prefix + "::" + binding.getName() + "::" + binding.getVariableId() + binding.hashCode();
			nameCache.put(binding, name);
			result = name;
		}
		return result;
	}

}
