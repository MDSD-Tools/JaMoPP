package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;

public class EnumConstantResolver extends AbstractResolverWithCache<EnumConstant, IVariableBinding> {

	private final MembersFactory membersFactory;
	private final Set<IVariableBinding> variableBindings;
	private final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver;
	private final ToStringConverter<IVariableBinding> toFieldNameConverter;

	@Inject
	public EnumConstantResolver(final Map<String, EnumConstant> bindings, final Set<IVariableBinding> variableBindings,
			final MembersFactory membersFactory, final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver,
			final ToStringConverter<IVariableBinding> toFieldNameConverter) {
		super(bindings);
		this.membersFactory = membersFactory;
		this.variableBindings = variableBindings;
		this.enumerationResolver = enumerationResolver;
		this.toFieldNameConverter = toFieldNameConverter;
	}

	@Override
	public EnumConstant getByBinding(final IVariableBinding binding) {
		final String enumCN = toFieldNameConverter.convert(binding);
		EnumConstant enumConstant;
		if (containsKey(enumCN)) {
			enumConstant = get(enumCN);
		} else {
			variableBindings.add(binding);
			final Enumeration potPar = enumerationResolver.getByBinding(binding.getDeclaringClass());
			EnumConstant result = null;
			if (potPar != null) {
				for (final EnumConstant con : potPar.getConstants()) {
					if (con.getName().equals(binding.getName())) {
						result = con;
						break;
					}
				}
			}
			if (result == null) {
				result = membersFactory.createEnumConstant();
			}
			putBinding(enumCN, result);
			enumConstant = result;
		}
		return enumConstant;
	}

	@Override
	public EnumConstant getByName(final String name) {
		EnumConstant enumConstant;
		if (containsKey(name)) {
			enumConstant = get(name);
		} else {
			final EnumConstant result = membersFactory.createEnumConstant();
			putBinding(name, result);
			enumConstant = result;
		}
		return enumConstant;
	}

}
