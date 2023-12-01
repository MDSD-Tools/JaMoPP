package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;

class ToMethodReferenceExpressionConverter {

	private final LiteralsFactory literalsFactory;
	private final ReferencesFactory referencesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilReferenceConverter referenceConverterUtility;
	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;

	@Inject
	ToMethodReferenceExpressionConverter(ToExpressionConverter toExpressionConverter,
			UtilReferenceConverter referenceConverterUtility, UtilLayout layoutInformationConverter,
			ToTypeReferenceConverter toTypeReferenceConverter, ExpressionsFactory expressionsFactory,
			ReferencesFactory referencesFactory, LiteralsFactory literalsFactory) {
		this.literalsFactory = literalsFactory;
		this.referencesFactory = referencesFactory;
		this.expressionsFactory = expressionsFactory;
		this.referenceConverterUtility = referenceConverterUtility;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.MethodReferenceExpression convertToMethodReferenceExpression(
			MethodReference ref) {
		if (ref.getNodeType() == ASTNode.CREATION_REFERENCE) {
			CreationReference crRef = (CreationReference) ref;
			if (crRef.getType().isArrayType()) {
				org.emftext.language.java.expressions.ArrayConstructorReferenceExpression result = expressionsFactory
						.createArrayConstructorReferenceExpression();
				result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(crRef.getType()));
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(crRef.getType(), result);
				layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
				return result;
			}
			org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression result = expressionsFactory
					.createClassTypeConstructorReferenceExpression();
			result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(crRef.getType()));
			crRef.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
			return result;
		}
		org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression result = expressionsFactory
				.createPrimaryExpressionReferenceExpression();
		if (ref.getNodeType() == ASTNode.TYPE_METHOD_REFERENCE) {
			TypeMethodReference typeRef = (TypeMethodReference) ref;
			result.setChild(referenceConverterUtility.convertToReference(typeRef.getType()));
			typeRef.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setMethodReference(referenceConverterUtility.convertToReference(typeRef.getName()));
		} else if (ref.getNodeType() == ASTNode.SUPER_METHOD_REFERENCE) {
			SuperMethodReference superRef = (SuperMethodReference) ref;
			if (superRef.getQualifier() != null) {
				org.emftext.language.java.references.Reference child = referenceConverterUtility
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
			superRef.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setMethodReference(referenceConverterUtility.convertToReference(superRef.getName()));
		} else if (ref.getNodeType() == ASTNode.EXPRESSION_METHOD_REFERENCE) {
			ExpressionMethodReference exprRef = (ExpressionMethodReference) ref;
			result.setChild((org.emftext.language.java.expressions.MethodReferenceExpressionChild) toExpressionConverter
					.convertToExpression(exprRef.getExpression()));
			exprRef.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setMethodReference(referenceConverterUtility.convertToReference(exprRef.getName()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, ref);
		return result;
	}

}
