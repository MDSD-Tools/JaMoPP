package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.List;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.generics.ExtendsTypeArgument;
import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.QualifiedTypeArgument;
import tools.mdsd.jamopp.model.java.generics.SuperTypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilArrays;

public class ToTypeArgumentConverter implements Converter<ITypeBinding, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final UtilArrays utilJdtBindingConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	public ToTypeArgumentConverter(final UtilArrays utilJdtBindingConverter,
			final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			final GenericsFactory genericsFactory) {
		this.genericsFactory = genericsFactory;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

	@Override
	public TypeArgument convert(final ITypeBinding binding) {
		TypeArgument typeArgument;
		if (binding.isWildcardType()) {
			if (binding.getBound() == null) {
				typeArgument = genericsFactory.createUnknownTypeArgument();
			} else if (binding.isUpperbound()) {
				final ExtendsTypeArgument result = genericsFactory.createExtendsTypeArgument();
				result.setExtendType(toTypeReferencesConverter.convert(binding.getBound()).get(0));
				utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding, result);
				typeArgument = result;
			} else {
				final SuperTypeArgument result = genericsFactory.createSuperTypeArgument();
				result.setSuperType(toTypeReferencesConverter.convert(binding.getBound()).get(0));
				utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding, result);
				typeArgument = result;
			}
		} else {
			final QualifiedTypeArgument result = genericsFactory.createQualifiedTypeArgument();
			result.setTypeReference(toTypeReferencesConverter.convert(binding).get(0));
			utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding, result);
			typeArgument = result;
		}
		return typeArgument;
	}

}
