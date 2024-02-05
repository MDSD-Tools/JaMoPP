package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;

public class OrdinaryParameterResolver extends AbstractResolverWithCache<OrdinaryParameter, IVariableBinding> {

	private final ParametersFactory parametersFactory;
	private final Set<IVariableBinding> variableBindings;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public OrdinaryParameterResolver(final Map<String, OrdinaryParameter> bindings,
			final ParametersFactory parametersFactory, final Set<IVariableBinding> variableBindings,
			final ToParameterNameConverter toParameterNameConverter) {
		super(bindings);
		this.parametersFactory = parametersFactory;
		this.variableBindings = variableBindings;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	@Override
	public OrdinaryParameter getByBinding(final IVariableBinding binding) {
		variableBindings.add(binding);
		return getByName(toParameterNameConverter.convertToParameterName(binding, true));
	}

	@Override
	public OrdinaryParameter getByName(final String name) {
		OrdinaryParameter ordinaryParameter;
		if (containsKey(name)) {
			ordinaryParameter = get(name);
		} else {
			final OrdinaryParameter result = parametersFactory.createOrdinaryParameter();
			putBinding(name, result);
			ordinaryParameter = result;
		}
		return ordinaryParameter;
	}

}
