package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

class ToExpressionConverter {

	private final UtilLayout layoutInformationConverter;
	private final UtilStatementConverter statementConverterUtility;
	private final UtilJDTResolver jdtResolverUtility;
	private final UtilJDTBindingConverter jdtBindingConverterUtility;
	private final UtilClassifierConverter classifierConverterUtility;
	private final ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter;
	private final ExpressionsFactory expressionsFactory;

	private ToAssignmentConverter toAssignmentOperatorConverter;
	private ToConditionalExpressionConverter toConditionalExpressionConverter;
	private ToEqualityExpressionConverter toEqualityExpressionConverter;
	private ToRelationExpressionConverter toRelationExpressionConverter;
	private ToShiftExpressionConverter toShiftExpressionConverter;
	private ToAdditiveExpressionConverter toAdditiveExpressionConverter;
	private ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter;
	private ToUnaryExpressionConverter toUnaryExpressionConverter;
	private ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter;
	private ToPrimaryExpressionConverter toPrimaryExpressionConverter;

	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;

	ToExpressionConverter(ExpressionsFactory expressionsFactory, UtilStatementConverter statementConverterUtility,
			UtilLayout layoutInformationConverter, UtilJDTResolver jdtResolverUtility,
			UtilJDTBindingConverter jdtBindingConverterUtility, UtilClassifierConverter classifierConverterUtility,
			ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter, ToTypeReferenceConverter toTypeReferenceConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.statementConverterUtility = statementConverterUtility;
		this.jdtResolverUtility = jdtResolverUtility;
		this.jdtBindingConverterUtility = jdtBindingConverterUtility;
		this.classifierConverterUtility = classifierConverterUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.expressionsFactory = expressionsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.Expression convertToExpression(Expression expr) {
		if (expr.getNodeType() == ASTNode.ASSIGNMENT) {
			return handleAssignment(expr);
		} else if (expr.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION) {
			return handleConditionalExpression(expr);
		} else if (expr.getNodeType() == ASTNode.INFIX_EXPRESSION) {
			return handleInfixExpression(expr);
		} else if (expr.getNodeType() == ASTNode.INSTANCEOF_EXPRESSION) {
			return handleInstanceOf(expr);
		} else if (expr.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
			return handlePrefixExpression(expr);
		} else if (expr.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
			return handlePostfixExpression(expr);
		} else if (expr.getNodeType() == ASTNode.CAST_EXPRESSION) {
			return handleCastExpression(expr);
		} else if (expr.getNodeType() == ASTNode.SWITCH_EXPRESSION) {
			return handleSwitchExpression(expr);
		} else if (expr instanceof MethodReference) {
			return handleMethodReference(expr);
		} else if (expr.getNodeType() == ASTNode.LAMBDA_EXPRESSION) {
			return handleLambdaExpression(expr);
		} else {
			return toPrimaryExpressionConverter.convertToPrimaryExpression(expr);
		}
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleLambdaExpression(Expression expr) {
		LambdaExpression lambda = (LambdaExpression) expr;
		org.emftext.language.java.expressions.LambdaExpression result = expressionsFactory.createLambdaExpression();
		if (!lambda.parameters().isEmpty() && lambda.parameters().get(0) instanceof VariableDeclarationFragment) {
			org.emftext.language.java.expressions.ImplicitlyTypedLambdaParameters param;
			if (!lambda.hasParentheses()) {
				param = expressionsFactory.createSingleImplicitLambdaParameter();
			} else {
				param = expressionsFactory.createImplicitlyTypedLambdaParameters();
			}
			lambda.parameters().forEach(obj -> {
				VariableDeclarationFragment frag = (VariableDeclarationFragment) obj;
				IVariableBinding binding = frag.resolveBinding();
				org.emftext.language.java.parameters.OrdinaryParameter nextParam;
				if (binding != null) {
					nextParam = jdtResolverUtility.getOrdinaryParameter(binding);
					nextParam.setTypeReference(
							jdtBindingConverterUtility.convertToTypeReferences(binding.getType()).get(0));
				} else {
					nextParam = jdtResolverUtility
							.getOrdinaryParameter(frag.getName().getIdentifier() + frag.hashCode());
					nextParam.setTypeReference(org.emftext.language.java.types.TypesFactory.eINSTANCE.createVoid());
				}
				nextParam.setName(frag.getName().getIdentifier());
				param.getParameters().add(nextParam);
			});
			result.setParameters(param);
		} else {
			org.emftext.language.java.expressions.ExplicitlyTypedLambdaParameters param = expressionsFactory
					.createExplicitlyTypedLambdaParameters();
			lambda.parameters().forEach(obj -> param.getParameters()
					.add(classifierConverterUtility.convertToOrdinaryParameter((SingleVariableDeclaration) obj)));
			result.setParameters(param);
		}
		if (lambda.getBody() instanceof Expression) {
			result.setBody(convertToExpression((Expression) lambda.getBody()));
		} else {
			result.setBody(statementConverterUtility.convertToBlock((Block) lambda.getBody()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, lambda);
		return result;
	}

	private org.emftext.language.java.expressions.Expression handleMethodReference(Expression expr) {
		return toMethodReferenceExpressionConverter.convertToMethodReferenceExpression((MethodReference) expr);
	}

	private org.emftext.language.java.expressions.Expression handleSwitchExpression(Expression expr) {
		SwitchExpression switchExpr = (SwitchExpression) expr;
		org.emftext.language.java.statements.Switch result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
				.createSwitch();
		result.setVariable(convertToExpression(switchExpr.getExpression()));
		statementConverterUtility.convertToSwitchCasesAndSet(result, switchExpr.statements());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

	private org.emftext.language.java.expressions.Expression handleCastExpression(Expression expr) {
		CastExpression castExpr = (CastExpression) expr;
		org.emftext.language.java.expressions.CastExpression result = expressionsFactory.createCastExpression();
		if (castExpr.getType().isIntersectionType()) {
			IntersectionType interType = (IntersectionType) castExpr.getType();
			result.setTypeReference(toTypeReferenceConverter.convertToTypeReference((Type) interType.types().get(0)));
			toTypeReferenceConverter.convertToArrayDimensionsAndSet((Type) interType.types().get(0), result);
			for (int index = 1; index < interType.types().size(); index++) {
				result.getAdditionalBounds()
						.add(toTypeReferenceConverter.convertToTypeReference((Type) interType.types().get(index)));
			}
		} else {
			result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(castExpr.getType()));
			toTypeReferenceConverter.convertToArrayDimensionsAndSet(castExpr.getType(), result);
		}
		result.setGeneralChild(convertToExpression(castExpr.getExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, castExpr);
		return result;
	}

	private org.emftext.language.java.expressions.Expression handlePostfixExpression(Expression expr) {
		PostfixExpression postfixExpr = (PostfixExpression) expr;
		org.emftext.language.java.expressions.SuffixUnaryModificationExpression result = expressionsFactory
				.createSuffixUnaryModificationExpression();
		if (postfixExpr.getOperator() == PostfixExpression.Operator.DECREMENT) {
			result.setOperator(org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createMinusMinus());
		} else {
			result.setOperator(org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createPlusPlus());
		}
		result.setChild((org.emftext.language.java.expressions.UnaryModificationExpressionChild) convertToExpression(
				postfixExpr.getOperand()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, postfixExpr);
		return result;
	}

	private org.emftext.language.java.expressions.Expression handlePrefixExpression(Expression expr) {
		PrefixExpression prefixExpr = (PrefixExpression) expr;
		if (prefixExpr.getOperator() == PrefixExpression.Operator.COMPLEMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.NOT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.PLUS
				|| prefixExpr.getOperator() == PrefixExpression.Operator.MINUS) {
			return toUnaryExpressionConverter.convertToUnaryExpression(prefixExpr);
		}
		if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.INCREMENT) {
			org.emftext.language.java.expressions.PrefixUnaryModificationExpression result = expressionsFactory
					.createPrefixUnaryModificationExpression();
			if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT) {
				result.setOperator(org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createMinusMinus());
			} else {
				result.setOperator(org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createPlusPlus());
			}
			result.setChild(
					(org.emftext.language.java.expressions.UnaryModificationExpressionChild) convertToExpression(
							prefixExpr.getOperand()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, prefixExpr);
			return result;
		}
		return null;
	}

	private org.emftext.language.java.expressions.Expression handleInstanceOf(Expression expr) {
		InstanceofExpression castedExpr = (InstanceofExpression) expr;
		org.emftext.language.java.expressions.InstanceOfExpression result = expressionsFactory
				.createInstanceOfExpression();
		result.setChild((InstanceOfExpressionChild) convertToExpression(castedExpr.getLeftOperand()));
		result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(castedExpr.getRightOperand()));
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(castedExpr.getRightOperand(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, castedExpr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleInfixExpression(Expression expr) {
		InfixExpression infix = (InfixExpression) expr;
		if (infix.getOperator() == InfixExpression.Operator.CONDITIONAL_OR) {
			org.emftext.language.java.expressions.ConditionalOrExpression result;
			org.emftext.language.java.expressions.Expression ex = convertToExpression(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.ConditionalOrExpression) {
				result = (org.emftext.language.java.expressions.ConditionalOrExpression) ex;
			} else {
				result = expressionsFactory.createConditionalOrExpression();
				result.getChildren().add((ConditionalOrExpressionChild) ex);
			}
			result.getChildren().add((ConditionalOrExpressionChild) convertToExpression(infix.getRightOperand()));
			infix.extendedOperands().forEach(obj -> result.getChildren()
					.add((ConditionalOrExpressionChild) convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.CONDITIONAL_AND) {
			org.emftext.language.java.expressions.ConditionalAndExpression result;
			org.emftext.language.java.expressions.Expression ex = convertToExpression(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.ConditionalAndExpression) {
				result = (org.emftext.language.java.expressions.ConditionalAndExpression) ex;
			} else {
				result = expressionsFactory.createConditionalAndExpression();
				result.getChildren().add((ConditionalAndExpressionChild) ex);
			}
			result.getChildren().add((ConditionalAndExpressionChild) convertToExpression(infix.getRightOperand()));
			infix.extendedOperands().forEach(obj -> result.getChildren()
					.add((ConditionalAndExpressionChild) convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.OR) {
			org.emftext.language.java.expressions.InclusiveOrExpression result;
			org.emftext.language.java.expressions.Expression ex = convertToExpression(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.InclusiveOrExpression) {
				result = (org.emftext.language.java.expressions.InclusiveOrExpression) ex;
			} else {
				result = expressionsFactory.createInclusiveOrExpression();
				result.getChildren().add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) ex);
			}
			result.getChildren()
					.add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) convertToExpression(
							infix.getRightOperand()));
			infix.extendedOperands()
					.forEach(obj -> result.getChildren()
							.add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) convertToExpression(
									(Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.XOR) {
			org.emftext.language.java.expressions.ExclusiveOrExpression result;
			org.emftext.language.java.expressions.Expression ex = convertToExpression(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.ExclusiveOrExpression) {
				result = (org.emftext.language.java.expressions.ExclusiveOrExpression) ex;
			} else {
				result = expressionsFactory.createExclusiveOrExpression();
				result.getChildren().add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) ex);
			}
			result.getChildren()
					.add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) convertToExpression(
							infix.getRightOperand()));
			infix.extendedOperands()
					.forEach(obj -> result.getChildren()
							.add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) convertToExpression(
									(Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.AND) {
			org.emftext.language.java.expressions.AndExpression result;
			org.emftext.language.java.expressions.Expression ex = convertToExpression(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.AndExpression) {
				result = (org.emftext.language.java.expressions.AndExpression) ex;
			} else {
				result = expressionsFactory.createAndExpression();
				result.getChildren().add((org.emftext.language.java.expressions.AndExpressionChild) ex);
			}
			result.getChildren().add((org.emftext.language.java.expressions.AndExpressionChild) convertToExpression(
					infix.getRightOperand()));
			infix.extendedOperands().forEach(obj -> result.getChildren().add(
					(org.emftext.language.java.expressions.AndExpressionChild) convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.EQUALS
				|| infix.getOperator() == InfixExpression.Operator.NOT_EQUALS) {
			return toEqualityExpressionConverter.convertToEqualityExpression(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.GREATER
				|| infix.getOperator() == InfixExpression.Operator.GREATER_EQUALS
				|| infix.getOperator() == InfixExpression.Operator.LESS
				|| infix.getOperator() == InfixExpression.Operator.LESS_EQUALS) {
			return toRelationExpressionConverter.convertToRelationExpression(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.LEFT_SHIFT
				|| infix.getOperator() == InfixExpression.Operator.RIGHT_SHIFT_SIGNED
				|| infix.getOperator() == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
			return toShiftExpressionConverter.convertToShiftExpression(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.PLUS
				|| infix.getOperator() == InfixExpression.Operator.MINUS) {
			return toAdditiveExpressionConverter.convertToAdditiveExpression(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.TIMES
				|| infix.getOperator() == InfixExpression.Operator.DIVIDE
				|| infix.getOperator() == InfixExpression.Operator.REMAINDER) {
			return toMultiplicativeExpressionConverter.convertToMultiplicativeExpression(infix);
		} else {
			return null;
		}
	}

	private org.emftext.language.java.expressions.Expression handleConditionalExpression(Expression expr) {
		return toConditionalExpressionConverter.convertToConditionalExpression((ConditionalExpression) expr);
	}

	private org.emftext.language.java.expressions.Expression handleAssignment(Expression expr) {
		Assignment assign = (Assignment) expr;
		org.emftext.language.java.expressions.AssignmentExpression result = expressionsFactory
				.createAssignmentExpression();
		result.setChild((org.emftext.language.java.expressions.AssignmentExpressionChild) convertToExpression(
				assign.getLeftHandSide()));
		result.setAssignmentOperator(toAssignmentOperatorConverter.convertToAssignmentOperator(assign.getOperator()));
		result.setValue(convertToExpression(assign.getRightHandSide()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void setToAssignmentOperatorConverter(ToAssignmentConverter toAssignmentOperatorConverter) {
		this.toAssignmentOperatorConverter = toAssignmentOperatorConverter;
	}

	void setToConditionalExpressionConverter(ToConditionalExpressionConverter toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	void setToEqualityExpressionConverter(ToEqualityExpressionConverter toEqualityExpressionConverter) {
		this.toEqualityExpressionConverter = toEqualityExpressionConverter;
	}

	void setToRelationExpressionConverter(ToRelationExpressionConverter toRelationExpressionConverter) {
		this.toRelationExpressionConverter = toRelationExpressionConverter;
	}

	void setToShiftExpressionConverter(ToShiftExpressionConverter toShiftExpressionConverter) {
		this.toShiftExpressionConverter = toShiftExpressionConverter;
	}

	void setToAdditiveExpressionConverter(ToAdditiveExpressionConverter toAdditiveExpressionConverter) {
		this.toAdditiveExpressionConverter = toAdditiveExpressionConverter;
	}

	void setToMultiplicativeExpressionConverter(
			ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter) {
		this.toMultiplicativeExpressionConverter = toMultiplicativeExpressionConverter;
	}

	void setToUnaryExpressionConverter(ToUnaryExpressionConverter toUnaryExpressionConverter) {
		this.toUnaryExpressionConverter = toUnaryExpressionConverter;
	}

	void setToMethodReferenceExpressionConverter(
			ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	void setToPrimaryExpressionConverter(ToPrimaryExpressionConverter toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

}
