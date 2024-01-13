package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.MembersFactory;

public class AdditionalFieldResolver extends ResolverAbstract<AdditionalField, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public AdditionalFieldResolver(Map<String, AdditionalField> bindings, Set<IVariableBinding> variableBindings,
			MembersFactory membersFactory, ClassifierResolver classifierResolver,
			ToFieldNameConverter toFieldNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public AdditionalField getByBinding(IVariableBinding binding) {
		String varName = toFieldNameConverter.convertToFieldName(binding);
		AdditionalField additionalField;
		if (getBindings().containsKey(varName)) {
			additionalField = getBindings().get(varName);
		} else {
			variableBindings.add(binding);
			AdditionalField result = null;
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) classifierResolver
					.getClassifier(binding.getDeclaringClass());
			if (potClass != null) {
				result = handleNullPotClass(binding, result, potClass);
			}
			if (result == null) {
				result = membersFactory.createAdditionalField();
			}
			getBindings().put(varName, result);
			additionalField = result;
		}
		return additionalField;
	}

	private AdditionalField handleNullPotClass(IVariableBinding binding, AdditionalField result,
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass) {
		AdditionalField additionalField = result;
		for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
			if (mem instanceof tools.mdsd.jamopp.model.java.members.Field field) {
				boolean leave = false;
				for (AdditionalField af : field.getAdditionalFields()) {
					if (af.getName().equals(binding.getName())) {
						additionalField = af;
						leave = true;
						break;
					}
				}
				if (leave) {
					break;
				}
			}
		}
		return additionalField;
	}

	@Override
	public AdditionalField getByName(String name) {
		AdditionalField additionalField;
		if (getBindings().containsKey(name)) {
			additionalField = getBindings().get(name);
		} else {
			AdditionalField result = membersFactory.createAdditionalField();
			getBindings().put(name, result);
			additionalField = result;
		}
		return additionalField;
	}

}
