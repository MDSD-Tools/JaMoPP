package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.ConverterWithBoolean;

public class CatchParameterResolver extends AbstractResolverWithCache<CatchParameter, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final ConverterWithBoolean<IVariableBinding> toParameterNameConverter;

	@Inject
	public CatchParameterResolver(final Map<String, CatchParameter> bindings,
			final Set<IVariableBinding> variableBindings, final ParametersFactory parametersFactory,
			final ConverterWithBoolean<IVariableBinding> toParameterNameConverter) {
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
		if (containsKey(name)) {
			catchParameter = get(name);
		} else {
			final CatchParameter result = parametersFactory.createCatchParameter();
			putBinding(name, result);
			catchParameter = result;
		}
		return catchParameter;
	}

}
