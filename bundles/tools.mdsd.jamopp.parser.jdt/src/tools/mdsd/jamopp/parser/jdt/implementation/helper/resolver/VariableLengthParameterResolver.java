package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class VariableLengthParameterResolver extends ResolverAbstract<VariableLengthParameter, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final UtilJdtResolverImpl utilJdtResolverImpl;
	private final ParametersFactory parametersFactory;

	public VariableLengthParameterResolver(HashMap<IBinding, String> nameCache,
			HashMap<String, VariableLengthParameter> bindings, HashSet<IVariableBinding> variableBindings,
			UtilJdtResolverImpl utilJdtResolverImpl, ParametersFactory parametersFactory) {
		super(nameCache, bindings);
		this.variableBindings = variableBindings;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
		this.parametersFactory = parametersFactory;
	}

	@Override
	public VariableLengthParameter getByBinding(IVariableBinding binding) {
		String paramName = utilJdtResolverImpl.convertToParameterName(binding, true);
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
