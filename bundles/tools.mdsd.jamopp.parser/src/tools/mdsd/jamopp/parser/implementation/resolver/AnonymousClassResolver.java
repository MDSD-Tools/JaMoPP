package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;

import  com.google.inject.name.Named;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class AnonymousClassResolver extends AbstractResolverWithCache<AnonymousClass, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final ToStringConverter<ITypeBinding> toTypeNameConverter;

	@Inject
	public AnonymousClassResolver(final Map<String, AnonymousClass> bindings,
			final ClassifiersFactory classifiersFactory,
			@Named("ToTypeNameConverterFromBinding") final ToStringConverter<ITypeBinding> toTypeNameConverter) {
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
