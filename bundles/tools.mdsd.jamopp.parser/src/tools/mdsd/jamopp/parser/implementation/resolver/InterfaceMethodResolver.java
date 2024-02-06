package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodChecker;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;

public class InterfaceMethodResolver extends AbstractResolverWithCache<InterfaceMethod, IMethodBinding> {

	private final Set<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final MethodChecker methodCheckerImpl;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;
	private final Converter<IMethodBinding> toMethodNameConverter;

	@Inject
	public InterfaceMethodResolver(final Map<String, InterfaceMethod> bindings, final TypesFactory typesFactory,
			final StatementsFactory statementsFactory, final Set<IMethodBinding> methodBindings,
			final MembersFactory membersFactory, final Resolver<Classifier, ITypeBinding> classifierResolver,
			final MethodChecker methodCheckerImpl, final Converter<IMethodBinding> toMethodNameConverter) {
		super(bindings);
		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.methodBindings = methodBindings;
		this.classifierResolver = classifierResolver;
		this.methodCheckerImpl = methodCheckerImpl;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	@Override
	public InterfaceMethod getByBinding(final IMethodBinding binding) {
		final IMethodBinding methoDeclaration = binding.getMethodDeclaration();
		methodBindings.add(methoDeclaration);
		final String methName = toMethodNameConverter.convert(methoDeclaration);
		InterfaceMethod interfaceMethod;
		if (containsKey(methName)) {
			interfaceMethod = get(methName);
		} else {
			final ConcreteClassifier classifier = (ConcreteClassifier) classifierResolver
					.getByBinding(methoDeclaration.getDeclaringClass());
			InterfaceMethod result = null;
			if (classifier != null) {
				result = handleNullClassifier(methoDeclaration, classifier);
			}
			if (result == null) {
				result = createNewInterfaceMethod();
			}
			putBinding(methName, result);
			interfaceMethod = result;
		}
		return interfaceMethod;
	}

	private InterfaceMethod handleNullClassifier(final IMethodBinding methoDeclaration,
			final ConcreteClassifier classifier) {
		InterfaceMethod interfaceMethod = null;
		for (final tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
			if (mem instanceof InterfaceMethod) {
				interfaceMethod = methodCheckerImpl.checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem,
						methoDeclaration);
				if (interfaceMethod != null) {
					break;
				}
			}
		}
		return interfaceMethod;
	}

	@Override
	public InterfaceMethod getByName(final String name) {
		InterfaceMethod interfaceMethod;
		if (containsKey(name)) {
			interfaceMethod = get(name);
		} else {
			final InterfaceMethod result = createNewInterfaceMethod();
			putBinding(name, result);
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
