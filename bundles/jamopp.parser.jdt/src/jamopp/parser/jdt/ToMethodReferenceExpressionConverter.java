package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeMethodReference;

class ToMethodReferenceExpressionConverter {

	private final ReferenceConverterUtility referenceConverterUtility;
	private final LayoutInformationConverter layoutInformationConverter;
	private final BaseConverterUtility baseConverterUtility;
	private final ToExpressionConverter toExpressionConverter;

	ToMethodReferenceExpressionConverter(ToExpressionConverter toExpressionConverter,
			ReferenceConverterUtility referenceConverterUtility, LayoutInformationConverter layoutInformationConverter,
			BaseConverterUtility baseConverterUtility) {
		this.referenceConverterUtility = referenceConverterUtility;
		this.layoutInformationConverter = layoutInformationConverter;
		this.baseConverterUtility = baseConverterUtility;
		this.toExpressionConverter = toExpressionConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.MethodReferenceExpression convertToMethodReferenceExpression(
			MethodReference ref) {
		if (ref.getNodeType() == ASTNode.CREATION_REFERENCE) {
			CreationReference crRef = (CreationReference) ref;
			if (crRef.getType().isArrayType()) {
				org.emftext.language.java.expressions.ArrayConstructorReferenceExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
						.createArrayConstructorReferenceExpression();
				result.setTypeReference(baseConverterUtility.convertToTypeReference(crRef.getType()));
				baseConverterUtility.convertToArrayDimensionsAndSet(crRef.getType(), result);
				layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
				return result;
			}
			org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
					.createClassTypeConstructorReferenceExpression();
			result.setTypeReference(baseConverterUtility.convertToTypeReference(crRef.getType()));
			crRef.typeArguments().forEach(
					obj -> result.getCallTypeArguments().add(baseConverterUtility.convertToTypeArgument((Type) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, crRef);
			return result;
		}
		org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createPrimaryExpressionReferenceExpression();
		if (ref.getNodeType() == ASTNode.TYPE_METHOD_REFERENCE) {
			TypeMethodReference typeRef = (TypeMethodReference) ref;
			result.setChild(referenceConverterUtility.convertToReference(typeRef.getType()));
			typeRef.typeArguments().forEach(
					obj -> result.getCallTypeArguments().add(baseConverterUtility.convertToTypeArgument((Type) obj)));
			result.setMethodReference(referenceConverterUtility.convertToReference(typeRef.getName()));
		} else if (ref.getNodeType() == ASTNode.SUPER_METHOD_REFERENCE) {
			SuperMethodReference superRef = (SuperMethodReference) ref;
			if (superRef.getQualifier() != null) {
				org.emftext.language.java.references.Reference child = referenceConverterUtility
						.convertToReference(superRef.getQualifier());
				org.emftext.language.java.references.SelfReference lastPart = org.emftext.language.java.references.ReferencesFactory.eINSTANCE
						.createSelfReference();
				lastPart.setSelf(org.emftext.language.java.literals.LiteralsFactory.eINSTANCE.createSuper());
				org.emftext.language.java.references.Reference part = child;
				org.emftext.language.java.references.Reference next = child.getNext();
				while (next != null) {
					part = next;
					next = part.getNext();
				}
				part.setNext(lastPart);
				result.setChild(child);
			} else {
				org.emftext.language.java.references.SelfReference child = org.emftext.language.java.references.ReferencesFactory.eINSTANCE
						.createSelfReference();
				child.setSelf(org.emftext.language.java.literals.LiteralsFactory.eINSTANCE.createSuper());
				result.setChild(child);
			}
			superRef.typeArguments().forEach(
					obj -> result.getCallTypeArguments().add(baseConverterUtility.convertToTypeArgument((Type) obj)));
			result.setMethodReference(referenceConverterUtility.convertToReference(superRef.getName()));
		} else if (ref.getNodeType() == ASTNode.EXPRESSION_METHOD_REFERENCE) {
			ExpressionMethodReference exprRef = (ExpressionMethodReference) ref;
			result.setChild((org.emftext.language.java.expressions.MethodReferenceExpressionChild) toExpressionConverter
					.convertToExpression(exprRef.getExpression()));
			exprRef.typeArguments().forEach(
					obj -> result.getCallTypeArguments().add(baseConverterUtility.convertToTypeArgument((Type) obj)));
			result.setMethodReference(referenceConverterUtility.convertToReference(exprRef.getName()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, ref);
		return result;
	}

}
