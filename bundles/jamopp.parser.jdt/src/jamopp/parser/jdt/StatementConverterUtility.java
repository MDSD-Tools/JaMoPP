/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package jamopp.parser.jdt;

import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.YieldStatement;

class StatementConverterUtility {

	private final ReferenceConverterUtility referenceConverterUtility;
	private final LayoutInformationConverter layoutInformationConverter;
	private final JDTResolverUtility jdtResolverUtility;
	private final ExpressionConverterUtility expressionConverterUtility;
	private final ClassifierConverterUtility classifierConverterUtility;
	private final BaseConverterUtility baseConverterUtility;
	private final UtilNamedElement utilNamedElement;

	private HashSet<org.emftext.language.java.statements.JumpLabel> currentJumpLabels = new HashSet<>();

	StatementConverterUtility(ReferenceConverterUtility referenceConverterUtility,
			LayoutInformationConverter layoutInformationConverter, JDTResolverUtility jdtResolverUtility,
			ExpressionConverterUtility expressionConverterUtility,
			ClassifierConverterUtility classifierConverterUtility, BaseConverterUtility baseConverterUtility, UtilNamedElement utilNamedElement) {
		this.referenceConverterUtility = referenceConverterUtility;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.classifierConverterUtility = classifierConverterUtility;
		this.baseConverterUtility = baseConverterUtility;
		this.utilNamedElement = utilNamedElement;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.statements.Block convertToBlock(Block block) {
		org.emftext.language.java.statements.Block result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
				.createBlock();
		result.setName("");
		block.statements().forEach(obj -> result.getStatements().add(convertToStatement((Statement) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, block);
		return result;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.statements.Statement convertToStatement(Statement statement) {
		if (statement.getNodeType() == ASTNode.ASSERT_STATEMENT) {
			AssertStatement assertSt = (AssertStatement) statement;
			org.emftext.language.java.statements.Assert result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createAssert();
			result.setCondition(expressionConverterUtility.convertToExpression(assertSt.getExpression()));
			if (assertSt.getMessage() != null) {
				result.setErrorMessage(expressionConverterUtility.convertToExpression(assertSt.getMessage()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, assertSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.BLOCK) {
			return convertToBlock((Block) statement);
		}
		if (statement.getNodeType() == ASTNode.BREAK_STATEMENT) {
			BreakStatement breakSt = (BreakStatement) statement;
			org.emftext.language.java.statements.Break result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createBreak();
			if (breakSt.getLabel() != null) {
				org.emftext.language.java.statements.JumpLabel proxyTarget = currentJumpLabels.stream()
						.filter(label -> label.getName().equals(breakSt.getLabel().getIdentifier())).findFirst().get();
				result.setTarget(proxyTarget);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, breakSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.CONTINUE_STATEMENT) {
			ContinueStatement conSt = (ContinueStatement) statement;
			org.emftext.language.java.statements.Continue result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createContinue();
			if (conSt.getLabel() != null) {
				org.emftext.language.java.statements.JumpLabel proxyTarget = currentJumpLabels.stream()
						.filter(label -> label.getName().equals(conSt.getLabel().getIdentifier())).findFirst().get();
				result.setTarget(proxyTarget);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, conSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.DO_STATEMENT) {
			DoStatement doSt = (DoStatement) statement;
			org.emftext.language.java.statements.DoWhileLoop result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createDoWhileLoop();
			result.setCondition(expressionConverterUtility.convertToExpression(doSt.getExpression()));
			result.setStatement(convertToStatement(doSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, doSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.EMPTY_STATEMENT) {
			org.emftext.language.java.statements.EmptyStatement result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createEmptyStatement();
			layoutInformationConverter.convertToMinimalLayoutInformation(result, statement);
			return result;
		}
		if (statement.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
			EnhancedForStatement forSt = (EnhancedForStatement) statement;
			org.emftext.language.java.statements.ForEachLoop result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createForEachLoop();
			result.setNext(classifierConverterUtility.convertToOrdinaryParameter(forSt.getParameter()));
			result.setCollection(expressionConverterUtility.convertToExpression(forSt.getExpression()));
			result.setStatement(convertToStatement(forSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {
			ExpressionStatement exprSt = (ExpressionStatement) statement;
			if (exprSt.getExpression().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION) {
				org.emftext.language.java.statements.LocalVariableStatement result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
						.createLocalVariableStatement();
				result.setVariable(convertToLocalVariable((VariableDeclarationExpression) exprSt.getExpression()));
				layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
				return result;
			}
			org.emftext.language.java.statements.ExpressionStatement result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createExpressionStatement();
			result.setExpression(expressionConverterUtility.convertToExpression(exprSt.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.FOR_STATEMENT) {
			ForStatement forSt = (ForStatement) statement;
			org.emftext.language.java.statements.ForLoop result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createForLoop();
			if (forSt.initializers().size() == 1
					&& forSt.initializers().get(0) instanceof VariableDeclarationExpression) {
				result.setInit(convertToLocalVariable((VariableDeclarationExpression) forSt.initializers().get(0)));
			} else {
				org.emftext.language.java.expressions.ExpressionList ini = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
						.createExpressionList();
				forSt.initializers().forEach(obj -> ini.getExpressions()
						.add(expressionConverterUtility.convertToExpression((Expression) obj)));
				result.setInit(ini);
			}
			if (forSt.getExpression() != null) {
				result.setCondition(expressionConverterUtility.convertToExpression(forSt.getExpression()));
			}
			forSt.updaters().forEach(
					obj -> result.getUpdates().add(expressionConverterUtility.convertToExpression((Expression) obj)));
			result.setStatement(convertToStatement(forSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.IF_STATEMENT) {
			IfStatement ifSt = (IfStatement) statement;
			org.emftext.language.java.statements.Condition result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createCondition();
			result.setCondition(expressionConverterUtility.convertToExpression(ifSt.getExpression()));
			result.setStatement(convertToStatement(ifSt.getThenStatement()));
			if (ifSt.getElseStatement() != null) {
				result.setElseStatement(convertToStatement(ifSt.getElseStatement()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, ifSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.LABELED_STATEMENT) {
			LabeledStatement labelSt = (LabeledStatement) statement;
			org.emftext.language.java.statements.JumpLabel result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createJumpLabel();
			utilNamedElement.convertToSimpleNameOnlyAndSet(labelSt.getLabel(), result);
			currentJumpLabels.add(result);
			result.setStatement(convertToStatement(labelSt.getBody()));
			currentJumpLabels.remove(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, labelSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.RETURN_STATEMENT) {
			ReturnStatement retSt = (ReturnStatement) statement;
			org.emftext.language.java.statements.Return result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createReturn();
			if (retSt.getExpression() != null) {
				result.setReturnValue(expressionConverterUtility.convertToExpression(retSt.getExpression()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, retSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.SWITCH_STATEMENT) {
			return convertToSwitch((SwitchStatement) statement);
		}
		if (statement.getNodeType() == ASTNode.SYNCHRONIZED_STATEMENT) {
			SynchronizedStatement synSt = (SynchronizedStatement) statement;
			org.emftext.language.java.statements.SynchronizedBlock result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createSynchronizedBlock();
			result.setLockProvider(expressionConverterUtility.convertToExpression(synSt.getExpression()));
			result.setBlock(convertToBlock(synSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, synSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.THROW_STATEMENT) {
			ThrowStatement throwSt = (ThrowStatement) statement;
			org.emftext.language.java.statements.Throw result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createThrow();
			result.setThrowable(expressionConverterUtility.convertToExpression(throwSt.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, throwSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.TRY_STATEMENT) {
			TryStatement trySt = (TryStatement) statement;
			org.emftext.language.java.statements.TryBlock result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createTryBlock();
			trySt.resources().forEach(obj -> {
				Expression resExpr = (Expression) obj;
				if (resExpr instanceof VariableDeclarationExpression) {
					result.getResources().add(convertToLocalVariable((VariableDeclarationExpression) resExpr));
				} else {
					result.getResources()
							.add((org.emftext.language.java.references.ElementReference) referenceConverterUtility
									.convertToReference(resExpr));
				}
			});
			result.setBlock(convertToBlock(trySt.getBody()));
			trySt.catchClauses().forEach(obj -> result.getCatchBlocks().add(convertToCatchBlock((CatchClause) obj)));
			if (trySt.getFinally() != null) {
				result.setFinallyBlock(convertToBlock(trySt.getFinally()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, trySt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT) {
			TypeDeclarationStatement declSt = (TypeDeclarationStatement) statement;
			return classifierConverterUtility.convertToConcreteClassifier(declSt.getDeclaration());
		}
		if (statement.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
			VariableDeclarationStatement varSt = (VariableDeclarationStatement) statement;
			org.emftext.language.java.statements.LocalVariableStatement result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createLocalVariableStatement();
			VariableDeclarationFragment frag = (VariableDeclarationFragment) varSt.fragments().get(0);
			org.emftext.language.java.variables.LocalVariable locVar;
			IVariableBinding binding = frag.resolveBinding();
			if (binding == null) {
				locVar = jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
			} else {
				locVar = jdtResolverUtility.getLocalVariable(binding);
			}
			utilNamedElement.convertToSimpleNameOnlyAndSet(frag.getName(), locVar);
			varSt.modifiers().forEach(obj -> locVar.getAnnotationsAndModifiers()
					.add(baseConverterUtility.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
			locVar.setTypeReference(baseConverterUtility.convertToTypeReference(varSt.getType()));
			baseConverterUtility.convertToArrayDimensionsAndSet(varSt.getType(), locVar);
			frag.extraDimensions()
					.forEach(obj -> baseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, locVar));
			if (frag.getInitializer() != null) {
				locVar.setInitialValue(expressionConverterUtility.convertToExpression(frag.getInitializer()));
			}
			for (int index = 1; index < varSt.fragments().size(); index++) {
				locVar.getAdditionalLocalVariables().add(
						convertToAdditionalLocalVariable((VariableDeclarationFragment) varSt.fragments().get(index)));
			}
			result.setVariable(locVar);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, varSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.WHILE_STATEMENT) {
			WhileStatement whileSt = (WhileStatement) statement;
			org.emftext.language.java.statements.WhileLoop result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createWhileLoop();
			result.setCondition(expressionConverterUtility.convertToExpression(whileSt.getExpression()));
			result.setStatement(convertToStatement(whileSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, whileSt);
			return result;
		}
		if (statement.getNodeType() != ASTNode.YIELD_STATEMENT) {
			org.emftext.language.java.statements.ExpressionStatement result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createExpressionStatement();
			result.setExpression(referenceConverterUtility.convertToReference(statement));
			return result;
		}
		YieldStatement yieldSt = (YieldStatement) statement;

		org.emftext.language.java.statements.YieldStatement result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
				.createYieldStatement();
		if (yieldSt.getExpression() != null) {
			result.setYieldExpression(expressionConverterUtility.convertToExpression(yieldSt.getExpression()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, yieldSt);
		return result;
	}

	private org.emftext.language.java.statements.Switch convertToSwitch(SwitchStatement switchSt) {
		org.emftext.language.java.statements.Switch result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
				.createSwitch();
		result.setVariable(expressionConverterUtility.convertToExpression(switchSt.getExpression()));
		convertToSwitchCasesAndSet(result, switchSt.statements());
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchSt);
		return result;
	}

	@SuppressWarnings("rawtypes")
	void convertToSwitchCasesAndSet(org.emftext.language.java.statements.Switch switchExprSt,
			List switchStatementList) {
		org.emftext.language.java.statements.SwitchCase currentCase = null;
		for (Object element : switchStatementList) {
			Statement st = (Statement) element;
			if (st.getNodeType() == ASTNode.SWITCH_CASE) {
				currentCase = convertToSwitchCase((SwitchCase) st);
				switchExprSt.getCases().add(currentCase);
			} else if (currentCase instanceof org.emftext.language.java.statements.SwitchRule
					&& st.getNodeType() == ASTNode.YIELD_STATEMENT) {
				YieldStatement ys = (YieldStatement) st;
				org.emftext.language.java.statements.ExpressionStatement exprSt = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
						.createExpressionStatement();
				exprSt.setExpression(expressionConverterUtility.convertToExpression(ys.getExpression()));
				currentCase.getStatements().add(exprSt);
			} else {
				currentCase.getStatements().add(convertToStatement(st));
			}
		}
	}

	private org.emftext.language.java.statements.SwitchCase convertToSwitchCase(SwitchCase switchCase) {
		org.emftext.language.java.statements.SwitchCase result = null;
		if (switchCase.isSwitchLabeledRule() && switchCase.isDefault()) {
			result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE.createDefaultSwitchRule();
		} else if (switchCase.isSwitchLabeledRule() && !switchCase.isDefault()) {
			org.emftext.language.java.statements.NormalSwitchRule normalRule = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createNormalSwitchRule();
			normalRule.setCondition(
					expressionConverterUtility.convertToExpression((Expression) switchCase.expressions().get(0)));
			for (int index = 1; index < switchCase.expressions().size(); index++) {
				Expression expr = (Expression) switchCase.expressions().get(index);
				normalRule.getAdditionalConditions().add(expressionConverterUtility.convertToExpression(expr));
			}
			result = normalRule;
		} else if (!switchCase.isSwitchLabeledRule() && switchCase.isDefault()) {
			result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE.createDefaultSwitchCase();
		} else { // !switchCase.isSwitchLabeledRule() && !switchCase.isDefault()
			org.emftext.language.java.statements.NormalSwitchCase normalCase = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
					.createNormalSwitchCase();
			normalCase.setCondition(
					expressionConverterUtility.convertToExpression((Expression) switchCase.expressions().get(0)));
			for (int index = 1; index < switchCase.expressions().size(); index++) {
				Expression expr = (Expression) switchCase.expressions().get(index);
				normalCase.getAdditionalConditions().add(expressionConverterUtility.convertToExpression(expr));
			}
			result = normalCase;
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, switchCase);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.statements.CatchBlock convertToCatchBlock(CatchClause block) {
		org.emftext.language.java.statements.CatchBlock result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
				.createCatchBlock();
		SingleVariableDeclaration decl = block.getException();
		org.emftext.language.java.parameters.CatchParameter param;
		IVariableBinding binding = decl.resolveBinding();
		if (binding == null) {
			param = jdtResolverUtility.getCatchParameter(decl.getName().getIdentifier() + "-" + block.hashCode());
		} else {
			param = jdtResolverUtility.getCatchParameter(binding);
		}
		decl.modifiers().forEach(obj -> param.getAnnotationsAndModifiers()
				.add(baseConverterUtility.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		if (decl.getType().isUnionType()) {
			UnionType un = (UnionType) decl.getType();
			param.setTypeReference(baseConverterUtility.convertToTypeReference((Type) un.types().get(0)));
			for (int index = 1; index < un.types().size(); index++) {
				param.getTypeReferences()
						.add(baseConverterUtility.convertToTypeReference((Type) un.types().get(index)));
			}
		} else {
			param.setTypeReference(baseConverterUtility.convertToTypeReference(decl.getType()));
		}
		utilNamedElement.convertToSimpleNameOnlyAndSet(decl.getName(), param);
		result.setParameter(param);
		result.setBlock(convertToBlock(block.getBody()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, block);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.variables.AdditionalLocalVariable convertToAdditionalLocalVariable(
			VariableDeclarationFragment frag) {
		org.emftext.language.java.variables.AdditionalLocalVariable result;
		IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			result = jdtResolverUtility
					.getAdditionalLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			result = jdtResolverUtility.getAdditionalLocalVariable(frag.resolveBinding());
		}
		utilNamedElement.convertToSimpleNameOnlyAndSet(frag.getName(), result);
		frag.extraDimensions()
				.forEach(obj -> baseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			result.setInitialValue(expressionConverterUtility.convertToExpression(frag.getInitializer()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, frag);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.variables.LocalVariable convertToLocalVariable(
			VariableDeclarationExpression expr) {
		VariableDeclarationFragment frag = (VariableDeclarationFragment) expr.fragments().get(0);
		org.emftext.language.java.variables.LocalVariable loc;
		IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			loc = jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			loc = jdtResolverUtility.getLocalVariable(binding);
		}
		utilNamedElement.convertToSimpleNameOnlyAndSet(frag.getName(), loc);
		expr.modifiers().forEach(obj -> loc.getAnnotationsAndModifiers()
				.add(baseConverterUtility.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		loc.setTypeReference(baseConverterUtility.convertToTypeReference(expr.getType()));
		baseConverterUtility.convertToArrayDimensionsAndSet(expr.getType(), loc);
		frag.extraDimensions()
				.forEach(obj -> baseConverterUtility.convertToArrayDimensionAfterAndSet((Dimension) obj, loc));
		if (frag.getInitializer() != null) {
			loc.setInitialValue(expressionConverterUtility.convertToExpression(frag.getInitializer()));
		}
		for (int index = 1; index < expr.fragments().size(); index++) {
			loc.getAdditionalLocalVariables()
					.add(convertToAdditionalLocalVariable((VariableDeclarationFragment) expr.fragments().get(index)));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(loc, expr);
		return loc;
	}
}
