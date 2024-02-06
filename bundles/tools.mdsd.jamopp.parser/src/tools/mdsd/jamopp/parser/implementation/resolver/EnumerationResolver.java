package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class EnumerationResolver extends AbstractResolverWithCache<Enumeration, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final Set<ITypeBinding> typeBindings;
	private final ToStringConverter<ITypeBinding> toTypeNameConverter;

	@Inject
	public EnumerationResolver(final Map<String, Enumeration> bindings, final Set<ITypeBinding> typeBindings,
			final ClassifiersFactory classifiersFactory,
			@Named("ToTypeNameConverterFromBinding") final ToStringConverter<ITypeBinding> toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Enumeration getByBinding(final ITypeBinding binding) {
		final String enumName = toTypeNameConverter.convert(binding);
		Enumeration enumeration;
		if (containsKey(enumName)) {
			enumeration = get(enumName);
		} else {
			typeBindings.add(binding);
			final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
					.getConcreteClassifier(enumName);
			Enumeration result;
			if (classifier instanceof Enumeration) {
				result = (Enumeration) classifier;
			} else {
				result = classifiersFactory.createEnumeration();
			}
			putBinding(enumName, result);
			enumeration = result;
		}
		return enumeration;
	}

	@Override
	public Enumeration getByName(final String name) {
		throw new UnsupportedOperationException("Not implemented");
	}

}
