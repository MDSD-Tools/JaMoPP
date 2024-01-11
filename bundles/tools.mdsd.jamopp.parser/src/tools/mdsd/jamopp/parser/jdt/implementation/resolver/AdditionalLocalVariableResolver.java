package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

public class AdditionalLocalVariableResolver extends ResolverAbstract<AdditionalLocalVariable, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final VariablesFactory variablesFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public AdditionalLocalVariableResolver(Map<String, AdditionalLocalVariable> bindings,
			VariablesFactory variablesFactory, Set<IVariableBinding> variableBindings,
			ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.variablesFactory = variablesFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public AdditionalLocalVariable getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public AdditionalLocalVariable getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		AdditionalLocalVariable result = variablesFactory.createAdditionalLocalVariable();
		getBindings().put(name, result);
		return result;
	}

}
