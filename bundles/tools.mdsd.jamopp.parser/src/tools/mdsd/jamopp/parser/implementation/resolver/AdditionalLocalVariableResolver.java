package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

public class AdditionalLocalVariableResolver extends ResolverAbstract<AdditionalLocalVariable, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final VariablesFactory variablesFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public AdditionalLocalVariableResolver(final Map<String, AdditionalLocalVariable> bindings,
			final VariablesFactory variablesFactory, final Set<IVariableBinding> variableBindings,
			final ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.variablesFactory = variablesFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public AdditionalLocalVariable getByBinding(final IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public AdditionalLocalVariable getByName(final String name) {
		AdditionalLocalVariable additionalLocalVariable;
		if (getBindings().containsKey(name)) {
			additionalLocalVariable = getBindings().get(name);
		} else {
			final AdditionalLocalVariable result = variablesFactory.createAdditionalLocalVariable();
			getBindings().put(name, result);
			additionalLocalVariable = result;
		}
		return additionalLocalVariable;
	}

}
