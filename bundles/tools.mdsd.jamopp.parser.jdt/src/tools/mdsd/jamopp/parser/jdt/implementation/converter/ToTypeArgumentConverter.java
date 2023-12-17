package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import tools.mdsd.jamopp.model.java.generics.ExtendsTypeArgument;
import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.QualifiedTypeArgument;
import tools.mdsd.jamopp.model.java.generics.SuperTypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;

public class ToTypeArgumentConverter implements Converter<ITypeBinding, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final UtilArrays utilJdtBindingConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	ToTypeArgumentConverter(UtilArrays utilJdtBindingConverter,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter, GenericsFactory genericsFactory) {
		this.genericsFactory = genericsFactory;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

	public TypeArgument convert(ITypeBinding binding) {
		if (!binding.isWildcardType()) {
			QualifiedTypeArgument result = genericsFactory.createQualifiedTypeArgument();
			result.setTypeReference(toTypeReferencesConverter.convert(binding).get(0));
			utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding, result);
			return result;
		}
		if (binding.getBound() == null) {
			return genericsFactory.createUnknownTypeArgument();
		}
		if (binding.isUpperbound()) {
			ExtendsTypeArgument result = genericsFactory.createExtendsTypeArgument();
			result.setExtendType((toTypeReferencesConverter.convert(binding.getBound()).get(0)));
			utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding, result);
			return result;
		}
		SuperTypeArgument result = genericsFactory.createSuperTypeArgument();
		result.setSuperType((toTypeReferencesConverter.convert(binding.getBound()).get(0)));
		utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding, result);
		return result;
	}

}
