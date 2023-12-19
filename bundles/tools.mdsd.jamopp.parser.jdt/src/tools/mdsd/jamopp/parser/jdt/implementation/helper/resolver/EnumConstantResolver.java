package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.MembersFactory;

public class EnumConstantResolver extends ResolverAbstract<EnumConstant, IVariableBinding> {

	private final MembersFactory membersFactory;
	private final HashSet<IVariableBinding> variableBindings;
	private final EnumerationResolver enumerationResolver;

	public EnumConstantResolver(HashMap<IBinding, String> nameCache, HashMap<String, EnumConstant> bindings,
			HashSet<IVariableBinding> variableBindings, MembersFactory membersFactory,
			EnumerationResolver enumerationResolver) {
		super(nameCache, bindings);
		this.membersFactory = membersFactory;
		this.variableBindings = variableBindings;
		this.enumerationResolver = enumerationResolver;
	}

	@Override
	public EnumConstant getByBinding(IVariableBinding binding) {
		String enumCN = convertToFieldName(binding);
		if (getBindings().containsKey(enumCN)) {
			return getBindings().get(enumCN);
		}
		variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.Enumeration potPar = enumerationResolver
				.getByBinding(binding.getDeclaringClass());
		tools.mdsd.jamopp.model.java.members.EnumConstant result = null;
		if (potPar != null) {
			for (tools.mdsd.jamopp.model.java.members.EnumConstant con : potPar.getConstants()) {
				if (con.getName().equals(binding.getName())) {
					result = con;
					break;
				}
			}
		}
		if (result == null) {
			result = membersFactory.createEnumConstant();
		}
		getBindings().put(enumCN, result);
		return result;
	}

	@Override
	public EnumConstant getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.members.EnumConstant result = membersFactory.createEnumConstant();
		getBindings().put(name, result);
		return result;
	}

}
