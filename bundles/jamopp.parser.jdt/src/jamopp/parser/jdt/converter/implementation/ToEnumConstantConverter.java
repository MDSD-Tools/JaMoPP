package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToEnumConstantConverter implements ToConverter<EnumConstantDeclaration, EnumConstant> {

	private final UtilJdtResolver utilJdtResolver;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final UtilNamedElement utilNamedElement;
	private final ToExpressionConverter utilExpressionConverter;
	private final ToAnonymousClassConverter toAnonymousClassConverter;
	private final UtilLayout utilLayout;

	@Inject
	ToEnumConstantConverter(UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToExpressionConverter utilExpressionConverter, ToAnonymousClassConverter toAnonymousClassConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
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
		enDecl.modifiers().forEach(obj -> result.getAnnotations()
				.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
		utilNamedElement.setNameOfElement(enDecl.getName(), result);
		enDecl.arguments().forEach(obj -> result.getArguments().add(utilExpressionConverter.convert((Expression) obj)));
		if (enDecl.getAnonymousClassDeclaration() != null) {
			result.setAnonymousClass(
					toAnonymousClassConverter.convertToAnonymousClass(enDecl.getAnonymousClassDeclaration()));
		}
		utilLayout.convertToMinimalLayoutInformation(result, enDecl);
		return result;
	}
}
