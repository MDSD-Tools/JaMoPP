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
	public InterfaceMethodResolver(Map<String, InterfaceMethod> bindings, TypesFactory typesFactory,
			StatementsFactory statementsFactory, Set<IMethodBinding> methodBindings, MembersFactory membersFactory,
			ClassifierResolver classifierResolver, MethodChecker methodChecker,
			ToMethodNameConverter toMethodNameConverter) {
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
	public InterfaceMethod getByBinding(IMethodBinding binding) {
		IMethodBinding methoDeclaration = binding.getMethodDeclaration();
		methodBindings.add(methoDeclaration);
		String methName = toMethodNameConverter.convertToMethodName(methoDeclaration);
		if (getBindings().containsKey(methName)) {
			return getBindings().get(methName);
		}
		ConcreteClassifier classifier = (ConcreteClassifier) classifierResolver
				.getClassifier(methoDeclaration.getDeclaringClass());
		InterfaceMethod result = null;
		if (classifier != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
				if (mem instanceof InterfaceMethod) {
					result = methodChecker.checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem,
							methoDeclaration);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (result == null) {
			result = createNewInterfaceMethod();
		}
		getBindings().put(methName, result);
		return result;
	}

	@Override
	public InterfaceMethod getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		InterfaceMethod result = createNewInterfaceMethod();
		getBindings().put(name, result);
		return result;
	}

	private InterfaceMethod createNewInterfaceMethod() {
		InterfaceMethod result = membersFactory.createInterfaceMethod();
		result.setTypeReference(typesFactory.createVoid());
		result.setStatement(statementsFactory.createEmptyStatement());
		return result;
	}

}
