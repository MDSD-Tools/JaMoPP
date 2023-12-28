package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;

public class MethodCompleter {

	private final AnonymousClassResolver anonymousClassResolver;
	private final HashSet<IMethodBinding> methodBindings;
	private final UtilJdtResolverImpl utilJdtResolverImpl;
	private final boolean extractAdditionalInfosFromTypeBindings;

	@Inject
	public MethodCompleter(UtilJdtResolverImpl utilJdtResolverImpl, HashSet<IMethodBinding> methodBindings,
			boolean extractAdditionalInfosFromBindings, AnonymousClassResolver anonymousClassResolver) {
		this.anonymousClassResolver = anonymousClassResolver;
		this.methodBindings = methodBindings;
		this.utilJdtResolverImpl = utilJdtResolverImpl;
		extractAdditionalInfosFromTypeBindings = extractAdditionalInfosFromBindings;
	}

	public void completeMethod(String methodName, tools.mdsd.jamopp.model.java.members.Member method) {
		if (method.eContainer() == null) {
			IMethodBinding methBind = methodBindings.stream()
					.filter(meth -> methodName.equals(utilJdtResolverImpl.convertToMethodName(meth))).findFirst()
					.orElse(null);
			if (methBind != null) {
				tools.mdsd.jamopp.model.java.classifiers.Classifier cla = utilJdtResolverImpl
						.getClassifier(methBind.getDeclaringClass());
				if (cla == null) {
					String typeName = utilJdtResolverImpl.convertToTypeName(methBind.getDeclaringClass());
					if (anonymousClassResolver.getBindings().containsKey(typeName)) {
						tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
								.getBindings().get(typeName);
						if (!anonClass.getMembers().contains(method)) {
							anonClass.getMembers().add(method);
						}
					} else {
						utilJdtResolverImpl.addToSyntheticClass(method);
					}
				} else if (!extractAdditionalInfosFromTypeBindings
						&& cla instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier i
						&& !i.getMembers().contains(method)) {
					i.getMembers().add(method);
				}
			} else {
				utilJdtResolverImpl.addToSyntheticClass(method);
			}
		}
	}

}
