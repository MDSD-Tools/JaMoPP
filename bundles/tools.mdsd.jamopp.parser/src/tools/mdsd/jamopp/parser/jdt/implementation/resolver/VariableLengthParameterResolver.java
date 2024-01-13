package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;

public class VariableLengthParameterResolver extends ResolverAbstract<VariableLengthParameter, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public VariableLengthParameterResolver(Map<String, VariableLengthParameter> bindings,
			Set<IVariableBinding> variableBindings, ParametersFactory parametersFactory,
			ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.parametersFactory = parametersFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public VariableLengthParameter getByBinding(IVariableBinding binding) {
		VariableLengthParameter variableLengthParameter;
		String paramName = toParameterNameConverter.convertToParameterName(binding, true);
		if (getBindings().containsKey(paramName)) {
			variableLengthParameter = getBindings().get(paramName);
		} else {
			variableBindings.add(binding);
			VariableLengthParameter result = parametersFactory.createVariableLengthParameter();
			getBindings().put(paramName, result);
			variableLengthParameter = result;
		}
		return variableLengthParameter;
	}

	@Override
	public VariableLengthParameter getByName(String name) {
		throw new RuntimeException("Not implemented");
	}

}
