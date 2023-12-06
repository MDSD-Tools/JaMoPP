package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.expressions.AssignmentExpressionChild;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToAnnotationValueConverter implements ToConverter<Expression, AnnotationValue> {

	private final ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierComverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToAnnotationValueConverter(
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter,
			ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierComverter,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toArrayInitialisierComverter = toArrayInitialisierComverter;
		this.toExpressionConverter = utilExpressionConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@Override
	public AnnotationValue convert(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convert((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			return toArrayInitialisierComverter.convert((ArrayInitializer) expr);
		}
		return (AssignmentExpressionChild) toExpressionConverter.convert(expr);
	}

}
