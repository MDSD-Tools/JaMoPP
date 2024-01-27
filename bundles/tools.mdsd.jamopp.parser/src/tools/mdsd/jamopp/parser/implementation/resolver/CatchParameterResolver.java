package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;

public class CatchParameterResolver extends ResolverAbstract<CatchParameter, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public CatchParameterResolver(final Map<String, CatchParameter> bindings,
			final Set<IVariableBinding> variableBindings, final ParametersFactory parametersFactory,
			final ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.parametersFactory = parametersFactory;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public CatchParameter getByBinding(final IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public CatchParameter getByName(final String name) {
		CatchParameter catchParameter;
		if (getBindings().containsKey(name)) {
			catchParameter = getBindings().get(name);
		} else {
			final CatchParameter result = parametersFactory.createCatchParameter();
			getBindings().put(name, result);
			catchParameter = result;
		}
		return catchParameter;
	}

}
