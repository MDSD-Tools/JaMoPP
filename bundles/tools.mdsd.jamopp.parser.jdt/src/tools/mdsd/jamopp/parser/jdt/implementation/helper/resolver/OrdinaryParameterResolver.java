package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.IBinding;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;

public class OrdinaryParameterResolver extends ResolverAbstract<OrdinaryParameter, IBinding> {

	private final ParametersFactory parametersFactory;

	public OrdinaryParameterResolver(HashMap<IBinding, String> nameCache, HashMap<String, OrdinaryParameter> bindings,
			ParametersFactory parametersFactory) {
		super(nameCache, bindings);
		this.parametersFactory = parametersFactory;
	}

	@Override
	public OrdinaryParameter getByBinding(IBinding binding) {
		throw new RuntimeException("Not implemented");
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
