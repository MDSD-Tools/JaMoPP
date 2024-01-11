package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

public class LocalVariableResolver extends ResolverAbstract<LocalVariable, IVariableBinding> {

	private final VariablesFactory variablesFactory;
	private final Set<IVariableBinding> variableBindings;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public LocalVariableResolver(Map<String, LocalVariable> bindings, VariablesFactory variablesFactory,
			Set<IVariableBinding> variableBindings, ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variablesFactory = variablesFactory;
		this.variableBindings = variableBindings;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public LocalVariable getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public LocalVariable getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		LocalVariable result = variablesFactory.createLocalVariable();
		getBindings().put(name, result);
		return result;
	}

}
