package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import  com.google.inject.name.Named;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class TypeParameterResolver extends AbstractResolverWithCache<TypeParameter, ITypeBinding> {

	private final Set<ITypeBinding> typeBindings;
	private final GenericsFactory genericsFactory;
	private final ToStringConverter<ITypeBinding> toTypeParameterNameConverter;

	@Inject
	public TypeParameterResolver(final Map<String, TypeParameter> bindings, final Set<ITypeBinding> typeBindings,
			final GenericsFactory genericsFactory,
			@Named("ToTypeParameterNameConverter") final ToStringConverter<ITypeBinding> toTypeParameterNameConverter) {
		super(bindings);
		this.typeBindings = typeBindings;
		this.genericsFactory = genericsFactory;
		this.toTypeParameterNameConverter = toTypeParameterNameConverter;
	}

	@Override
	public TypeParameter getByBinding(final ITypeBinding binding) {
		final String paramName = toTypeParameterNameConverter.convert(binding);
		TypeParameter typeParameter;
		if (containsKey(paramName)) {
			typeParameter = get(paramName);
		} else {
			typeBindings.add(binding);
			final TypeParameter result = genericsFactory.createTypeParameter();
			putBinding(paramName, result);
			typeParameter = result;
		}
		return typeParameter;
	}

	@Override
	public TypeParameter getByName(final String name) {
		throw new UnsupportedOperationException("Not implemented");
	}

}
