package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class FieldResolver extends AbstractResolverWithCache<Field, IVariableBinding> {

	private final Set<IVariableBinding> variableBindings;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;
	private final ToStringConverter<IVariableBinding> toFieldNameConverter;

	@Inject
	public FieldResolver(final Map<String, Field> bindings, final Set<IVariableBinding> variableBindings,
			final TypesFactory typesFactory, final MembersFactory membersFactory,
			final Resolver<Classifier, ITypeBinding> classifierResolver,
			final ToStringConverter<IVariableBinding> toFieldNameConverter) {
		super(bindings);
		this.variableBindings = variableBindings;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public Field getByBinding(final IVariableBinding binding) {
		final String varName = toFieldNameConverter.convert(binding);
		Field field;
		if (containsKey(varName)) {
			field = get(varName);
		} else {
			field = getByBinding(binding, varName);
		}
		return field;
	}

	@Override
	public Field getByName(final String name) {
		Field field;
		if (containsKey(name)) {
			field = get(name);
		} else {
			final Field result = membersFactory.createField();
			putBinding(name, result);
			field = result;
		}
		return field;
	}

	private Field getByBinding(final IVariableBinding binding, final String varName) {
		variableBindings.add(binding);
		ConcreteClassifier potClass = null;
		if (binding.getDeclaringClass() != null) {
			potClass = (ConcreteClassifier) classifierResolver.getByBinding(binding.getDeclaringClass());
		}
		Field result = null;
		if (potClass != null) {
			for (final tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
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
		putBinding(varName, result);
		return result;
	}

}
