package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class FieldResolver extends ResolverAbstract<Field, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public FieldResolver(HashMap<IBinding, String> nameCache, HashMap<String, Field> bindings,
			HashSet<IVariableBinding> variableBindings, UtilJdtResolverImpl utilJdtResolverImpl,
			TypesFactory typesFactory, MembersFactory membersFactory) {
		super(nameCache, bindings);
		this.variableBindings = variableBindings;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public Field getByBinding(IVariableBinding binding) {
		String varName = convertToFieldName(binding);
		if (getBindings().containsKey(varName)) {
			return getBindings().get(varName);
		}
		variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = null;
		if (binding.getDeclaringClass() != null) {
			potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) utilJdtResolverImpl
					.getClassifier(binding.getDeclaringClass());
		}
		tools.mdsd.jamopp.model.java.members.Field result = null;
		if (potClass != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Field
						&& mem.getName().equals(binding.getName())) {
					result = (tools.mdsd.jamopp.model.java.members.Field) mem;
					break;
				}
			}
		}
		if (result == null) {
			result = membersFactory.createField();
			result.setTypeReference(typesFactory.createInt());
		}
		getBindings().put(varName, result);
		return result;
	}

	@Override
	public Field getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.members.Field result = membersFactory.createField();
		getBindings().put(name, result);
		return result;
	}

}
