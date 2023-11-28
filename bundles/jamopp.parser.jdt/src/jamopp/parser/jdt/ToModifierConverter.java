package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.ModifiersFactory;

class ToModifierConverter {
	
	private final UtilLayout layoutInformationConverter;
	
	ToModifierConverter(UtilLayout layoutInformationConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
	}

	org.emftext.language.java.modifiers.Modifier convertToModifier(Modifier mod) {
		org.emftext.language.java.modifiers.Modifier result = null;
		if (mod.isAbstract()) {
			result = ModifiersFactory.eINSTANCE.createAbstract();
		} else if (mod.isDefault()) {
			result = ModifiersFactory.eINSTANCE.createDefault();
		} else if (mod.isFinal()) {
			result = ModifiersFactory.eINSTANCE.createFinal();
		} else if (mod.isNative()) {
			result = ModifiersFactory.eINSTANCE.createNative();
		} else if (mod.isPrivate()) {
			result = ModifiersFactory.eINSTANCE.createPrivate();
		} else if (mod.isProtected()) {
			result = ModifiersFactory.eINSTANCE.createProtected();
		} else if (mod.isPublic()) {
			result = ModifiersFactory.eINSTANCE.createPublic();
		} else if (mod.isStatic()) {
			result = ModifiersFactory.eINSTANCE.createStatic();
		} else if (mod.isStrictfp()) {
			result = ModifiersFactory.eINSTANCE.createStrictfp();
		} else if (mod.isSynchronized()) {
			result = ModifiersFactory.eINSTANCE.createSynchronized();
		} else if (mod.isTransient()) {
			result = ModifiersFactory.eINSTANCE.createTransient();
		} else { // mod.isVolatile()
			result = ModifiersFactory.eINSTANCE.createVolatile();
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, mod);
		return result;
	}
	
}
