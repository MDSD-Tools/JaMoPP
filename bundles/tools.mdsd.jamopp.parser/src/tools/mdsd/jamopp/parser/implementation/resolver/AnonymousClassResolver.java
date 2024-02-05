package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnonymousClassResolver extends AbstractResolverWithCache<AnonymousClass, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public AnonymousClassResolver(final Map<String, AnonymousClass> bindings,
			final ClassifiersFactory classifiersFactory, final ToTypeNameConverter toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public AnonymousClass getByBinding(final ITypeBinding binding) {
		final String typeName = toTypeNameConverter.convert(binding);
		return getByName(typeName);
	}

	@Override
	public AnonymousClass getByName(final String name) {
		AnonymousClass anonymousClass;
		if (containsKey(name)) {
			anonymousClass = get(name);
		} else {
			final AnonymousClass result = classifiersFactory.createAnonymousClass();
			putBinding(name, result);
			anonymousClass = result;
		}
		return anonymousClass;
	}

}
