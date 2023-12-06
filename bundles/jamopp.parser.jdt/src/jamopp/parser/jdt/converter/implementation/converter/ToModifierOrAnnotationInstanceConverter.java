package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToModifierOrAnnotationInstanceConverter
		implements ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> {

	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final ToConverter<Modifier, org.emftext.language.java.modifiers.Modifier> toModifierConverter;

	@Inject
	ToModifierOrAnnotationInstanceConverter(
			ToConverter<Modifier, org.emftext.language.java.modifiers.Modifier> toModifierConverter,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toModifierConverter = toModifierConverter;
	}

	@Override
	public AnnotationInstanceOrModifier convert(IExtendedModifier mod) {
		if (mod.isModifier()) {
			return toModifierConverter.convert((Modifier) mod);
		}
		return toAnnotationInstanceConverter.convert((Annotation) mod);
	}

}
