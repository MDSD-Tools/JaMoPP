package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Modifier;

import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToModifiersConverter
		implements Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> {

	private final Map<Predicate<Integer>, Supplier<tools.mdsd.jamopp.model.java.modifiers.Modifier>> mappings;

	@Inject
	public ToModifiersConverter(ModifiersFactory modifiersFactory) {
		mappings = new HashMap<>();
		mappings.put(Modifier::isAbstract, () -> modifiersFactory.createAbstract());
		mappings.put(Modifier::isDefault, () -> modifiersFactory.createDefault());
		mappings.put(Modifier::isFinal, () -> modifiersFactory.createFinal());
		mappings.put(Modifier::isNative, () -> modifiersFactory.createNative());
		mappings.put(Modifier::isPrivate, () -> modifiersFactory.createPrivate());
		mappings.put(Modifier::isProtected, () -> modifiersFactory.createProtected());
		mappings.put(Modifier::isPublic, () -> modifiersFactory.createPublic());
		mappings.put(Modifier::isStatic, () -> modifiersFactory.createStatic());
		mappings.put(Modifier::isStrictfp, () -> modifiersFactory.createStrictfp());
		mappings.put(Modifier::isSynchronized, () -> modifiersFactory.createSynchronized());
		mappings.put(Modifier::isTransient, () -> modifiersFactory.createTransient());
		mappings.put(Modifier::isVolatile, () -> modifiersFactory.createVolatile());
	}

	@Override
	public Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier> convert(Integer modifiers) {
		List<tools.mdsd.jamopp.model.java.modifiers.Modifier> result = new ArrayList<>();

		for (Entry<Predicate<Integer>, Supplier<tools.mdsd.jamopp.model.java.modifiers.Modifier>> entry : mappings
				.entrySet()) {
			if (entry.getKey().test(modifiers)) {
				result.add(entry.getValue().get());
			}
		}

		return result;
	}

}
