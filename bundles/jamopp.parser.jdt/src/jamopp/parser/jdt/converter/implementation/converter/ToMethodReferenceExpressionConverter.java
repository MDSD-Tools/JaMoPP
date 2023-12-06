package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CreationReference;
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

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

public class ToMethodReferenceExpressionConverter implements ToConverter<MethodReference, MethodReferenceExpression> {

	private final LiteralsFactory literalsFactory;
	private final ReferencesFactory referencesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToReferenceConverterFromExpression toReferenceConverterFromExpression;
	private final ToReferenceConverterFromType toReferenceConverterFromType;
	private final ToConverter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToMethodReferenceExpressionConverter(
			ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			IUtilLayout layoutInformationConverter, ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ExpressionsFactory expressionsFactory, ReferencesFactory referencesFactory, LiteralsFactory literalsFactory,
			ToReferenceConverterFromType toReferenceConverterFromType,
			ToReferenceConverterFromExpression toReferenceConverterFromExpression,
			ToConverter<Type, TypeArgument> typeToTypeArgumentConverter,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.literalsFactory = literalsFactory;
		this.referencesFactory = referencesFactory;
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
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
			result.setChild(toReferenceConverterFromType.convert(typeRef.getType()));
			typeRef.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setMethodReference(toReferenceConverterFromExpression.convertToReference(typeRef.getName()));
		} else if (ref.getNodeType() == ASTNode.SUPER_METHOD_REFERENCE) {
			SuperMethodReference superRef = (SuperMethodReference) ref;
			if (superRef.getQualifier() != null) {
				org.emftext.language.java.references.Reference child = toReferenceConverterFromExpression
						.convertToReference(superRef.getQualifier());
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
			result.setMethodReference(toReferenceConverterFromExpression.convertToReference(superRef.getName()));
		} else if (ref.getNodeType() == ASTNode.EXPRESSION_METHOD_REFERENCE) {
			ExpressionMethodReference exprRef = (ExpressionMethodReference) ref;
			result.setChild((org.emftext.language.java.expressions.MethodReferenceExpressionChild) toExpressionConverter
					.convert(exprRef.getExpression()));
			exprRef.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setMethodReference(toReferenceConverterFromExpression.convertToReference(exprRef.getName()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, ref);
		return result;
	}

}
