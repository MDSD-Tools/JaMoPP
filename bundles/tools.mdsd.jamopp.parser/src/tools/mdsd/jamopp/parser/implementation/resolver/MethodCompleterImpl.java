package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.parser.interfaces.resolver.MethodCompleter;

public class MethodCompleterImpl implements MethodCompleter {

	private final boolean extractAdditionalInfosFromTypeBindings;
	private final AnonymousClassResolver anonymousClassResolver;
	private final ToMethodNameConverter toMethodNameConverter;
	private final ClassifierResolver classifierResolver;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ClassResolverExtensionImpl classResolverExtensionImpl;
	private final Set<IMethodBinding> methodBindings;

	@Inject
	public MethodCompleterImpl(final Set<IMethodBinding> methodBindings,
			@Named("extractAdditionalInfosFromTypeBindings") final boolean extractAdditionalInfosFromBindings,
			final AnonymousClassResolver anonymousClassResolver, final ToTypeNameConverter toTypeNameConverter,
			final ToMethodNameConverter toMethodNameConverter, final ClassifierResolver classifierResolver,
			final ClassResolverExtensionImpl classResolverExtensionImpl) {
		this.anonymousClassResolver = anonymousClassResolver;
		this.methodBindings = methodBindings;
		extractAdditionalInfosFromTypeBindings = extractAdditionalInfosFromBindings;
		this.toMethodNameConverter = toMethodNameConverter;
		this.classifierResolver = classifierResolver;
		this.toTypeNameConverter = toTypeNameConverter;
		this.classResolverExtensionImpl = classResolverExtensionImpl;
	}

	@Override
	public void completeMethod(final String methodName, final tools.mdsd.jamopp.model.java.members.Member method) {
		if (method.eContainer() == null) {
			final IMethodBinding methBind = methodBindings.stream()
					.filter(meth -> methodName.equals(toMethodNameConverter.convert(meth))).findFirst().orElse(null);
			if (methBind != null) {
				handleEmptyMehodBinding(method, methBind);
			} else {
				classResolverExtensionImpl.addToSyntheticClass(method);
			}
		}
	}

	private void handleEmptyMehodBinding(final tools.mdsd.jamopp.model.java.members.Member method,
			final IMethodBinding methBind) {
		final tools.mdsd.jamopp.model.java.classifiers.Classifier cla = classifierResolver
				.getByBinding(methBind.getDeclaringClass());
		if (cla == null) {
			final String typeName = toTypeNameConverter.convert(methBind.getDeclaringClass());
			if (anonymousClassResolver.containsKey(typeName)) {
				final tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
						.get(typeName);
				if (!anonClass.getMembers().contains(method)) {
					anonClass.getMembers().add(method);
				}
			} else {
				classResolverExtensionImpl.addToSyntheticClass(method);
			}
		} else if (!extractAdditionalInfosFromTypeBindings
				&& cla instanceof final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier
				&& !classifier.getMembers().contains(method)) {
			classifier.getMembers().add(method);
		}
	}

}
