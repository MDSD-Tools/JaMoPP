package jamopp.parser.jdt.converter.implementation;

import java.util.HashSet;

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
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.YieldStatement;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.helper.ToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.BlockToBlockConverter;
import jamopp.parser.jdt.converter.interfaces.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class StatementToStatementConverterImpl implements StatementToStatementConverter {

	private final ExpressionsFactory expressionsFactory;
	private final StatementsFactory statementsFactory;;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final ToConcreteClassifierConverter classifierConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToModifierOrAnnotationInstanceConverter annotationInstanceConverter;
	private final ToOrdinaryParameterConverter toOrdinaryParameterConverter;
	private final Provider<ToReferenceConverterFromStatement> toReferenceConverterFromStatement;
	private final Provider<ToReferenceConverterFromExpression> toReferenceConverterFromExpression;
	private final BlockToBlockConverter blockToBlockConverter;
	private final ToLocalVariableConverter toLocalVariableConverter;
	private final SwitchToSwitchConverter switchToSwitchConverter;
	private final ToCatchblockConverter toCatchblockConverter;
	private final ToAdditionalLocalVariableConverter toAdditionalLocalVariableConverter;

	private HashSet<org.emftext.language.java.statements.JumpLabel> currentJumpLabels = new HashSet<>();

	@Inject
	StatementToStatementConverterImpl(UtilNamedElement utilNamedElement,
			ToTypeReferenceConverter toTypeReferenceConverter,
			Provider<ToReferenceConverterFromStatement> toReferenceConverterFromStatement,
			Provider<ToReferenceConverterFromExpression> toReferenceConverterFromExpression,
			ToOrdinaryParameterConverter toOrdinaryParameterConverter,
			ToLocalVariableConverter toLocalVariableConverter, ToCatchblockConverter toCatchblockConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			ToAdditionalLocalVariableConverter toAdditionalLocalVariableConverter,
			SwitchToSwitchConverter switchToSwitchConverter, StatementsFactory statementsFactory,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ExpressionsFactory expressionsFactory, ToExpressionConverter expressionConverterUtility,
			ToConcreteClassifierConverter classifierConverterUtility, BlockToBlockConverter blockToBlockConverter,
			ToModifierOrAnnotationInstanceConverter annotationInstanceConverter) {
		this.expressionsFactory = expressionsFactory;
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.classifierConverterUtility = classifierConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.toReferenceConverterFromStatement = toReferenceConverterFromStatement;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
		this.blockToBlockConverter = blockToBlockConverter;
		this.toLocalVariableConverter = toLocalVariableConverter;
		this.switchToSwitchConverter = switchToSwitchConverter;
		this.toCatchblockConverter = toCatchblockConverter;
		this.toAdditionalLocalVariableConverter = toAdditionalLocalVariableConverter;
	}

	@SuppressWarnings("unchecked")
	public org.emftext.language.java.statements.Statement convert(Statement statement) {
		if (statement.getNodeType() == ASTNode.ASSERT_STATEMENT) {
			AssertStatement assertSt = (AssertStatement) statement;
			org.emftext.language.java.statements.Assert result = statementsFactory.createAssert();
			result.setCondition(expressionConverterUtility.convert(assertSt.getExpression()));
			if (assertSt.getMessage() != null) {
				result.setErrorMessage(expressionConverterUtility.convert(assertSt.getMessage()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, assertSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.BLOCK) {
			return blockToBlockConverter.convert((Block) statement);
		}
		if (statement.getNodeType() == ASTNode.BREAK_STATEMENT) {
			BreakStatement breakSt = (BreakStatement) statement;
			org.emftext.language.java.statements.Break result = statementsFactory.createBreak();
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
			org.emftext.language.java.statements.Continue result = statementsFactory.createContinue();
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
			org.emftext.language.java.statements.DoWhileLoop result = statementsFactory.createDoWhileLoop();
			result.setCondition(expressionConverterUtility.convert(doSt.getExpression()));
			result.setStatement(convert(doSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, doSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.EMPTY_STATEMENT) {
			org.emftext.language.java.statements.EmptyStatement result = statementsFactory.createEmptyStatement();
			layoutInformationConverter.convertToMinimalLayoutInformation(result, statement);
			return result;
		}
		if (statement.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
			EnhancedForStatement forSt = (EnhancedForStatement) statement;
			org.emftext.language.java.statements.ForEachLoop result = statementsFactory.createForEachLoop();
			result.setNext(toOrdinaryParameterConverter.convert(forSt.getParameter()));
			result.setCollection(expressionConverterUtility.convert(forSt.getExpression()));
			result.setStatement(convert(forSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {
			ExpressionStatement exprSt = (ExpressionStatement) statement;
			if (exprSt.getExpression().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION) {
				org.emftext.language.java.statements.LocalVariableStatement result = statementsFactory
						.createLocalVariableStatement();
				result.setVariable(toLocalVariableConverter
						.convert((VariableDeclarationExpression) exprSt.getExpression()));
				layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
				return result;
			}
			org.emftext.language.java.statements.ExpressionStatement result = statementsFactory
					.createExpressionStatement();
			result.setExpression(expressionConverterUtility.convert(exprSt.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.FOR_STATEMENT) {
			ForStatement forSt = (ForStatement) statement;
			org.emftext.language.java.statements.ForLoop result = statementsFactory.createForLoop();
			if (forSt.initializers().size() == 1
					&& forSt.initializers().get(0) instanceof VariableDeclarationExpression) {
				result.setInit(toLocalVariableConverter
						.convert((VariableDeclarationExpression) forSt.initializers().get(0)));
			} else {
				org.emftext.language.java.expressions.ExpressionList ini = expressionsFactory.createExpressionList();
				forSt.initializers()
						.forEach(obj -> ini.getExpressions().add(expressionConverterUtility.convert((Expression) obj)));
				result.setInit(ini);
			}
			if (forSt.getExpression() != null) {
				result.setCondition(expressionConverterUtility.convert(forSt.getExpression()));
			}
			forSt.updaters()
					.forEach(obj -> result.getUpdates().add(expressionConverterUtility.convert((Expression) obj)));
			result.setStatement(convert(forSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.IF_STATEMENT) {
			IfStatement ifSt = (IfStatement) statement;
			org.emftext.language.java.statements.Condition result = statementsFactory.createCondition();
			result.setCondition(expressionConverterUtility.convert(ifSt.getExpression()));
			result.setStatement(convert(ifSt.getThenStatement()));
			if (ifSt.getElseStatement() != null) {
				result.setElseStatement(convert(ifSt.getElseStatement()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, ifSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.LABELED_STATEMENT) {
			LabeledStatement labelSt = (LabeledStatement) statement;
			org.emftext.language.java.statements.JumpLabel result = statementsFactory.createJumpLabel();
			utilNamedElement.setNameOfElement(labelSt.getLabel(), result);
			currentJumpLabels.add(result);
			result.setStatement(convert(labelSt.getBody()));
			currentJumpLabels.remove(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, labelSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.RETURN_STATEMENT) {
			ReturnStatement retSt = (ReturnStatement) statement;
			org.emftext.language.java.statements.Return result = statementsFactory.createReturn();
			if (retSt.getExpression() != null) {
				result.setReturnValue(expressionConverterUtility.convert(retSt.getExpression()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, retSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.SWITCH_STATEMENT) {
			return switchToSwitchConverter.convert((SwitchStatement) statement);
		}
		if (statement.getNodeType() == ASTNode.SYNCHRONIZED_STATEMENT) {
			SynchronizedStatement synSt = (SynchronizedStatement) statement;
			org.emftext.language.java.statements.SynchronizedBlock result = statementsFactory.createSynchronizedBlock();
			result.setLockProvider(expressionConverterUtility.convert(synSt.getExpression()));
			result.setBlock(blockToBlockConverter.convert(synSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, synSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.THROW_STATEMENT) {
			ThrowStatement throwSt = (ThrowStatement) statement;
			org.emftext.language.java.statements.Throw result = statementsFactory.createThrow();
			result.setThrowable(expressionConverterUtility.convert(throwSt.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, throwSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.TRY_STATEMENT) {
			TryStatement trySt = (TryStatement) statement;
			org.emftext.language.java.statements.TryBlock result = statementsFactory.createTryBlock();
			trySt.resources().forEach(obj -> {
				Expression resExpr = (Expression) obj;
				if (resExpr instanceof VariableDeclarationExpression) {
					result.getResources().add(
							toLocalVariableConverter.convert((VariableDeclarationExpression) resExpr));
				} else {
					result.getResources().add(
							(org.emftext.language.java.references.ElementReference) toReferenceConverterFromExpression
									.get().convertToReference(resExpr));
				}
			});
			result.setBlock(blockToBlockConverter.convert(trySt.getBody()));
			trySt.catchClauses().forEach(
					obj -> result.getCatchBlocks().add(toCatchblockConverter.convert((CatchClause) obj)));
			if (trySt.getFinally() != null) {
				result.setFinallyBlock(blockToBlockConverter.convert(trySt.getFinally()));
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, trySt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT) {
			TypeDeclarationStatement declSt = (TypeDeclarationStatement) statement;
			return classifierConverterUtility.convert(declSt.getDeclaration());
		}
		if (statement.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
			VariableDeclarationStatement varSt = (VariableDeclarationStatement) statement;
			org.emftext.language.java.statements.LocalVariableStatement result = statementsFactory
					.createLocalVariableStatement();
			VariableDeclarationFragment frag = (VariableDeclarationFragment) varSt.fragments().get(0);
			org.emftext.language.java.variables.LocalVariable locVar;
			IVariableBinding binding = frag.resolveBinding();
			if (binding == null) {
				locVar = jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
			} else {
				locVar = jdtResolverUtility.getLocalVariable(binding);
			}
			utilNamedElement.setNameOfElement(frag.getName(), locVar);
			varSt.modifiers().forEach(obj -> locVar.getAnnotationsAndModifiers()
					.add(annotationInstanceConverter.convert((IExtendedModifier) obj)));
			locVar.setTypeReference(toTypeReferenceConverter.convert(varSt.getType()));
			toTypeReferenceConverter.convertToArrayDimensionsAndSet(varSt.getType(), locVar);
			frag.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
					.convert((Dimension) obj, locVar));
			if (frag.getInitializer() != null) {
				locVar.setInitialValue(expressionConverterUtility.convert(frag.getInitializer()));
			}
			for (int index = 1; index < varSt.fragments().size(); index++) {
				locVar.getAdditionalLocalVariables().add(toAdditionalLocalVariableConverter
						.convert((VariableDeclarationFragment) varSt.fragments().get(index)));
			}
			result.setVariable(locVar);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, varSt);
			return result;
		}
		if (statement.getNodeType() == ASTNode.WHILE_STATEMENT) {
			WhileStatement whileSt = (WhileStatement) statement;
			org.emftext.language.java.statements.WhileLoop result = statementsFactory.createWhileLoop();
			result.setCondition(expressionConverterUtility.convert(whileSt.getExpression()));
			result.setStatement(convert(whileSt.getBody()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, whileSt);
			return result;
		}
		if (statement.getNodeType() != ASTNode.YIELD_STATEMENT) {
			org.emftext.language.java.statements.ExpressionStatement result = statementsFactory
					.createExpressionStatement();
			result.setExpression(toReferenceConverterFromStatement.get().convert(statement));
			return result;
		}
		YieldStatement yieldSt = (YieldStatement) statement;

		org.emftext.language.java.statements.YieldStatement result = statementsFactory.createYieldStatement();
		if (yieldSt.getExpression() != null) {
			result.setYieldExpression(expressionConverterUtility.convert(yieldSt.getExpression()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, yieldSt);
		return result;
	}

}
