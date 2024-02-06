package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.parser.interfaces.resolver.ClassResolverExtension;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class MethodCompleterImpl implements MethodCompleter {

	private final boolean extractAdditionalInfosFromTypeBindings;
	private final Set<IMethodBinding> methodBindings;

	private final ResolverWithCache<AnonymousClass, ITypeBinding> anonymousClassResolver;
	private final ToStringConverter<IMethodBinding> toMethodNameConverter;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;
	private final ToStringConverter<ITypeBinding> toTypeNameConverter;
	private final ClassResolverExtension classResolverExtensionImpl;

	@Inject
	public MethodCompleterImpl(final Set<IMethodBinding> methodBindings,
			@Named("extractAdditionalInfosFromTypeBindings") final boolean extractAdditionalInfosFromBindings,
			final ResolverWithCache<AnonymousClass, ITypeBinding> anonymousClassResolver,
			@Named("ToTypeNameConverterFromBinding") final ToStringConverter<ITypeBinding> toTypeNameConverter,
			final ToStringConverter<IMethodBinding> toMethodNameConverter,
			final Resolver<Classifier, ITypeBinding> classifierResolver,
			final ClassResolverExtension classResolverExtensionImpl) {
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
		final Classifier cla = classifierResolver.getByBinding(methBind.getDeclaringClass());
		if (cla == null) {
			final String typeName = toTypeNameConverter.convert(methBind.getDeclaringClass());
			if (anonymousClassResolver.containsKey(typeName)) {
				final AnonymousClass anonClass = anonymousClassResolver.get(typeName);
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
