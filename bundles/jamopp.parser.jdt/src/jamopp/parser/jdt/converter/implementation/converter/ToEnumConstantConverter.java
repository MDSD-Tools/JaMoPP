package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToEnumConstantConverter implements Converter<EnumConstantDeclaration, EnumConstant> {

	private final IUtilJdtResolver iUtilJdtResolver;
	private final IUtilLayout utilLayout;
	private final IUtilNamedElement utilNamedElement;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter;
	private final Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter;

	@Inject
	ToEnumConstantConverter(IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, IUtilJdtResolver iUtilJdtResolver,
			Converter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter,
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
