package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;

public class TypeParameterResolver extends ResolverAbstract<TypeParameter, ITypeBinding> {

	private final HashSet<ITypeBinding> typeBindings;
	private final GenericsFactory genericsFactory;
	private final ToTypeParameterNameConverter toTypeParameterNameConverter;

	@Inject
	public TypeParameterResolver(HashMap<String, TypeParameter> bindings, HashSet<ITypeBinding> typeBindings,
			GenericsFactory genericsFactory, ToTypeParameterNameConverter toTypeParameterNameConverter) {
		super(bindings);
		this.typeBindings = typeBindings;
		this.genericsFactory = genericsFactory;
		this.toTypeParameterNameConverter = toTypeParameterNameConverter;
	}

	@Override
	public TypeParameter getByBinding(ITypeBinding binding) {
		String paramName = toTypeParameterNameConverter.convertToTypeParameterName(binding);
		if (getBindings().containsKey(paramName)) {
			return getBindings().get(paramName);
		}
		typeBindings.add(binding);
		tools.mdsd.jamopp.model.java.generics.TypeParameter result = genericsFactory.createTypeParameter();
		getBindings().put(paramName, result);
		return result;
	}

	@Override
	public TypeParameter getByName(String name) {
		throw new RuntimeException("Not implemented");
	}

}
