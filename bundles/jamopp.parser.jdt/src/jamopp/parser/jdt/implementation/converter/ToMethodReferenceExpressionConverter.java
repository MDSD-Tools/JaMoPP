package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;

public class ToMethodReferenceExpressionConverter implements Converter<MethodReference, MethodReferenceExpression> {

	private final LiteralsFactory literalsFactory;
	private final ReferencesFactory referencesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression;
	private final Converter<Type, org.emftext.language.java.references.Reference> toReferenceConverterFromType;
	private final Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToMethodReferenceExpressionConverter(
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, Converter<Type, TypeReference> toTypeReferenceConverter,
			ExpressionsFactory expressionsFactory, ReferencesFactory referencesFactory, LiteralsFactory literalsFactory,
			Converter<Type, org.emftext.language.java.references.Reference> toReferenceConverterFromType,
			Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression,
			Converter<Type, TypeArgument> typeToTypeArgumentConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter, UtilReferenceWalker utilReferenceWalker) {
		this.literalsFactory = literalsFactory;
		this.referencesFactory = referencesFactory;
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilReferenceWalker = utilReferenceWalker;
		this.toExpressionConverter = toExpressionConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
		this.toReferenceConverterFromType = toReferenceConverterFromType;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	@SuppressWarnings("unchecked")
	public MethodReferenceExpression convert(MethodReference ref) {
		if (ref.getNodeType() == ASTNode.CREATION_REFERENCE) {
			CreationReference crRef = (CreationReference) ref;
			if (crRef.getType().isArrayType()) {
				org.emftext.language.java.expressions.ArrayConstructorReferenceExpression result = expressionsFactory
						.createArrayConstructorReferenceExpression();
				result.setTypeReference(toTypeReferenceConverter.convert(crRef.getType()));
				utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(crRef.getType(), result);
				layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
				return result;
			}
			org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression result = expressionsFactory
					.createClassTypeConstructorReferenceExpression();
			result.setTypeReference(toTypeReferenceConverter.convert(crRef.getType()));
			crRef.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
			return result;
		}
		org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression result = expressionsFactory
				.createPrimaryExpressionReferenceExpression();
		if (ref.getNodeType() == ASTNode.TYPE_METHOD_REFERENCE) {
			TypeMethodReference typeRef = (TypeMethodReference) ref;
			result.setChild(utilReferenceWalker.walkUp(toReferenceConverterFromType.convert(typeRef.getType())));
			typeRef.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setMethodReference(utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(typeRef.getName())));
		} else if (ref.getNodeType() == ASTNode.SUPER_METHOD_REFERENCE) {
			SuperMethodReference superRef = (SuperMethodReference) ref;
			if (superRef.getQualifier() != null) {
				org.emftext.language.java.references.Reference child = utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(superRef.getQualifier()));
				org.emftext.language.java.references.SelfReference lastPart = referencesFactory.createSelfReference();
				lastPart.setSelf(literalsFactory.createSuper());
				org.emftext.language.java.references.Reference part = child;
				org.emftext.language.java.references.Reference next = child.getNext();
				while (next != null) {
					part = next;
					next = part.getNext();
				}
				part.setNext(lastPart);
				result.setChild(child);
			} else {
				org.emftext.language.java.references.SelfReference child = referencesFactory.createSelfReference();
				child.setSelf(literalsFactory.createSuper());
				result.setChild(child);
			}
			superRef.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setMethodReference(utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(superRef.getName())));
		} else if (ref.getNodeType() == ASTNode.EXPRESSION_METHOD_REFERENCE) {
			ExpressionMethodReference exprRef = (ExpressionMethodReference) ref;
			result.setChild((org.emftext.language.java.expressions.MethodReferenceExpressionChild) toExpressionConverter
					.convert(exprRef.getExpression()));
			exprRef.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setMethodReference(utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(exprRef.getName())));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, ref);
		return result;
	}

}
