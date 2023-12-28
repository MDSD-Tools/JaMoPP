package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class AdditionalLocalVariableResolver extends ResolverAbstract<AdditionalLocalVariable, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final VariablesFactory variablesFactory;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public AdditionalLocalVariableResolver(HashMap<IBinding, String> nameCache,
			HashMap<String, AdditionalLocalVariable> bindings, VariablesFactory variablesFactory,
			HashSet<IVariableBinding> variableBindings, UtilJdtResolverImpl utilJdtResolverImpl) {
		super(nameCache, bindings);
		this.variableBindings = variableBindings;
		this.variablesFactory = variablesFactory;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public AdditionalLocalVariable getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return utilJdtResolverImpl
				.getAdditionalLocalVariable(utilJdtResolverImpl.convertToParameterName(binding, true));
	}

	@Override
	public AdditionalLocalVariable getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable result = variablesFactory
				.createAdditionalLocalVariable();
		getBindings().put(name, result);
		return result;
	}

}
