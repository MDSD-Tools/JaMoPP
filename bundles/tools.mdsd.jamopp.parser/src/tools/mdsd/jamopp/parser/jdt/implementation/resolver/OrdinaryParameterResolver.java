package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;

public class OrdinaryParameterResolver extends ResolverAbstract<OrdinaryParameter, IVariableBinding> {

	private final ParametersFactory parametersFactory;
	private final Set<IVariableBinding> variableBindings;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public OrdinaryParameterResolver(Map<String, OrdinaryParameter> bindings, ParametersFactory parametersFactory,
			Set<IVariableBinding> variableBindings, ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.parametersFactory = parametersFactory;
		this.variableBindings = variableBindings;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public OrdinaryParameter getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public OrdinaryParameter getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		OrdinaryParameter result = parametersFactory.createOrdinaryParameter();
		getBindings().put(name, result);
		return result;
	}

}
