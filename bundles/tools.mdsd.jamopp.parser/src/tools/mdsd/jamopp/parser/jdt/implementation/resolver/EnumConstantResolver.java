package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.MembersFactory;

public class EnumConstantResolver extends ResolverAbstract<EnumConstant, IVariableBinding> {

	private final MembersFactory membersFactory;
	private final Set<IVariableBinding> variableBindings;
	private final EnumerationResolver enumerationResolver;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public EnumConstantResolver(Map<String, EnumConstant> bindings, Set<IVariableBinding> variableBindings,
			MembersFactory membersFactory, EnumerationResolver enumerationResolver,
			ToFieldNameConverter toFieldNameConverter) {
		super(bindings);
		this.membersFactory = membersFactory;
		this.variableBindings = variableBindings;
		this.enumerationResolver = enumerationResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public EnumConstant getByBinding(IVariableBinding binding) {
		String enumCN = toFieldNameConverter.convertToFieldName(binding);
		EnumConstant enumConstant;
		if (getBindings().containsKey(enumCN)) {
			enumConstant = getBindings().get(enumCN);
		} else {
			variableBindings.add(binding);
			tools.mdsd.jamopp.model.java.classifiers.Enumeration potPar = enumerationResolver
					.getByBinding(binding.getDeclaringClass());
			EnumConstant result = null;
			if (potPar != null) {
				for (EnumConstant con : potPar.getConstants()) {
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
			enumConstant = result;
		}
		return enumConstant;
	}

	@Override
	public EnumConstant getByName(String name) {
		EnumConstant enumConstant;
		if (getBindings().containsKey(name)) {
			enumConstant = getBindings().get(name);
		} else {
			EnumConstant result = membersFactory.createEnumConstant();
			getBindings().put(name, result);
			enumConstant = result;
		}
		return enumConstant;
	}

}
