package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;

public class ConstructorResolver extends AbstractResolverWithCache<Constructor, IMethodBinding> {

	private final Set<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final ToMethodNameConverter toMethodNameConverter;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public ConstructorResolver(final Map<String, Constructor> bindings, final StatementsFactory statementsFactory,
			final Set<IMethodBinding> methodBindings, final MembersFactory membersFactory,
			final ClassifierResolver classifierResolver, final ToTypeNameConverter toTypeNameConverter,
			final ToMethodNameConverter toMethodNameConverter) {
		super(bindings);
		this.methodBindings = methodBindings;
		this.statementsFactory = statementsFactory;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.toMethodNameConverter = toMethodNameConverter;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Constructor getByBinding(final IMethodBinding binding) {
		final String methName = toMethodNameConverter.convert(binding);
		Constructor constructor;
		if (containsKey(methName)) {
			constructor = get(methName);
		} else {
			methodBindings.add(binding);
			Constructor result = getConstructor(binding);
			if (result == null) {
				result = membersFactory.createConstructor();
				final tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
				block.setName("");
				result.setBlock(block);
			}
			putBinding(methName, result);
			constructor = result;
		}
		return constructor;
	}

	@Override
	public Constructor getByName(final String name) {
		Constructor constructor;
		if (containsKey(name)) {
			constructor = get(name);
		} else {
			final Constructor result = membersFactory.createConstructor();
			final tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
			block.setName("");
			result.setBlock(block);
			putBinding(name, result);
			constructor = result;
		}
		return constructor;
	}

	private Constructor getConstructor(final IMethodBinding binding) {
		Constructor result = null;
		final ConcreteClassifier potClass = (ConcreteClassifier) classifierResolver
				.getClassifier(binding.getDeclaringClass());

		if (potClass != null) {
			for (final tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof final Constructor con && mem.getName().equals(binding.getName())) {
					int receiveOffset = 0;
					if (binding.getDeclaredReceiverType() != null) {
						receiveOffset = 1;
					}
					if (con.getParameters().size() == binding.getParameterTypes().length + receiveOffset) {
						if (shouldSkip(binding, con, receiveOffset)) {
							continue;
						}

						result = con;
						break;
					}
				}
			}
		}
		return result;
	}

	private boolean shouldSkip(final IMethodBinding binding, final Constructor con, final int receiveOffset) {
		return receiveOffset == 1
				&& (!(con.getParameters().get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
						|| !toTypeNameConverter.convert(binding.getDeclaredReceiverType()).equals(
								toTypeNameConverter.convertToTypeName(con.getParameters().get(0).getTypeReference())))
				|| skipMember(binding, con, receiveOffset);
	}

	private boolean skipMember(final IMethodBinding binding, final Constructor con, final int receiveOffset) {
		boolean skipMember = false;
		for (int i = 0; i < binding.getParameterTypes().length; i++) {
			final ITypeBinding currentType = binding.getParameterTypes()[i];
			final tools.mdsd.jamopp.model.java.parameters.Parameter currentParam = con.getParameters()
					.get(i + receiveOffset);
			if (!toTypeNameConverter.convert(currentType)
					.equals(toTypeNameConverter.convertToTypeName(currentParam.getTypeReference()))
					|| currentType.getDimensions() != currentParam.getArrayDimension()) {
				skipMember = true;
				break;
			}
		}
		return skipMember;
	}

}
