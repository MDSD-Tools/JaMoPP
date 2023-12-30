package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;

public class VariableLengthParameterResolver extends ResolverAbstract<VariableLengthParameter, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public VariableLengthParameterResolver(HashMap<IBinding, String> nameCache,
			HashMap<String, VariableLengthParameter> bindings, HashSet<IVariableBinding> variableBindings,
			ParametersFactory parametersFactory, ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.parametersFactory = parametersFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public VariableLengthParameter getByBinding(IVariableBinding binding) {
		String paramName = toParameterNameConverter.convertToParameterName(binding, true);
		if (getBindings().containsKey(paramName)) {
			return getBindings().get(paramName);
		}
		variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter result = parametersFactory
				.createVariableLengthParameter();
		getBindings().put(paramName, result);
		return result;
	}

	@Override
	public VariableLengthParameter getByName(String name) {
		throw new RuntimeException("Not implemented");
	}

}
