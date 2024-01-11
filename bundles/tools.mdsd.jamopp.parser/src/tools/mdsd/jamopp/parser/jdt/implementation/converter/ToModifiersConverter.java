package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Modifier;

import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToModifiersConverter
		implements Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> {

	private final ModifiersFactory modifiersFactory;

	@Inject
	public ToModifiersConverter(ModifiersFactory modifiersFactory) {
		this.modifiersFactory = modifiersFactory;
	}

	@Override
	public Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier> convert(Integer modifiers) {
		List<tools.mdsd.jamopp.model.java.modifiers.Modifier> result = new ArrayList<>();
		if (Modifier.isAbstract(modifiers)) {
			result.add(modifiersFactory.createAbstract());
		}
		if (Modifier.isDefault(modifiers)) {
			result.add(modifiersFactory.createDefault());
		}
		if (Modifier.isFinal(modifiers)) {
			result.add(modifiersFactory.createFinal());
		}
		if (Modifier.isNative(modifiers)) {
			result.add(modifiersFactory.createNative());
		}
		if (Modifier.isPrivate(modifiers)) {
			result.add(modifiersFactory.createPrivate());
		}
		if (Modifier.isProtected(modifiers)) {
			result.add(modifiersFactory.createProtected());
		}
		if (Modifier.isPublic(modifiers)) {
			result.add(modifiersFactory.createPublic());
		}
		if (Modifier.isStatic(modifiers)) {
			result.add(modifiersFactory.createStatic());
		}
		if (Modifier.isStrictfp(modifiers)) {
			result.add(modifiersFactory.createStrictfp());
		}
		if (Modifier.isSynchronized(modifiers)) {
			result.add(modifiersFactory.createSynchronized());
		}
		if (Modifier.isTransient(modifiers)) {
			result.add(modifiersFactory.createTransient());
		}
		if (Modifier.isVolatile(modifiers)) {
			result.add(modifiersFactory.createVolatile());
		}
		return result;
	}

}
