package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;

class ToEnumConstantConverter {

	private final UtilJdtResolver utilJdtResolver;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilExpressionConverter utilExpressionConverter;
	private final ToAnonymousClassConverter toAnonymousClassConverter;
	private final UtilLayout utilLayout;

	@Inject
	ToEnumConstantConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout,
			UtilJdtResolver utilJdtResolver, UtilExpressionConverter utilExpressionConverter,
			ToAnonymousClassConverter toAnonymousClassConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilExpressionConverter = utilExpressionConverter;
		this.toAnonymousClassConverter = toAnonymousClassConverter;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	EnumConstant convertToEnumConstant(EnumConstantDeclaration enDecl) {
		EnumConstant result;
		IVariableBinding binding = enDecl.resolveVariable();
		if (binding == null) {
			result = utilJdtResolver.getEnumConstant(enDecl.getName().getIdentifier());
		} else {
			result = utilJdtResolver.getEnumConstant(binding);
		}
		enDecl.modifiers().forEach(obj -> result.getAnnotations()
				.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
		utilNamedElement.setNameOfElement(enDecl.getName(), result);
		enDecl.arguments().forEach(
				obj -> result.getArguments().add(utilExpressionConverter.convertToExpression((Expression) obj)));
		if (enDecl.getAnonymousClassDeclaration() != null) {
			result.setAnonymousClass(
					toAnonymousClassConverter.convertToAnonymousClass(enDecl.getAnonymousClassDeclaration()));
		}
		utilLayout.convertToMinimalLayoutInformation(result, enDecl);
		return result;
	}

}
