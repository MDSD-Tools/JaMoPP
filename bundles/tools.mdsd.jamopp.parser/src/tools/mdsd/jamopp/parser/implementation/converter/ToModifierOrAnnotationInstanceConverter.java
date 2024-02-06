package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToModifierOrAnnotationInstanceConverter
		implements Converter<IExtendedModifier, AnnotationInstanceOrModifier> {

	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> toModifierConverter;

	@Inject
	public ToModifierOrAnnotationInstanceConverter(
			final Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> toModifierConverter,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toModifierConverter = toModifierConverter;
	}

	@Override
	public AnnotationInstanceOrModifier convert(final IExtendedModifier mod) {
		AnnotationInstanceOrModifier result;
		if (mod.isModifier()) {
			result = toModifierConverter.convert((Modifier) mod);
		} else {
			result = toAnnotationInstanceConverter.convert((Annotation) mod);
		}
		return result;
	}

}
