package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.List;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeMethodReference;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpressionReferenceExpression;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilReferenceWalker;

public class ToMethodReferenceExpressionConverter implements Converter<MethodReference, MethodReferenceExpression> {

	private final LiteralsFactory literalsFactory;
	private final ReferencesFactory referencesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression;
	private final Converter<Type, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromType;
	private final Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	public ToMethodReferenceExpressionConverter(
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			final UtilLayout layoutInformationConverter, final Converter<Type, TypeReference> toTypeReferenceConverter,
			final ExpressionsFactory expressionsFactory, final ReferencesFactory referencesFactory,
			final LiteralsFactory literalsFactory,
			final Converter<Type, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromType,
			final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression,
			final Converter<Type, TypeArgument> typeToTypeArgumentConverter,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			final UtilReferenceWalker utilReferenceWalker) {
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

	@Override
	public MethodReferenceExpression convert(final MethodReference ref) {
		MethodReferenceExpression expression;
		if (ref.getNodeType() == ASTNode.CREATION_REFERENCE) {
			expression = handleCreationReference(ref);
		} else {
			final PrimaryExpressionReferenceExpression result = expressionsFactory
					.createPrimaryExpressionReferenceExpression();
			if (ref.getNodeType() == ASTNode.TYPE_METHOD_REFERENCE) {
				handleTypeMethodReference(ref, result);
			} else if (ref.getNodeType() == ASTNode.SUPER_METHOD_REFERENCE) {
				handleSuperMethodReference(ref, result);
			} else if (ref.getNodeType() == ASTNode.EXPRESSION_METHOD_REFERENCE) {
				handleExpressionMethodReference(ref, result);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, ref);
			expression = result;
		}
		return expression;
	}

	private MethodReferenceExpression handleCreationReference(final MethodReference ref) {
		MethodReferenceExpression expression;
		final CreationReference crRef = (CreationReference) ref;
		if (crRef.getType().isArrayType()) {
			final tools.mdsd.jamopp.model.java.expressions.ArrayConstructorReferenceExpression result = expressionsFactory
					.createArrayConstructorReferenceExpression();
			result.setTypeReference(toTypeReferenceConverter.convert(crRef.getType()));
			utilToArrayDimensionsAndSetConverter.convert(crRef.getType(), result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
			expression = result;
		} else {
			final tools.mdsd.jamopp.model.java.expressions.ClassTypeConstructorReferenceExpression result = expressionsFactory
					.createClassTypeConstructorReferenceExpression();
			result.setTypeReference(toTypeReferenceConverter.convert(crRef.getType()));
			((List<?>) crRef.typeArguments())
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
			expression = result;
		}
		return expression;
	}

	private void handleTypeMethodReference(final MethodReference ref,
			final PrimaryExpressionReferenceExpression result) {
		final TypeMethodReference typeRef = (TypeMethodReference) ref;
		result.setChild(utilReferenceWalker.walkUp(toReferenceConverterFromType.convert(typeRef.getType())));
		((List<?>) typeRef.typeArguments())
				.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		result.setMethodReference(
				utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(typeRef.getName())));
	}

	private void handleExpressionMethodReference(final MethodReference ref,
			final PrimaryExpressionReferenceExpression result) {
		final ExpressionMethodReference exprRef = (ExpressionMethodReference) ref;
		result.setChild((tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild) toExpressionConverter
				.convert(exprRef.getExpression()));
		((List<?>) exprRef.typeArguments())
				.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		result.setMethodReference(
				utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(exprRef.getName())));
	}

	private void handleSuperMethodReference(final MethodReference ref,
			final PrimaryExpressionReferenceExpression result) {
		final SuperMethodReference superRef = (SuperMethodReference) ref;
		if (superRef.getQualifier() != null) {
			final tools.mdsd.jamopp.model.java.references.Reference child = utilReferenceWalker
					.walkUp(toReferenceConverterFromExpression.convert(superRef.getQualifier()));
			final tools.mdsd.jamopp.model.java.references.SelfReference lastPart = referencesFactory
					.createSelfReference();
			lastPart.setSelf(literalsFactory.createSuper());
			tools.mdsd.jamopp.model.java.references.Reference part = child;
			tools.mdsd.jamopp.model.java.references.Reference next = child.getNext();
			while (next != null) {
				part = next;
				next = part.getNext();
			}
			part.setNext(lastPart);
			result.setChild(child);
		} else {
			final tools.mdsd.jamopp.model.java.references.SelfReference child = referencesFactory.createSelfReference();
			child.setSelf(literalsFactory.createSuper());
			result.setChild(child);
		}
		((List<?>) superRef.typeArguments())
				.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		result.setMethodReference(
				utilReferenceWalker.walkUp(toReferenceConverterFromExpression.convert(superRef.getName())));
	}

}
