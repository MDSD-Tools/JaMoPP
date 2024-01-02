package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IVariableBinding;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

public class LocalVariableResolver extends ResolverAbstract<LocalVariable, IVariableBinding> {

	private final VariablesFactory variablesFactory;
	private final HashSet<IVariableBinding> variableBindings;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public LocalVariableResolver(HashMap<String, LocalVariable> bindings, VariablesFactory variablesFactory,
			HashSet<IVariableBinding> variableBindings, ToParameterNameConverter toParameterNameConverter) {
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
		tools.mdsd.jamopp.model.java.variables.LocalVariable result = variablesFactory.createLocalVariable();
		getBindings().put(name, result);
		return result;
	}

}
