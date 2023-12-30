package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;

public class CatchParameterResolver extends ResolverAbstract<CatchParameter, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public CatchParameterResolver(HashMap<String, CatchParameter> bindings, HashSet<IVariableBinding> variableBindings,
			ParametersFactory parametersFactory, ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.parametersFactory = parametersFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public CatchParameter getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public CatchParameter getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.parameters.CatchParameter result = parametersFactory.createCatchParameter();
		getBindings().put(name, result);
		return result;
	}

}
