package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public class ClassMethodResolver extends ResolverAbstract<ClassMethod, IMethodBinding> {

	private final Set<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final MethodChecker methodChecker;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ClassMethodResolver(Map<String, ClassMethod> bindings, TypesFactory typesFactory,
			StatementsFactory statementsFactory, Set<IMethodBinding> methodBindings, MembersFactory membersFactory,
			ClassifierResolver classifierResolver, MethodChecker methodChecker,
			ToMethodNameConverter toMethodNameConverter) {
		super(bindings);
		this.methodBindings = methodBindings;
		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.methodChecker = methodChecker;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	@Override
	public ClassMethod getByBinding(IMethodBinding binding) {
		IMethodBinding methodDeclaration = binding.getMethodDeclaration();
		methodBindings.add(methodDeclaration);
		String methName = toMethodNameConverter.convertToMethodName(methodDeclaration);
		if (getBindings().containsKey(methName)) {
			return getBindings().get(methName);
		}
		ConcreteClassifier classifier = (ConcreteClassifier) classifierResolver
				.getClassifier(methodDeclaration.getDeclaringClass());
		ClassMethod result = null;
		if (classifier != null) {
			result = handleClassifier(methodDeclaration, classifier, result);
		}
		if (result == null) {
			result = createNewClassMethod();
		}
		getBindings().put(methName, result);
		return result;
	}

	private ClassMethod handleClassifier(IMethodBinding binding, ConcreteClassifier classifier, ClassMethod method) {
		ClassMethod result = null;
		for (tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
			if (mem instanceof ClassMethod) {
				result = methodChecker.checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem, binding);
				if (result != null) {
					break;
				}
			}
		}
		if (result != null) {
			return result;
		}
		return method;
	}

	@Override
	public ClassMethod getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		ClassMethod result = createNewClassMethod();
		getBindings().put(name, result);
		return result;
	}

	private ClassMethod createNewClassMethod() {
		ClassMethod result = membersFactory.createClassMethod();
		result.setTypeReference(typesFactory.createVoid());
		tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
		block.setName("");
		result.setStatement(block);
		return result;
	}

}
