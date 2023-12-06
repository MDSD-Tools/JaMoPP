package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.helper.UtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToEnumConstantConverter implements ToConverter<EnumConstantDeclaration, EnumConstant> {

	private final UtilJdtResolver utilJdtResolver;
	private final UtilLayout utilLayout;
	private final UtilNamedElement utilNamedElement;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter;
	private final ToConverter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter;

	@Inject
	ToEnumConstantConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter,
			ToConverter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.utilJdtResolver = utilJdtResolver;
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
			result = utilJdtResolver.getEnumConstant(enDecl.getName().getIdentifier());
		} else {
			result = utilJdtResolver.getEnumConstant(binding);
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
