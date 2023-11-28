package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

class ToModifierOrAnnotationInstanceConverter {

	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ToModifierConverter toModifierConverter;

	ToModifierOrAnnotationInstanceConverter(ToModifierConverter toModifierConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toModifierConverter = toModifierConverter;
	}

	AnnotationInstanceOrModifier converToModifierOrAnnotationInstance(IExtendedModifier mod) {
		if (mod.isModifier()) {
			return toModifierConverter.convertToModifier((Modifier) mod);
		}
		return toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) mod);
	}

}
