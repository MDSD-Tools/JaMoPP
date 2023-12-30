package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

public class AdditionalLocalVariableResolver extends ResolverAbstract<AdditionalLocalVariable, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final VariablesFactory variablesFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public AdditionalLocalVariableResolver(HashMap<String, AdditionalLocalVariable> bindings,
			VariablesFactory variablesFactory, HashSet<IVariableBinding> variableBindings,
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
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable result = variablesFactory
				.createAdditionalLocalVariable();
		getBindings().put(name, result);
		return result;
	}

}
