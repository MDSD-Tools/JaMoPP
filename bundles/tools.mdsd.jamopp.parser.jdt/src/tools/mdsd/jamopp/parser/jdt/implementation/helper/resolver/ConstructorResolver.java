package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class ConstructorResolver extends ResolverAbstract<Constructor, IMethodBinding> {

	private final HashSet<IMethodBinding> methodBindings;
	private final StatementsFactory statementsFactory;
	private final MembersFactory membersFactory;
	private final UtilJdtResolverImpl utilJdtResolverImpl;

	@Inject
	public ConstructorResolver(HashMap<IBinding, String> nameCache, HashMap<String, Constructor> bindings,
			StatementsFactory statementsFactory, HashSet<IMethodBinding> methodBindings, MembersFactory membersFactory,
			UtilJdtResolverImpl utilJdtResolverImpl) {
		super(nameCache, bindings);
		this.methodBindings = methodBindings;
		this.statementsFactory = statementsFactory;
		this.membersFactory = membersFactory;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
	}

	@Override
	public Constructor getByBinding(IMethodBinding binding) {
		String methName = convertToMethodName(binding);
		if (getBindings().containsKey(methName)) {
			return getBindings().get(methName);
		}
		methodBindings.add(binding);
		tools.mdsd.jamopp.model.java.members.Constructor result = null;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) utilJdtResolverImpl
				.getClassifier(binding.getDeclaringClass());
		if (potClass != null) {
			outerLoop: for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Constructor con
						&& mem.getName().equals(binding.getName())) {
					int receiveOffset = 0;
					if (binding.getDeclaredReceiverType() != null) {
						receiveOffset = 1;
					}
					if (con.getParameters().size() == binding.getParameterTypes().length + receiveOffset) {
						if (receiveOffset == 1 && (!(con.getParameters()
								.get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
								|| !convertToTypeName(binding.getDeclaredReceiverType())
										.equals(convertToTypeName(con.getParameters().get(0).getTypeReference())))) {
							continue outerLoop;
						}
						for (int i = 0; i < binding.getParameterTypes().length; i++) {
							ITypeBinding currentType = binding.getParameterTypes()[i];
							tools.mdsd.jamopp.model.java.parameters.Parameter currentParam = con.getParameters()
									.get(i + receiveOffset);
							if (!convertToTypeName(currentType)
									.equals(convertToTypeName(currentParam.getTypeReference()))
									|| currentType.getDimensions() != currentParam.getArrayDimension()) {
								continue outerLoop;
							}
						}
						result = con;
						break;
					}
				}
			}
		}
		if (result == null) {
			result = membersFactory.createConstructor();
			tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
			block.setName("");
			result.setBlock(block);
		}
		getBindings().put(methName, result);
		return result;
	}

	@Override
	public Constructor getByName(String name) {
		if (getBindings().containsKey(name)) {
			return getBindings().get(name);
		}
		tools.mdsd.jamopp.model.java.members.Constructor result = membersFactory.createConstructor();
		tools.mdsd.jamopp.model.java.statements.Block block = statementsFactory.createBlock();
		block.setName("");
		result.setBlock(block);
		getBindings().put(name, result);
		return result;
	}

}
