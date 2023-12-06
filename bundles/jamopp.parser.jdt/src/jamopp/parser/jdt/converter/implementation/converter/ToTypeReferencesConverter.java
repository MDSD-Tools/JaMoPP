package jamopp.parser.jdt.converter.implementation.converter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToTypeReferencesConverter implements ToConverter<ITypeBinding, List<TypeReference>> {

	private final TypesFactory typesFactory;
	private final UtilJdtResolver utilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private ToConverter<ITypeBinding, TypeArgument> toTypeArgumentConverter;

	@Inject
	ToTypeReferencesConverter(TypesFactory typesFactory, UtilNamedElement utilNamedElement,
			UtilJdtResolver utilJdtResolver) {
		this.typesFactory = typesFactory;
		this.utilJdtResolver = utilJdtResolver;
		this.utilNamedElement = utilNamedElement;
	}

	public List<TypeReference> convert(ITypeBinding binding) {
		List<TypeReference> result = new ArrayList<>();
		if (binding.isPrimitive()) {
			handlePrimitive(binding, result);
		} else if (binding.isArray()) {
			return convert(binding.getElementType());
		} else if (binding.isIntersectionType()) {
			for (ITypeBinding b : binding.getTypeBounds()) {
				result.addAll(convert(b));
			}
		} else {
			Classifier classifier = utilJdtResolver.getClassifier(binding);
			utilNamedElement.convertToNameAndSet(binding, classifier);
			ClassifierReference ref = typesFactory.createClassifierReference();
			if (binding.isParameterizedType()) {
				for (ITypeBinding b : binding.getTypeArguments()) {
					ref.getTypeArguments().add(toTypeArgumentConverter.convert(b));
				}
			}
			ref.setTarget(classifier);
			result.add(ref);
		}
		return result;
	}

	private void handlePrimitive(ITypeBinding binding, List<TypeReference> result) {
		if ("int".equals(binding.getName())) {
			result.add(typesFactory.createInt());
		} else if ("byte".equals(binding.getName())) {
			result.add(typesFactory.createByte());
		} else if ("short".equals(binding.getName())) {
			result.add(typesFactory.createShort());
		} else if ("long".equals(binding.getName())) {
			result.add(typesFactory.createLong());
		} else if ("boolean".equals(binding.getName())) {
			result.add(typesFactory.createBoolean());
		} else if ("double".equals(binding.getName())) {
			result.add(typesFactory.createDouble());
		} else if ("float".equals(binding.getName())) {
			result.add(typesFactory.createFloat());
		} else if ("void".equals(binding.getName())) {
			result.add(typesFactory.createVoid());
		} else if ("char".equals(binding.getName())) {
			result.add(typesFactory.createChar());
		}
	}

	@Inject
	public void setToTypeArgumentConverter(ToConverter<ITypeBinding, TypeArgument> toTypeArgumentConverter) {
		this.toTypeArgumentConverter = toTypeArgumentConverter;
	}

}
