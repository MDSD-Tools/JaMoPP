package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MethodCompleter {

	private final boolean extractAdditionalInfosFromTypeBindings;
	private final AnonymousClassResolver anonymousClassResolver;
	private final ToMethodNameConverter toMethodNameConverter;
	private final ClassifierResolver classifierResolver;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ClassResolverHelper classResolverHelper;
	private final Set<IMethodBinding> methodBindings;

	@Inject
	public MethodCompleter(final Set<IMethodBinding> methodBindings,
			@Named("extractAdditionalInfosFromTypeBindings") final boolean extractAdditionalInfosFromBindings,
			final AnonymousClassResolver anonymousClassResolver, final ToTypeNameConverter toTypeNameConverter,
			final ToMethodNameConverter toMethodNameConverter, final ClassifierResolver classifierResolver,
			final ClassResolverHelper classResolverHelper) {
		this.anonymousClassResolver = anonymousClassResolver;
		this.methodBindings = methodBindings;
		extractAdditionalInfosFromTypeBindings = extractAdditionalInfosFromBindings;
		this.toMethodNameConverter = toMethodNameConverter;
		this.classifierResolver = classifierResolver;
		this.toTypeNameConverter = toTypeNameConverter;
		this.classResolverHelper = classResolverHelper;
	}

	public void completeMethod(final String methodName, final tools.mdsd.jamopp.model.java.members.Member method) {
		if (method.eContainer() == null) {
			final IMethodBinding methBind = methodBindings.stream()
					.filter(meth -> methodName.equals(toMethodNameConverter.convertToMethodName(meth))).findFirst()
					.orElse(null);
			if (methBind != null) {
				final tools.mdsd.jamopp.model.java.classifiers.Classifier cla = classifierResolver
						.getClassifier(methBind.getDeclaringClass());
				if (cla == null) {
					final String typeName = toTypeNameConverter.convertToTypeName(methBind.getDeclaringClass());
					if (anonymousClassResolver.getBindings().containsKey(typeName)) {
						final tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
								.getBindings().get(typeName);
						if (!anonClass.getMembers().contains(method)) {
							anonClass.getMembers().add(method);
						}
					} else {
						classResolverHelper.addToSyntheticClass(method);
					}
				} else if (!extractAdditionalInfosFromTypeBindings
						&& cla instanceof final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier
						&& !classifier.getMembers().contains(method)) {
					classifier.getMembers().add(method);
				}
			} else {
				classResolverHelper.addToSyntheticClass(method);
			}
		}
	}

}
