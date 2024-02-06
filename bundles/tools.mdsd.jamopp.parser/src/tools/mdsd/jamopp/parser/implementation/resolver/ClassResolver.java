package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;

public class ClassResolver extends AbstractResolverWithCache<Class, ITypeBinding> {

	private final ClassifiersFactory classifiersFactory;
	private final Set<ITypeBinding> typeBindings;
	private final Converter<ITypeBinding> toTypeNameConverter;

	@Inject
	public ClassResolver(final Map<String, Class> bindings, final ClassifiersFactory classifiersFactory,
			final Set<ITypeBinding> typeBindings,
			@Named("ToTypeNameConverter") final Converter<ITypeBinding> toTypeNameConverter) {
		super(bindings);
		this.classifiersFactory = classifiersFactory;
		this.typeBindings = typeBindings;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	@Override
	public Class getByBinding(final ITypeBinding binding) {
		typeBindings.add(binding);
		return getByName(toTypeNameConverter.convert(binding));
	}

	@Override
	public Class getByName(final String name) {
		Class resultClass;
		if (containsKey(name)) {
			resultClass = get(name);
		} else {
			Class result;
			final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
					.getConcreteClassifier(name);
			if (potClass instanceof Class) {
				result = (Class) potClass;
			} else {
				result = classifiersFactory.createClass();
			}
			putBinding(name, result);
			resultClass = result;
		}
		return resultClass;
	}

}
