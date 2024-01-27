package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;

public class VariableLengthParameterResolver extends ResolverAbstract<VariableLengthParameter, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public VariableLengthParameterResolver(final Map<String, VariableLengthParameter> bindings,
			final Set<IVariableBinding> variableBindings, final ParametersFactory parametersFactory,
			final ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.parametersFactory = parametersFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public VariableLengthParameter getByBinding(final IVariableBinding binding) {
		VariableLengthParameter variableLengthParameter;
		final String paramName = toParameterNameConverter.convertToParameterName(binding, true);
		if (getBindings().containsKey(paramName)) {
			variableLengthParameter = getBindings().get(paramName);
		} else {
			variableBindings.add(binding);
			final VariableLengthParameter result = parametersFactory.createVariableLengthParameter();
			getBindings().put(paramName, result);
			variableLengthParameter = result;
		}
		return variableLengthParameter;
	}

	@Override
	public VariableLengthParameter getByName(final String name) {
		throw new UnsupportedOperationException("Not implemented");
	}

}
