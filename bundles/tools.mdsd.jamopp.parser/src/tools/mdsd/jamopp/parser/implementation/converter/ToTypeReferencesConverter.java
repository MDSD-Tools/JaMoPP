package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToTypeReferencesConverter implements Converter<ITypeBinding, List<TypeReference>> {

	private final TypesFactory typesFactory;
	private final JdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final Provider<Converter<ITypeBinding, TypeArgument>> toTypeArgumentConverter;
	private final Map<String, Supplier<TypeReference>> mappings;

	@Inject
	public ToTypeReferencesConverter(final TypesFactory typesFactory, final UtilNamedElement utilNamedElement,
			final JdtResolver iUtilJdtResolver,
			final Provider<Converter<ITypeBinding, TypeArgument>> toTypeArgumentConverter) {
		this.typesFactory = typesFactory;
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.utilNamedElement = utilNamedElement;
		this.toTypeArgumentConverter = toTypeArgumentConverter;
		mappings = new HashMap<>();
		mappings.put("int", () -> typesFactory.createInt());
		mappings.put("byte", () -> typesFactory.createByte());
		mappings.put("short", () -> typesFactory.createShort());
		mappings.put("long", () -> typesFactory.createLong());
		mappings.put("boolean", () -> typesFactory.createBoolean());
		mappings.put("double", () -> typesFactory.createDouble());
		mappings.put("float", () -> typesFactory.createFloat());
		mappings.put("void", () -> typesFactory.createVoid());
		mappings.put("char", () -> typesFactory.createChar());
	}

	@Override
	public List<TypeReference> convert(final ITypeBinding binding) {
		List<TypeReference> list;
		if (binding.isPrimitive()) {
			list = new ArrayList<>();
			handlePrimitive(binding, list);
		} else if (binding.isArray()) {
			list = convert(binding.getElementType());
		} else if (binding.isIntersectionType()) {
			list = new ArrayList<>();
			for (final ITypeBinding b : binding.getTypeBounds()) {
				list.addAll(convert(b));
			}
		} else {
			list = new ArrayList<>();
			final Classifier classifier = iUtilJdtResolver.getClassifier(binding);
			utilNamedElement.convertToNameAndSet(binding, classifier);
			final ClassifierReference ref = typesFactory.createClassifierReference();
			if (binding.isParameterizedType()) {
				for (final ITypeBinding b : binding.getTypeArguments()) {
					ref.getTypeArguments().add(toTypeArgumentConverter.get().convert(b));
				}
			}
			ref.setTarget(classifier);
			list.add(ref);
		}

		return list;
	}

	private void handlePrimitive(final ITypeBinding binding, final List<TypeReference> result) {
		for (final Entry<String, Supplier<TypeReference>> entry : mappings.entrySet()) {
			if (entry.getKey().equals(binding.getName())) {
				result.add(entry.getValue().get());
				break;
			}
		}
	}

}
