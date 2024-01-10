package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToTypeReferencesConverter implements Converter<ITypeBinding, List<TypeReference>> {

	private final TypesFactory typesFactory;
	private final JdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private Converter<ITypeBinding, TypeArgument> toTypeArgumentConverter;

	@Inject
	ToTypeReferencesConverter(TypesFactory typesFactory, UtilNamedElement utilNamedElement,
			JdtResolver iUtilJdtResolver) {
		this.typesFactory = typesFactory;
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.utilNamedElement = utilNamedElement;
	}

	@Override
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
