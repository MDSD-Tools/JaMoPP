package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;

public class OrdinaryParameterResolver extends ResolverAbstract<OrdinaryParameter, IVariableBinding> {

	private final ParametersFactory parametersFactory;
	private final HashSet<IVariableBinding> variableBindings;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public OrdinaryParameterResolver(HashMap<String, OrdinaryParameter> bindings, ParametersFactory parametersFactory,
			HashSet<IVariableBinding> variableBindings, ToParameterNameConverter toParameterNameConverter) {
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
		tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter result = parametersFactory.createOrdinaryParameter();
		getBindings().put(name, result);
		return result;
	}

}
