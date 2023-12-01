package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.ModifiersFactory;

import com.google.inject.Inject;

class ToModifierConverter {

	private final ModifiersFactory modifiersFactory;
	private final UtilLayout layoutInformationConverter;

	@Inject
	ToModifierConverter(UtilLayout layoutInformationConverter, ModifiersFactory modifiersFactory) {
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	org.emftext.language.java.modifiers.Modifier convertToModifier(Modifier mod) {
		org.emftext.language.java.modifiers.Modifier result = null;
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
