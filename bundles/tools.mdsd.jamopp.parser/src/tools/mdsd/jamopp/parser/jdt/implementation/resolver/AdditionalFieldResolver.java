package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.MembersFactory;

public class AdditionalFieldResolver extends ResolverAbstract<AdditionalField, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public AdditionalFieldResolver(HashMap<String, AdditionalField> bindings,
			HashSet<IVariableBinding> variableBindings, MembersFactory membersFactory,
			ClassifierResolver classifierResolver, ToFieldNameConverter toFieldNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public AdditionalField getByBinding(IVariableBinding binding) {
		String varName = toFieldNameConverter.convertToFieldName(binding);
		if (getBindings().containsKey(varName)) {
			return getBindings().get(varName);
		}
		variableBindings.add(binding);
		AdditionalField result = null;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) classifierResolver
				.getClassifier(binding.getDeclaringClass());
		if (potClass != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Field field) {
					boolean leave = false;
					for (AdditionalField af : field.getAdditionalFields()) {
						if (af.getName().equals(binding.getName())) {
							result = af;
							leave = true;
							break;
						}
					}
					if (leave) {
						break;
					}
				}
			}
		}
		if (result == null) {
			result = membersFactory.createAdditionalField();
		}
		getBindings().put(varName, result);
		return result;
	}

	@Override
	public AdditionalField getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		AdditionalField result = membersFactory.createAdditionalField();
		getBindings().put(name, result);
		return result;
	}

}
