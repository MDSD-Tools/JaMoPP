package jamopp.parser.jdt.implementation.converter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;

public class ToTypeReferencesConverter implements Converter<ITypeBinding, List<TypeReference>> {

	private final TypesFactory typesFactory;
	private final IUtilJdtResolver iUtilJdtResolver;
	private final IUtilNamedElement utilNamedElement;
	private Converter<ITypeBinding, TypeArgument> toTypeArgumentConverter;

	@Inject
	ToTypeReferencesConverter(TypesFactory typesFactory, IUtilNamedElement utilNamedElement,
			IUtilJdtResolver iUtilJdtResolver) {
		this.typesFactory = typesFactory;
		this.iUtilJdtResolver = iUtilJdtResolver;
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
			Classifier classifier = iUtilJdtResolver.getClassifier(binding);
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
	public void setToTypeArgumentConverter(Converter<ITypeBinding, TypeArgument> toTypeArgumentConverter) {
		this.toTypeArgumentConverter = toTypeArgumentConverter;
	}

}
