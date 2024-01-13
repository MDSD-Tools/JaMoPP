package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;

public class TypeParameterResolver extends ResolverAbstract<TypeParameter, ITypeBinding> {

	private final Set<ITypeBinding> typeBindings;
	private final GenericsFactory genericsFactory;
	private final ToTypeParameterNameConverter toTypeParameterNameConverter;

	@Inject
	public TypeParameterResolver(Map<String, TypeParameter> bindings, Set<ITypeBinding> typeBindings,
			GenericsFactory genericsFactory, ToTypeParameterNameConverter toTypeParameterNameConverter) {
		super(bindings);
		this.typeBindings = typeBindings;
		this.genericsFactory = genericsFactory;
		this.toTypeParameterNameConverter = toTypeParameterNameConverter;
	}

	@Override
	public TypeParameter getByBinding(ITypeBinding binding) {
		String paramName = toTypeParameterNameConverter.convertToTypeParameterName(binding);
		TypeParameter typeParameter;
		if (getBindings().containsKey(paramName)) {
			typeParameter = getBindings().get(paramName);
		} else {
			typeBindings.add(binding);
			TypeParameter result = genericsFactory.createTypeParameter();
			getBindings().put(paramName, result);
			typeParameter = result;
		}
		return typeParameter;
	}

	@Override
	public TypeParameter getByName(String name) {
		throw new UnsupportedOperationException("Not implemented");
	}

}
