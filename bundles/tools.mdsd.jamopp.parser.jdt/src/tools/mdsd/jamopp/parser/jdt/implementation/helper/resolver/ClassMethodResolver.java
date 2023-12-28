package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ClassMethodResolver extends ResolverAbstract<ClassMethod, IMethodBinding> {

	private final HashSet<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public ClassMethodResolver(HashMap<IBinding, String> nameCache, HashMap<String, ClassMethod> bindings,
			UtilJdtResolverImpl utilJdtResolverImpl, TypesFactory typesFactory, StatementsFactory statementsFactory,
			HashSet<IMethodBinding> methodBindings, MembersFactory membersFactory) {
		super(nameCache, bindings);
		this.methodBindings = methodBindings;
		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public ClassMethod getByBinding(IMethodBinding binding) {
		binding = binding.getMethodDeclaration();
		methodBindings.add(binding);
		String methName = convertToMethodName(binding);
		if (getBindings().containsKey(methName)) {
			return getBindings().get(methName);
		}
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) utilJdtResolverImpl
				.getClassifier(binding.getDeclaringClass());
		tools.mdsd.jamopp.model.java.members.ClassMethod result = null;
		if (classifier != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.ClassMethod) {
					result = checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem, binding);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (result == null) {
			result = createNewClassMethod();
		}
		getBindings().put(methName, result);
		return result;
	}

	@Override
	public ClassMethod getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.members.ClassMethod result = createNewClassMethod();
		getBindings().put(name, result);
		return result;
	}

	private tools.mdsd.jamopp.model.java.members.ClassMethod createNewClassMethod() {
		tools.mdsd.jamopp.model.java.members.ClassMethod result = membersFactory.createClassMethod();
		result.setTypeReference(typesFactory.createVoid());
		tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
		block.setName("");
		result.setStatement(block);
		return result;
	}

}
