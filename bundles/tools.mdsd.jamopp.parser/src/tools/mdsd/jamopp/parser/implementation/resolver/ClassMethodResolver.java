package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodChecker;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;

public class ClassMethodResolver extends AbstractResolverWithCache<ClassMethod, IMethodBinding> {

	private final Set<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final MethodChecker methodCheckerImpl;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;
	private final Converter<IMethodBinding> toMethodNameConverter;

	@Inject
	public ClassMethodResolver(final Map<String, ClassMethod> bindings, final TypesFactory typesFactory,
			final StatementsFactory statementsFactory, final Set<IMethodBinding> methodBindings,
			final MembersFactory membersFactory, final Resolver<Classifier, ITypeBinding> classifierResolver,
			final MethodChecker methodCheckerImpl, final Converter<IMethodBinding> toMethodNameConverter) {
		super(bindings);
		this.methodBindings = methodBindings;
		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.classifierResolver = classifierResolver;
		this.methodCheckerImpl = methodCheckerImpl;
		this.toMethodNameConverter = toMethodNameConverter;
	}

	@Override
	public ClassMethod getByBinding(final IMethodBinding binding) {
		final IMethodBinding methodDeclaration = binding.getMethodDeclaration();
		methodBindings.add(methodDeclaration);
		final String methName = toMethodNameConverter.convert(methodDeclaration);
		ClassMethod classMethod;
		if (containsKey(methName)) {
			classMethod = get(methName);
		} else {
			final ConcreteClassifier classifier = (ConcreteClassifier) classifierResolver
					.getByBinding(methodDeclaration.getDeclaringClass());
			ClassMethod result = null;
			if (classifier != null) {
				result = handleClassifier(methodDeclaration, classifier);
			}
			if (result == null) {
				result = createNewClassMethod();
			}
			putBinding(methName, result);
			classMethod = result;
		}
		return classMethod;
	}

	private ClassMethod handleClassifier(final IMethodBinding binding, final ConcreteClassifier classifier) {
		ClassMethod result = null;
		for (final tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
			if (mem instanceof ClassMethod) {
				result = methodCheckerImpl.checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem, binding);
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
		if (containsKey(name)) {
			classMethod = get(name);
		} else {
			final ClassMethod result = createNewClassMethod();
			putBinding(name, result);
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
