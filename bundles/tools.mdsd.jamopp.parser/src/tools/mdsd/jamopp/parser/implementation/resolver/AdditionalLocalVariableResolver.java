package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.ConverterWithBoolean;

public class AdditionalLocalVariableResolver
		extends AbstractResolverWithCache<AdditionalLocalVariable, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final VariablesFactory variablesFactory;
	private final ConverterWithBoolean<IVariableBinding> toParameterNameConverter;

	@Inject
	public AdditionalLocalVariableResolver(final Map<String, AdditionalLocalVariable> bindings,
			final VariablesFactory variablesFactory, final Set<IVariableBinding> variableBindings,
			final ConverterWithBoolean<IVariableBinding> toParameterNameConverter) {
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
		if (containsKey(name)) {
			additionalLocalVariable = get(name);
		} else {
			final AdditionalLocalVariable result = variablesFactory.createAdditionalLocalVariable();
			putBinding(name, result);
			additionalLocalVariable = result;
		}
		return additionalLocalVariable;
	}

}
