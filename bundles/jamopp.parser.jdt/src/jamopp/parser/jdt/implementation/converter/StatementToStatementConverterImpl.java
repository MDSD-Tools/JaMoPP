package jamopp.parser.jdt.implementation.converter;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
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
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.YieldStatement;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;

public class StatementToStatementConverterImpl
		implements Converter<Statement, org.emftext.language.java.statements.Statement> {

	private final ExpressionsFactory expressionsFactory;
	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final UtilReferenceWalker utilReferenceWalker;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter;
	private final Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final Converter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter;
	private final Converter<VariableDeclarationExpression, org.emftext.language.java.variables.LocalVariable> toLocalVariableConverter;
	private final Converter<SwitchStatement, Switch> switchToSwitchConverter;
	private final Converter<CatchClause, CatchBlock> toCatchblockConverter;
	private final Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter;

	private final HashSet<org.emftext.language.java.statements.JumpLabel> currentJumpLabels = new HashSet<>();

	private Converter<Statement, org.emftext.language.java.references.Reference> toReferenceConverterFromStatement;
	private Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression;

	@Inject
	StatementToStatementConverterImpl(UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			UtilNamedElement utilNamedElement, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			Converter<VariableDeclarationExpression, org.emftext.language.java.variables.LocalVariable> toLocalVariableConverter,
			Converter<CatchClause, CatchBlock> toCatchblockConverter,
			Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter,
			Converter<SwitchStatement, Switch> switchToSwitchConverter, StatementsFactory statementsFactory,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ExpressionsFactory expressionsFactory,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility,
			Converter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter,
			UtilReferenceWalker utilReferenceWalker) {
		this.expressionsFactory = expressionsFactory;
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.utilReferenceWalker = utilReferenceWalker;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.classifierConverterUtility = classifierConverterUtility;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.blockToBlockConverter = blockToBlockConverter;
		this.toLocalVariableConverter = toLocalVariableConverter;
		this.switchToSwitchConverter = switchToSwitchConverter;
		this.toCatchblockConverter = toCatchblockConverter;
		this.toAdditionalLocalVariableConverter = toAdditionalLocalVariableConverter;
	}

	@Override
	public org.emftext.language.java.statements.Statement convert(Statement statement) {
		if (statement.getNodeType() == ASTNode.ASSERT_STATEMENT) {
			return handleAssertStatement(statement);
		} else if (statement.getNodeType() == ASTNode.BLOCK) {
			return handleBlock(statement);
		} else if (statement.getNodeType() == ASTNode.BREAK_STATEMENT) {
			return handleBreakStatement(statement);
		} else if (statement.getNodeType() == ASTNode.CONTINUE_STATEMENT) {
			return handleContinueStatement(statement);
		} else if (statement.getNodeType() == ASTNode.DO_STATEMENT) {
			return handleDoStatement(statement);
		} else if (statement.getNodeType() == ASTNode.EMPTY_STATEMENT) {
			return handleEmptyStatement(statement);
		} else if (statement.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
			return handleEnhancedForStatement(statement);
		} else if (statement.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {
			return handleExpressionStatement(statement);
		} else if (statement.getNodeType() == ASTNode.FOR_STATEMENT) {
			return handleForStatement(statement);
		} else if (statement.getNodeType() == ASTNode.IF_STATEMENT) {
			return handleIfStatement(statement);
		} else if (statement.getNodeType() == ASTNode.LABELED_STATEMENT) {
			return handleLabeledStatement(statement);
		} else if (statement.getNodeType() == ASTNode.RETURN_STATEMENT) {
			return handleReturnStatement(statement);
		} else if (statement.getNodeType() == ASTNode.SWITCH_STATEMENT) {
			return handleSwitchStatement(statement);
		} else if (statement.getNodeType() == ASTNode.SYNCHRONIZED_STATEMENT) {
			return handleSynchonizedStatement(statement);
		} else if (statement.getNodeType() == ASTNode.THROW_STATEMENT) {
			return handleThrowStatement(statement);
		} else if (statement.getNodeType() == ASTNode.TRY_STATEMENT) {
			return handleTryStatement(statement);
		} else if (statement.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT) {
			return handleTypeDeclarationStatement(statement);
		} else if (statement.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
			return handleVariableDeclarationStatement(statement);
		} else if (statement.getNodeType() == ASTNode.WHILE_STATEMENT) {
			return handleWhileStatement(statement);
		} else if (statement.getNodeType() != ASTNode.YIELD_STATEMENT) {
			return handleOther(statement);
		} else {
			return handleYieldStatement(statement);
		}
	}

	private org.emftext.language.java.statements.Statement handleYieldStatement(Statement statement) {
		YieldStatement yieldSt = (YieldStatement) statement;
		org.emftext.language.java.statements.YieldStatement result = this.statementsFactory.createYieldStatement();
		if (yieldSt.getExpression() != null) {
			result.setYieldExpression(this.expressionConverterUtility.convert(yieldSt.getExpression()));
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, yieldSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleOther(Statement statement) {
		org.emftext.language.java.statements.ExpressionStatement result = this.statementsFactory
				.createExpressionStatement();
		result.setExpression(
				this.utilReferenceWalker.walkUp(this.toReferenceConverterFromStatement.convert(statement)));
		return result;
	}

	private org.emftext.language.java.statements.Statement handleWhileStatement(Statement statement) {
		WhileStatement whileSt = (WhileStatement) statement;
		org.emftext.language.java.statements.WhileLoop result = this.statementsFactory.createWhileLoop();
		result.setCondition(this.expressionConverterUtility.convert(whileSt.getExpression()));
		result.setStatement(convert(whileSt.getBody()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, whileSt);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.statements.Statement handleVariableDeclarationStatement(Statement statement) {
		VariableDeclarationStatement varSt = (VariableDeclarationStatement) statement;
		org.emftext.language.java.statements.LocalVariableStatement result = this.statementsFactory
				.createLocalVariableStatement();
		VariableDeclarationFragment frag = (VariableDeclarationFragment) varSt.fragments().get(0);
		org.emftext.language.java.variables.LocalVariable locVar;
		IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			locVar = this.jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			locVar = this.jdtResolverUtility.getLocalVariable(binding);
		}
		this.utilNamedElement.setNameOfElement(frag.getName(), locVar);
		varSt.modifiers().forEach(obj -> locVar.getAnnotationsAndModifiers()
				.add(this.annotationInstanceConverter.convert((IExtendedModifier) obj)));
		locVar.setTypeReference(this.toTypeReferenceConverter.convert(varSt.getType()));
		this.utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(varSt.getType(), locVar);
		frag.extraDimensions().forEach(obj -> this.utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, locVar));
		if (frag.getInitializer() != null) {
			locVar.setInitialValue(this.expressionConverterUtility.convert(frag.getInitializer()));
		}
		for (int index = 1; index < varSt.fragments().size(); index++) {
			locVar.getAdditionalLocalVariables().add(this.toAdditionalLocalVariableConverter
					.convert((VariableDeclarationFragment) varSt.fragments().get(index)));
		}
		result.setVariable(locVar);
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, varSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleTypeDeclarationStatement(Statement statement) {
		TypeDeclarationStatement declSt = (TypeDeclarationStatement) statement;
		return this.classifierConverterUtility.convert(declSt.getDeclaration());
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.statements.Statement handleTryStatement(Statement statement) {
		TryStatement trySt = (TryStatement) statement;
		org.emftext.language.java.statements.TryBlock result = this.statementsFactory.createTryBlock();
		trySt.resources().forEach(obj -> {
			Expression resExpr = (Expression) obj;
			if (resExpr instanceof VariableDeclarationExpression) {
				result.getResources()
						.add(this.toLocalVariableConverter.convert((VariableDeclarationExpression) resExpr));
			} else {
				result.getResources()
						.add((org.emftext.language.java.references.ElementReference) this.utilReferenceWalker
								.walkUp(this.toReferenceConverterFromExpression.convert(resExpr)));
			}
		});
		result.setBlock(this.blockToBlockConverter.convert(trySt.getBody()));
		trySt.catchClauses()
				.forEach(obj -> result.getCatchBlocks().add(this.toCatchblockConverter.convert((CatchClause) obj)));
		if (trySt.getFinally() != null) {
			result.setFinallyBlock(this.blockToBlockConverter.convert(trySt.getFinally()));
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, trySt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleThrowStatement(Statement statement) {
		ThrowStatement throwSt = (ThrowStatement) statement;
		org.emftext.language.java.statements.Throw result = this.statementsFactory.createThrow();
		result.setThrowable(this.expressionConverterUtility.convert(throwSt.getExpression()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, throwSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleSynchonizedStatement(Statement statement) {
		SynchronizedStatement synSt = (SynchronizedStatement) statement;
		org.emftext.language.java.statements.SynchronizedBlock result = this.statementsFactory
				.createSynchronizedBlock();
		result.setLockProvider(this.expressionConverterUtility.convert(synSt.getExpression()));
		result.setBlock(this.blockToBlockConverter.convert(synSt.getBody()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, synSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleSwitchStatement(Statement statement) {
		return this.switchToSwitchConverter.convert((SwitchStatement) statement);
	}

	private org.emftext.language.java.statements.Statement handleReturnStatement(Statement statement) {
		ReturnStatement retSt = (ReturnStatement) statement;
		org.emftext.language.java.statements.Return result = this.statementsFactory.createReturn();
		if (retSt.getExpression() != null) {
			result.setReturnValue(this.expressionConverterUtility.convert(retSt.getExpression()));
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, retSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleLabeledStatement(Statement statement) {
		LabeledStatement labelSt = (LabeledStatement) statement;
		org.emftext.language.java.statements.JumpLabel result = this.statementsFactory.createJumpLabel();
		this.utilNamedElement.setNameOfElement(labelSt.getLabel(), result);
		this.currentJumpLabels.add(result);
		result.setStatement(convert(labelSt.getBody()));
		this.currentJumpLabels.remove(result);
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, labelSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleIfStatement(Statement statement) {
		IfStatement ifSt = (IfStatement) statement;
		org.emftext.language.java.statements.Condition result = this.statementsFactory.createCondition();
		result.setCondition(this.expressionConverterUtility.convert(ifSt.getExpression()));
		result.setStatement(convert(ifSt.getThenStatement()));
		if (ifSt.getElseStatement() != null) {
			result.setElseStatement(convert(ifSt.getElseStatement()));
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, ifSt);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.statements.Statement handleForStatement(Statement statement) {
		ForStatement forSt = (ForStatement) statement;
		org.emftext.language.java.statements.ForLoop result = this.statementsFactory.createForLoop();
		if (forSt.initializers().size() == 1 && forSt.initializers().get(0) instanceof VariableDeclarationExpression) {
			result.setInit(
					this.toLocalVariableConverter.convert((VariableDeclarationExpression) forSt.initializers().get(0)));
		} else {
			org.emftext.language.java.expressions.ExpressionList ini = this.expressionsFactory.createExpressionList();
			forSt.initializers().forEach(
					obj -> ini.getExpressions().add(this.expressionConverterUtility.convert((Expression) obj)));
			result.setInit(ini);
		}
		if (forSt.getExpression() != null) {
			result.setCondition(this.expressionConverterUtility.convert(forSt.getExpression()));
		}
		forSt.updaters()
				.forEach(obj -> result.getUpdates().add(this.expressionConverterUtility.convert((Expression) obj)));
		result.setStatement(convert(forSt.getBody()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleExpressionStatement(Statement statement) {
		ExpressionStatement exprSt = (ExpressionStatement) statement;
		if (exprSt.getExpression().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION) {
			org.emftext.language.java.statements.LocalVariableStatement result = this.statementsFactory
					.createLocalVariableStatement();
			result.setVariable(
					this.toLocalVariableConverter.convert((VariableDeclarationExpression) exprSt.getExpression()));
			this.layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
			return result;
		}
		org.emftext.language.java.statements.ExpressionStatement result = this.statementsFactory
				.createExpressionStatement();
		result.setExpression(this.expressionConverterUtility.convert(exprSt.getExpression()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleEnhancedForStatement(Statement statement) {
		EnhancedForStatement forSt = (EnhancedForStatement) statement;
		org.emftext.language.java.statements.ForEachLoop result = this.statementsFactory.createForEachLoop();
		result.setNext(this.toOrdinaryParameterConverter.convert(forSt.getParameter()));
		result.setCollection(this.expressionConverterUtility.convert(forSt.getExpression()));
		result.setStatement(convert(forSt.getBody()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleEmptyStatement(Statement statement) {
		org.emftext.language.java.statements.EmptyStatement result = this.statementsFactory.createEmptyStatement();
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, statement);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleDoStatement(Statement statement) {
		DoStatement doSt = (DoStatement) statement;
		org.emftext.language.java.statements.DoWhileLoop result = this.statementsFactory.createDoWhileLoop();
		result.setCondition(this.expressionConverterUtility.convert(doSt.getExpression()));
		result.setStatement(convert(doSt.getBody()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, doSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleContinueStatement(Statement statement) {
		ContinueStatement conSt = (ContinueStatement) statement;
		org.emftext.language.java.statements.Continue result = this.statementsFactory.createContinue();
		if (conSt.getLabel() != null) {
			org.emftext.language.java.statements.JumpLabel proxyTarget = this.currentJumpLabels.stream()
					.filter(label -> label.getName().equals(conSt.getLabel().getIdentifier())).findFirst().get();
			result.setTarget(proxyTarget);
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, conSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleBreakStatement(Statement statement) {
		BreakStatement breakSt = (BreakStatement) statement;
		org.emftext.language.java.statements.Break result = this.statementsFactory.createBreak();
		if (breakSt.getLabel() != null) {
			org.emftext.language.java.statements.JumpLabel proxyTarget = this.currentJumpLabels.stream()
					.filter(label -> label.getName().equals(breakSt.getLabel().getIdentifier())).findFirst().get();
			result.setTarget(proxyTarget);
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, breakSt);
		return result;
	}

	private org.emftext.language.java.statements.Statement handleBlock(Statement statement) {
		return this.blockToBlockConverter.convert((Block) statement);
	}

	private org.emftext.language.java.statements.Statement handleAssertStatement(Statement statement) {
		AssertStatement assertSt = (AssertStatement) statement;
		org.emftext.language.java.statements.Assert result = this.statementsFactory.createAssert();
		result.setCondition(this.expressionConverterUtility.convert(assertSt.getExpression()));
		if (assertSt.getMessage() != null) {
			result.setErrorMessage(this.expressionConverterUtility.convert(assertSt.getMessage()));
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, assertSt);
		return result;
	}

	@Inject
	public void setToReferenceConverterFromExpression(
			Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression) {
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	@Inject
	public void setToReferenceConverterFromStatement(
			Converter<Statement, org.emftext.language.java.references.Reference> toReferenceConverterFromStatement) {
		this.toReferenceConverterFromStatement = toReferenceConverterFromStatement;
	}

}
