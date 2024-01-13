package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Modifier;

import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToModifierConverter implements Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> {

	private final ModifiersFactory modifiersFactory;
	private final UtilLayout layoutInformationConverter;

	@Inject
	public ToModifierConverter(UtilLayout layoutInformationConverter, ModifiersFactory modifiersFactory) {
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.modifiers.Modifier convert(Modifier mod) {
		tools.mdsd.jamopp.model.java.modifiers.Modifier result;
		if (mod.isAbstract()) {
			result = modifiersFactory.createAbstract();
		} else if (mod.isDefault()) {
			result = modifiersFactory.createDefault();
		} else if (mod.isFinal()) {
			result = modifiersFactory.createFinal();
		} else if (mod.isNative()) {
			result = modifiersFactory.createNative();
		} else if (mod.isPrivate()) {
			result = modifiersFactory.createPrivate();
		} else if (mod.isProtected()) {
			result = modifiersFactory.createProtected();
		} else if (mod.isPublic()) {
			result = modifiersFactory.createPublic();
		} else if (mod.isStatic()) {
			result = modifiersFactory.createStatic();
		} else if (mod.isStrictfp()) {
			result = modifiersFactory.createStrictfp();
		} else if (mod.isSynchronized()) {
			result = modifiersFactory.createSynchronized();
		} else if (mod.isTransient()) {
			result = modifiersFactory.createTransient();
		} else { // mod.isVolatile()
			result = modifiersFactory.createVolatile();
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, mod);
		return result;
	}

}
