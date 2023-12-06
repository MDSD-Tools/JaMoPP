package jamopp.parser.jdt.converter.implementation;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilArrays;

public class ToTypeArgumentConverter implements ToConverter<ITypeBinding, TypeArgument> {

	private final GenericsFactory genericsFactory;
	private final UtilArrays utilJdtBindingConverter;
	private final ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	ToTypeArgumentConverter(UtilArrays utilJdtBindingConverter,
			ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter, GenericsFactory genericsFactory) {
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
