package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;

public class AnonymousClassResolver extends ResolverAbstract<AnonymousClass, ITypeBinding> {

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
		final String typeName = toTypeNameConverter.convertToTypeName(binding);
		return getByName(typeName);
	}

	@Override
	public AnonymousClass getByName(final String name) {
		AnonymousClass anonymousClass;
		if (getBindings().containsKey(name)) {
			anonymousClass = getBindings().get(name);
		} else {
			final AnonymousClass result = classifiersFactory.createAnonymousClass();
			getBindings().put(name, result);
			anonymousClass = result;
		}
		return anonymousClass;
	}

}
