package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.ModuleDirective;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.ModifiersFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToModuleConverter implements Converter<ModuleDeclaration, org.emftext.language.java.containers.Module> {

	private final ModifiersFactory modifiersFactory;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver jdtResolverUtility;
	private final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	private final Converter<ModuleDirective, org.emftext.language.java.modules.ModuleDirective> toDirectiveConverter;

	@Inject
	public ToModuleConverter(UtilNamedElement utilNamedElement,
			Converter<ModuleDirective, org.emftext.language.java.modules.ModuleDirective> toDirectiveConverter,
			ModifiersFactory modifiersFactory, UtilJdtResolver jdtResolverUtility,
			Converter<Annotation, AnnotationInstance> annotationInstanceConverter) {
		this.modifiersFactory = modifiersFactory;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toDirectiveConverter = toDirectiveConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.containers.Module convert(ModuleDeclaration node) {
		org.emftext.language.java.containers.Module module = jdtResolverUtility.getModule(node.resolveBinding());
		if (node.isOpen()) {
			module.setOpen(modifiersFactory.createOpen());
		}
		utilNamedElement.addNameToNameSpace(node.getName(), module);
		module.setName("");
		node.annotations()
				.forEach(obj -> module.getAnnotations().add(annotationInstanceConverter.convert((Annotation) obj)));
		node.moduleStatements()
				.forEach(obj -> module.getTarget().add(toDirectiveConverter.convert((ModuleDirective) obj)));
		return module;
	}

}
