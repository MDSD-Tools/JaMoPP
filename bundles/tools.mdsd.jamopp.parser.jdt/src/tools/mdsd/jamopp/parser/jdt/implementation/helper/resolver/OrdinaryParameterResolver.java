package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class OrdinaryParameterResolver extends ResolverAbstract<OrdinaryParameter, IVariableBinding> {

	private final ParametersFactory parametersFactory;
	private final HashSet<IVariableBinding> variableBindings;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	public OrdinaryParameterResolver(HashMap<IBinding, String> nameCache, HashMap<String, OrdinaryParameter> bindings,
			ParametersFactory parametersFactory, HashSet<IVariableBinding> variableBindings,
			UtilJdtResolverImpl utilJdtResolverImpl) {
		super(nameCache, bindings);
		this.parametersFactory = parametersFactory;
		this.variableBindings = variableBindings;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public OrdinaryParameter getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		String paramName = utilJdtResolverImpl.convertToParameterName(binding, true);
		return utilJdtResolverImpl.getOrdinaryParameter(paramName);
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
