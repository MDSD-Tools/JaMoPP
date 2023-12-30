package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.MembersFactory;

public class AdditionalFieldResolver extends ResolverAbstract<AdditionalField, IVariableBinding> {

	private final HashSet<IVariableBinding> variableBindings;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;

	@Inject
	public AdditionalFieldResolver(HashMap<IBinding, String> nameCache, HashMap<String, AdditionalField> bindings,
			HashSet<IVariableBinding> variableBindings, MembersFactory membersFactory,
			ClassifierResolver classifierResolver) {
		super(nameCache, bindings);
		this.variableBindings = variableBindings;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
	}

	@Override
	public AdditionalField getByBinding(IVariableBinding binding) {
		String varName = convertToFieldName(binding);
		if (getBindings().containsKey(varName)) {
			return getBindings().get(varName);
		}
		variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.members.AdditionalField result = null;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) classifierResolver
				.getClassifier(binding.getDeclaringClass());
		if (potClass != null) {
			outerLoop: for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Field field) {
					for (tools.mdsd.jamopp.model.java.members.AdditionalField af : field.getAdditionalFields()) {
						if (af.getName().equals(binding.getName())) {
							result = af;
							break outerLoop;
						}
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
		tools.mdsd.jamopp.model.java.members.AdditionalField result = membersFactory.createAdditionalField();
		getBindings().put(name, result);
		return result;
	}

}
