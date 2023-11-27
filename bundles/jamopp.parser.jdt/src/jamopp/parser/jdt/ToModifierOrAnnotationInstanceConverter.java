package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

public class ToModifierOrAnnotationInstanceConverter {

	private final UtilBaseConverter utilBaseConverter;
	
	public ToModifierOrAnnotationInstanceConverter(UtilBaseConverter utilBaseConverter) {
		this.utilBaseConverter = utilBaseConverter;
	}
	
	AnnotationInstanceOrModifier converToModifierOrAnnotationInstance(IExtendedModifier mod) {
		if (mod.isModifier()) {
			return utilBaseConverter.convertToModifier((Modifier) mod);
		}
		return utilBaseConverter.convertToAnnotationInstance((Annotation) mod);
	}
	
}
