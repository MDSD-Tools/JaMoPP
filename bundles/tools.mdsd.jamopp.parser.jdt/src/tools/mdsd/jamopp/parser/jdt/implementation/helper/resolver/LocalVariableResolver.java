package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class LocalVariableResolver extends ResolverAbstract<LocalVariable, IVariableBinding> {

	private final VariablesFactory variablesFactory;
	private final HashSet<IVariableBinding> variableBindings;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public LocalVariableResolver(HashMap<IBinding, String> nameCache, HashMap<String, LocalVariable> bindings,
			VariablesFactory variablesFactory, HashSet<IVariableBinding> variableBindings,
			UtilJdtResolverImpl utilJdtResolverImpl) {
		super(nameCache, bindings);
		this.variablesFactory = variablesFactory;
		this.variableBindings = variableBindings;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public LocalVariable getByBinding(IVariableBinding binding) {
		variableBindings.add(binding);
		return utilJdtResolverImpl.getLocalVariable(utilJdtResolverImpl.convertToParameterName(binding, true));
	}

	@Override
	public LocalVariable getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.variables.LocalVariable result = variablesFactory.createLocalVariable();
		getBindings().put(name, result);
		return result;
	}

}
