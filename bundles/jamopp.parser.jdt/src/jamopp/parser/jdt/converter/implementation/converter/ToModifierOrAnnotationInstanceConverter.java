package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;

public class ToModifierOrAnnotationInstanceConverter
		implements Converter<IExtendedModifier, AnnotationInstanceOrModifier> {

	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Modifier, org.emftext.language.java.modifiers.Modifier> toModifierConverter;

	@Inject
	ToModifierOrAnnotationInstanceConverter(
			Converter<Modifier, org.emftext.language.java.modifiers.Modifier> toModifierConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
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
