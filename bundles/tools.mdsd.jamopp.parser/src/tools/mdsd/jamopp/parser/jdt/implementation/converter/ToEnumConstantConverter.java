package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToEnumConstantConverter implements Converter<EnumConstantDeclaration, EnumConstant> {

	private final JdtResolver iUtilJdtResolver;
	private final UtilLayout utilLayout;
	private final UtilNamedElement utilNamedElement;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> utilExpressionConverter;
	private final Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter;

	@Inject
	public ToEnumConstantConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout,
			JdtResolver iUtilJdtResolver,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> utilExpressionConverter,
			Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilExpressionConverter = utilExpressionConverter;
		this.toAnonymousClassConverter = toAnonymousClassConverter;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnumConstant convert(EnumConstantDeclaration enDecl) {
		EnumConstant result;
		IVariableBinding binding = enDecl.resolveVariable();
		if (binding == null) {
			result = iUtilJdtResolver.getEnumConstant(enDecl.getName().getIdentifier());
		} else {
			result = iUtilJdtResolver.getEnumConstant(binding);
		}
		enDecl.modifiers()
				.forEach(obj -> result.getAnnotations().add(toAnnotationInstanceConverter.convert((Annotation) obj)));
		utilNamedElement.setNameOfElement(enDecl.getName(), result);
		enDecl.arguments().forEach(obj -> result.getArguments().add(utilExpressionConverter.convert((Expression) obj)));
		if (enDecl.getAnonymousClassDeclaration() != null) {
			result.setAnonymousClass(toAnonymousClassConverter.convert(enDecl.getAnonymousClassDeclaration()));
		}
		utilLayout.convertToMinimalLayoutInformation(result, enDecl);
		return result;
	}
}
