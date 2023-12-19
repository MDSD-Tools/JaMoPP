package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;

public class TypeParameterResolver extends ResolverAbstract<TypeParameter, ITypeBinding> {

	private final HashSet<ITypeBinding> typeBindings;
	private final GenericsFactory genericsFactory;

	public TypeParameterResolver(HashMap<IBinding, String> nameCache, HashMap<String, TypeParameter> bindings,
			HashSet<ITypeBinding> typeBindings, GenericsFactory genericsFactory) {
		super(nameCache, bindings);
		this.typeBindings = typeBindings;
		this.genericsFactory = genericsFactory;
	}

	@Override
	public TypeParameter getByBinding(ITypeBinding binding) {
		String paramName = convertToTypeParameterName(binding);
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
