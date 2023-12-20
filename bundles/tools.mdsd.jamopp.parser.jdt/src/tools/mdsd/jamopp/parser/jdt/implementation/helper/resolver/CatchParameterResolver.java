package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class CatchParameterResolver extends ResolverAbstract<CatchParameter, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final ParametersFactory parametersFactory;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	public CatchParameterResolver(HashMap<IBinding, String> nameCache, HashMap<String, CatchParameter> bindings,
			HashSet<IVariableBinding> variableBindings, ParametersFactory parametersFactory,
			UtilJdtResolverImpl utilJdtResolverImpl) {
		super(nameCache, bindings);
		this.variableBindings = variableBindings;
		this.parametersFactory = parametersFactory;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public CatchParameter getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return utilJdtResolverImpl.getCatchParameter(utilJdtResolverImpl.convertToParameterName(binding, true));
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
