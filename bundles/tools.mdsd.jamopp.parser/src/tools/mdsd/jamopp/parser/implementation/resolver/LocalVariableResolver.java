package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

public class LocalVariableResolver extends AbstractResolverWithCache<LocalVariable, IVariableBinding> {

	private final VariablesFactory variablesFactory;
	private final Set<IVariableBinding> variableBindings;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public LocalVariableResolver(final Map<String, LocalVariable> bindings, final VariablesFactory variablesFactory,
			final Set<IVariableBinding> variableBindings, final ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variablesFactory = variablesFactory;
		this.variableBindings = variableBindings;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public LocalVariable getByBinding(final IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public LocalVariable getByName(final String name) {
		LocalVariable localVariable;
		if (containsKey(name)) {
			localVariable = get(name);
		} else {
			final LocalVariable result = variablesFactory.createLocalVariable();
			putBinding(name, result);
			localVariable = result;
		}
		return localVariable;
	}

}
