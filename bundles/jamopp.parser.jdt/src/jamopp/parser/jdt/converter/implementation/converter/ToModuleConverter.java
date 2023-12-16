package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.ModuleDirective;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.ModifiersFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.visitor.AbstractVisitor;

public class ToModuleConverter implements Converter<ModuleDeclaration, org.emftext.language.java.containers.Module> {

	private final ModifiersFactory modifiersFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtResolverUtility;
	private final AbstractVisitor visitor;
	private final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	private final Converter<ModuleDirective, org.emftext.language.java.modules.ModuleDirective> toDirectiveConverter;

	@Inject
	public ToModuleConverter(AbstractVisitor visitor, IUtilNamedElement utilNamedElement,
			Converter<ModuleDirective, org.emftext.language.java.modules.ModuleDirective> toDirectiveConverter,
			ModifiersFactory modifiersFactory, IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility,
			Converter<Annotation, AnnotationInstance> annotationInstanceConverter) {
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toDirectiveConverter = toDirectiveConverter;
		this.visitor = visitor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.containers.Module convert(ModuleDeclaration node) {
		org.emftext.language.java.containers.Module module = jdtResolverUtility.getModule(node.resolveBinding());
		if (node.isOpen()) {
			module.setOpen(modifiersFactory.createOpen());
		}
		layoutInformationConverter.convertJavaRootLayoutInformation(module, node, visitor.getSource());
		utilNamedElement.addNameToNameSpace(node.getName(), module);
		module.setName("");
		node.annotations()
				.forEach(obj -> module.getAnnotations().add(annotationInstanceConverter.convert((Annotation) obj)));
		node.moduleStatements()
				.forEach(obj -> module.getTarget().add(toDirectiveConverter.convert((ModuleDirective) obj)));
		return module;
	}

}
