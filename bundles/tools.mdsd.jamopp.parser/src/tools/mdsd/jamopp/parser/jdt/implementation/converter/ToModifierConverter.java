package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Modifier;

import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToModifierConverter implements Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> {

	private final ModifiersFactory modifiersFactory;
	private final UtilLayout layoutInformationConverter;
	private final Map<Predicate<Modifier>, Supplier<tools.mdsd.jamopp.model.java.modifiers.Modifier>> mappings;

	@Inject
	public ToModifierConverter(final UtilLayout layoutInformationConverter, final ModifiersFactory modifiersFactory) {
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
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
	}

	@Override
	public tools.mdsd.jamopp.model.java.modifiers.Modifier convert(final Modifier mod) {
		tools.mdsd.jamopp.model.java.modifiers.Modifier result = null;

		for (final Entry<Predicate<Modifier>, Supplier<tools.mdsd.jamopp.model.java.modifiers.Modifier>> entry : mappings
				.entrySet()) {
			if (entry.getKey().test(mod)) {
				result = entry.getValue().get();
				break;
			}
		}

		if (result == null) {
			result = modifiersFactory.createVolatile();
		}

		layoutInformationConverter.convertToMinimalLayoutInformation(result, mod);
		return result;
	}

}
