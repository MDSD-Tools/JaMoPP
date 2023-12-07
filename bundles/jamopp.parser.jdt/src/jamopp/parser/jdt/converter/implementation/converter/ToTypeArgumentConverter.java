package jamopp.parser.jdt.converter.implementation.converter;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilArrays;

public class ToTypeArgumentConverter implements Converter<ITypeBinding, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final IUtilArrays utilJdtBindingConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	ToTypeArgumentConverter(IUtilArrays utilJdtBindingConverter,
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