package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public class FieldResolver extends ResolverAbstract<Field, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public FieldResolver(Map<String, Field> bindings, Set<IVariableBinding> variableBindings, TypesFactory typesFactory,
			MembersFactory membersFactory, ClassifierResolver classifierResolver,
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
		Field field;
		if (getBindings().containsKey(varName)) {
			field = getBindings().get(varName);
		} else {
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
			field = result;
		}
		return field;
	}

	@Override
	public Field getByName(String name) {
		Field field;
		if (getBindings().containsKey(name)) {
			field = getBindings().get(name);
		} else {
			Field result = membersFactory.createField();
			getBindings().put(name, result);
			field = result;
		}
		return field;
	}

}
