package jamopp.parser.jdt.converter.other;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToModifierOrAnnotationInstanceConverter implements ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> {

	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ToModifierConverter toModifierConverter;

	@Inject
	ToModifierOrAnnotationInstanceConverter(ToModifierConverter toModifierConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toModifierConverter = toModifierConverter;
	}

	@Override
	public
	AnnotationInstanceOrModifier convert(IExtendedModifier mod) {
		if (mod.isModifier()) {
			return toModifierConverter.convert((Modifier) mod);
		}
		return toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) mod);
	}

}
