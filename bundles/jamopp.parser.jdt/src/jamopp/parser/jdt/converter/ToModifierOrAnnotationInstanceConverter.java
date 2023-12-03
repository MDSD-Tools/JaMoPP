package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;

public class ToModifierOrAnnotationInstanceConverter extends ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> {

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
