package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MethodCompleter {

	private final AnonymousClassResolver anonymousClassResolver;
	private final HashSet<IMethodBinding> methodBindings;
	private final boolean extractAdditionalInfosFromTypeBindings;
	private final ToMethodNameConverter toMethodNameConverter;
	private final ClassifierResolver classifierResolver;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ClassResolverSynthetic classResolverSynthetic;

	@Inject
	public MethodCompleter(HashSet<IMethodBinding> methodBindings,
			@Named("extractAdditionalInfosFromTypeBindings") boolean extractAdditionalInfosFromBindings,
			AnonymousClassResolver anonymousClassResolver, ToTypeNameConverter toTypeNameConverter,
			ToMethodNameConverter toMethodNameConverter, ClassifierResolver classifierResolver,
			ClassResolverSynthetic classResolverSynthetic) {
		this.anonymousClassResolver = anonymousClassResolver;
		this.methodBindings = methodBindings;
		extractAdditionalInfosFromTypeBindings = extractAdditionalInfosFromBindings;
		this.toMethodNameConverter = toMethodNameConverter;
		this.classifierResolver = classifierResolver;
		this.toTypeNameConverter = toTypeNameConverter;
		this.classResolverSynthetic = classResolverSynthetic;
	}

	public void completeMethod(String methodName, tools.mdsd.jamopp.model.java.members.Member method) {
		if (method.eContainer() == null) {
			IMethodBinding methBind = methodBindings.stream()
					.filter(meth -> methodName.equals(toMethodNameConverter.convertToMethodName(meth))).findFirst()
					.orElse(null);
			if (methBind != null) {
				tools.mdsd.jamopp.model.java.classifiers.Classifier cla = classifierResolver
						.getClassifier(methBind.getDeclaringClass());
				if (cla == null) {
					String typeName = toTypeNameConverter.convertToTypeName(methBind.getDeclaringClass());
					if (anonymousClassResolver.getBindings().containsKey(typeName)) {
						tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
								.getBindings().get(typeName);
						if (!anonClass.getMembers().contains(method)) {
							anonClass.getMembers().add(method);
						}
					} else {
						classResolverSynthetic.addToSyntheticClass(method);
					}
				} else if (!extractAdditionalInfosFromTypeBindings
						&& cla instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier i
						&& !i.getMembers().contains(method)) {
					i.getMembers().add(method);
				}
			} else {
				classResolverSynthetic.addToSyntheticClass(method);
			}
		}
	}

}
