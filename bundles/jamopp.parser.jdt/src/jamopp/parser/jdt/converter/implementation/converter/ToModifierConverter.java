package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.ModifiersFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToModifierConverter implements Converter<Modifier, org.emftext.language.java.modifiers.Modifier> {

	private final ModifiersFactory modifiersFactory;
	private final IUtilLayout layoutInformationConverter;

	@Inject
	ToModifierConverter(IUtilLayout layoutInformationConverter, ModifiersFactory modifiersFactory) {
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	@Override
	public org.emftext.language.java.modifiers.Modifier convert(Modifier mod) {
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
