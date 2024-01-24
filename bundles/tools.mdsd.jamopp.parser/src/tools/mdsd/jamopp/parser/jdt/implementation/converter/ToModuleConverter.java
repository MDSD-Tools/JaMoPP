package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.ModuleDirective;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class ToModuleConverter implements Converter<ModuleDeclaration, tools.mdsd.jamopp.model.java.containers.Module> {

	private final ModifiersFactory modifiersFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;
	private final AbstractVisitor visitor;
	private final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	private final Converter<ModuleDirective, tools.mdsd.jamopp.model.java.modules.ModuleDirective> toDirectiveConverter;

	@Inject
	public ToModuleConverter(final AbstractVisitor visitor, final UtilNamedElement utilNamedElement,
			final Converter<ModuleDirective, tools.mdsd.jamopp.model.java.modules.ModuleDirective> toDirectiveConverter,
			final ModifiersFactory modifiersFactory, final UtilLayout layoutInformationConverter,
			final JdtResolver jdtResolverUtility,
			final Converter<Annotation, AnnotationInstance> annotationInstanceConverter) {
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
	public tools.mdsd.jamopp.model.java.containers.Module convert(final ModuleDeclaration node) {
		final tools.mdsd.jamopp.model.java.containers.Module module = jdtResolverUtility
				.getModule(node.resolveBinding());
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
