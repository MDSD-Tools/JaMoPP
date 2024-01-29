package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public class ClassMethodResolver extends AbstractResolver<ClassMethod, IMethodBinding> {

	private final Set<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final ClassifierResolver classifierResolver;
	private final MethodChecker methodChecker;
	private final ToMethodNameConverter toMethodNameConverter;

	@Inject
	public ClassMethodResolver(final Map<String, ClassMethod> bindings, final TypesFactory typesFactory,
			final StatementsFactory statementsFactory, final Set<IMethodBinding> methodBindings,
			final MembersFactory membersFactory, final ClassifierResolver classifierResolver,
			final MethodChecker methodChecker, final ToMethodNameConverter toMethodNameConverter) {
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
	public ClassMethod getByBinding(final IMethodBinding binding) {
		final IMethodBinding methodDeclaration = binding.getMethodDeclaration();
		methodBindings.add(methodDeclaration);
		final String methName = toMethodNameConverter.convertToMethodName(methodDeclaration);
		ClassMethod classMethod;
		if (getBindings().containsKey(methName)) {
			classMethod = getBindings().get(methName);
		} else {
			final ConcreteClassifier classifier = (ConcreteClassifier) classifierResolver
					.getClassifier(methodDeclaration.getDeclaringClass());
			ClassMethod result = null;
			if (classifier != null) {
				result = handleClassifier(methodDeclaration, classifier);
			}
			if (result == null) {
				result = createNewClassMethod();
			}
			getBindings().put(methName, result);
			classMethod = result;
		}
		return classMethod;
	}

	private ClassMethod handleClassifier(final IMethodBinding binding, final ConcreteClassifier classifier) {
		ClassMethod result = null;
		for (final tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
			if (mem instanceof ClassMethod) {
				result = methodChecker.checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem, binding);
				if (result != null) {
					break;
				}
			}
		}

		return result;
	}

	@Override
	public ClassMethod getByName(final String name) {
		ClassMethod classMethod;
		if (getBindings().containsKey(name)) {
			classMethod = getBindings().get(name);
		} else {
			final ClassMethod result = createNewClassMethod();
			getBindings().put(name, result);
			classMethod = result;
		}
		return classMethod;

	}

	private ClassMethod createNewClassMethod() {
		final ClassMethod result = membersFactory.createClassMethod();
		result.setTypeReference(typesFactory.createVoid());
		final tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
		block.setName("");
		result.setStatement(block);
		return result;
	}

}
