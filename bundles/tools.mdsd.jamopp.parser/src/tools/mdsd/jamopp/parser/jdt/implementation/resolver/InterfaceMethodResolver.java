package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public class InterfaceMethodResolver extends ResolverAbstract<InterfaceMethod, IMethodBinding> {

	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final Set<IMethodBinding> methodBindings;
	private final ClassifierResolver classifierResolver;
	private final MethodChecker methodChecker;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public InterfaceMethodResolver(final Map<String, InterfaceMethod> bindings, final TypesFactory typesFactory,
			final StatementsFactory statementsFactory, final Set<IMethodBinding> methodBindings,
			final MembersFactory membersFactory, final ClassifierResolver classifierResolver,
			final MethodChecker methodChecker, final ToMethodNameConverter toMethodNameConverter) {
		super(bindings);
		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.methodBindings = methodBindings;
		this.classifierResolver = classifierResolver;
		this.methodChecker = methodChecker;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	@Override
	public InterfaceMethod getByBinding(final IMethodBinding binding) {
		final IMethodBinding methoDeclaration = binding.getMethodDeclaration();
		methodBindings.add(methoDeclaration);
		final String methName = toMethodNameConverter.convertToMethodName(methoDeclaration);
		InterfaceMethod interfaceMethod;
		if (getBindings().containsKey(methName)) {
			interfaceMethod = getBindings().get(methName);
		} else {
			final ConcreteClassifier classifier = (ConcreteClassifier) classifierResolver
					.getClassifier(methoDeclaration.getDeclaringClass());
			InterfaceMethod result = null;
			if (classifier != null) {
				result = handleNullClassifier(methoDeclaration, classifier, result);
			}
			if (result == null) {
				result = createNewInterfaceMethod();
			}
			getBindings().put(methName, result);
			interfaceMethod = result;
		}
		return interfaceMethod;
	}

	private InterfaceMethod handleNullClassifier(final IMethodBinding methoDeclaration,
			final ConcreteClassifier classifier, final InterfaceMethod result) {
		InterfaceMethod interfaceMethod = result;
		for (final tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
			if (mem instanceof InterfaceMethod) {
				interfaceMethod = methodChecker.checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem,
						methoDeclaration);
				if (result != null) {
					break;
				}
			}
		}
		return interfaceMethod;
	}

	@Override
	public InterfaceMethod getByName(final String name) {
		InterfaceMethod interfaceMethod;
		if (getBindings().containsKey(name)) {
			interfaceMethod = getBindings().get(name);
		} else {
			final InterfaceMethod result = createNewInterfaceMethod();
			getBindings().put(name, result);
			interfaceMethod = result;
		}
		return interfaceMethod;
	}

	private InterfaceMethod createNewInterfaceMethod() {
		final InterfaceMethod result = membersFactory.createInterfaceMethod();
		result.setTypeReference(typesFactory.createVoid());
		result.setStatement(statementsFactory.createEmptyStatement());
		return result;
	}

}
