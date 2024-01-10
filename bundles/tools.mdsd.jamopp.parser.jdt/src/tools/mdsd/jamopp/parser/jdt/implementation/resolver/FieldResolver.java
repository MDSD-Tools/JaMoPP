package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public class FieldResolver extends ResolverAbstract<Field, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public FieldResolver(HashMap<String, Field> bindings, HashSet<IVariableBinding> variableBindings,
			TypesFactory typesFactory, MembersFactory membersFactory, ClassifierResolver classifierResolver,
			ToFieldNameConverter toFieldNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public Field getByBinding(IVariableBinding binding) {
		String varName = toFieldNameConverter.convertToFieldName(binding);
		if (getBindings().containsKey(varName)) {
			return getBindings().get(varName);
		}
		variableBindings.add(binding);
		ConcreteClassifier potClass = null;
		if (binding.getDeclaringClass() != null) {
			potClass = (ConcreteClassifier) classifierResolver.getClassifier(binding.getDeclaringClass());
		}
		Field result = null;
		if (potClass != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof Field && mem.getName().equals(binding.getName())) {
					result = (Field) mem;
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
		Field result = membersFactory.createField();
		getBindings().put(name, result);
		return result;
	}

}
