package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;

import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToModifierOrAnnotationInstanceConverter
		implements Converter<IExtendedModifier, AnnotationInstanceOrModifier> {

	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> toModifierConverter;

	@Inject
	ToModifierOrAnnotationInstanceConverter(
			Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> toModifierConverter,
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
