package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.MembersFactory;

public class AdditionalFieldResolver extends ResolverWithCache<AdditionalField, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final ToFieldNameConverter toFieldNameConverter;

	@Inject
	public AdditionalFieldResolver(final Map<String, AdditionalField> bindings,
			final Set<IVariableBinding> variableBindings, final MembersFactory membersFactory,
			final ClassifierResolver classifierResolver, final ToFieldNameConverter toFieldNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public AdditionalField getByBinding(final IVariableBinding binding) {
		final String varName = toFieldNameConverter.convertToFieldName(binding);
		AdditionalField additionalField;
		if (containsKey(varName)) {
			additionalField = get(varName);
		} else {
			variableBindings.add(binding);
			AdditionalField result = null;
			final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) classifierResolver
					.getClassifier(binding.getDeclaringClass());
			if (potClass != null) {
				result = handleNullPotClass(binding, potClass);
			}
			if (result == null) {
				result = membersFactory.createAdditionalField();
			}
			putBinding(varName, result);
			additionalField = result;
		}
		return additionalField;
	}

	private AdditionalField handleNullPotClass(final IVariableBinding binding,
			final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass) {
		AdditionalField additionalField = null;
		for (final tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
			if (mem instanceof final tools.mdsd.jamopp.model.java.members.Field field) {
				boolean leave = false;
				for (final AdditionalField af : field.getAdditionalFields()) {
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
	public AdditionalField getByName(final String name) {
		AdditionalField additionalField;
		if (containsKey(name)) {
			additionalField = get(name);
		} else {
			final AdditionalField result = membersFactory.createAdditionalField();
			putBinding(name, result);
			additionalField = result;
		}
		return additionalField;
	}

}
