package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;

public class StatementToStatementConverterImpl
		implements Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> {

	private StatementHandler assertStatementHandler;
	private StatementHandler blockHandler;
	private StatementHandler breakStatementHandler;
	private StatementHandler continueStatementHandler;
	private StatementHandler doStatementHandler;
	private StatementHandler emptyStatementHandler;
	private StatementHandler enhancedForStatementHandler;
	private StatementHandler expressionStatementHandler;
	private StatementHandler forStatementHandler;
	private StatementHandler ifStatementHandler;
	private StatementHandler labeledStatementHandler;
	private StatementHandler otherHandler;
	private StatementHandler returnStatementHandler;
	private StatementHandler switchStatementHandler;
	private StatementHandler synchonizedStatementHandler;
	private StatementHandler throwStatementHandler;
	private StatementHandler tryStatementHandler;
	private StatementHandler typeDeclarationStatementHandler;
	private StatementHandler variableDeclarationStatementHandler;
	private StatementHandler whileStatementHandler;
	private StatementHandler yieldStatementHandler;

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement convert(Statement statement) {
		if (statement.getNodeType() == ASTNode.ASSERT_STATEMENT) {
			return assertStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.BLOCK) {
			return blockHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.BREAK_STATEMENT) {
			return breakStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.CONTINUE_STATEMENT) {
			return continueStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.DO_STATEMENT) {
			return doStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.EMPTY_STATEMENT) {
			return emptyStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
			return enhancedForStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {
			return expressionStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.FOR_STATEMENT) {
			return forStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.IF_STATEMENT) {
			return ifStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.LABELED_STATEMENT) {
			return labeledStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.RETURN_STATEMENT) {
			return returnStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.SWITCH_STATEMENT) {
			return switchStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.SYNCHRONIZED_STATEMENT) {
			return synchonizedStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.THROW_STATEMENT) {
			return throwStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.TRY_STATEMENT) {
			return tryStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT) {
			return typeDeclarationStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
			return variableDeclarationStatementHandler.handle(statement);
		} else if (statement.getNodeType() == ASTNode.WHILE_STATEMENT) {
			return whileStatementHandler.handle(statement);
		} else if (statement.getNodeType() != ASTNode.YIELD_STATEMENT) {
			return otherHandler.handle(statement);
		} else {
			return yieldStatementHandler.handle(statement);
		}
	}

	@Inject
	public void setAssertStatementHandler(@Named("AssertStatementHandler") StatementHandler assertStatementHandler) {
		this.assertStatementHandler = assertStatementHandler;
	}

	@Inject
	public void setBlockHandler(@Named("BlockHandler") StatementHandler blockHandler) {
		this.blockHandler = blockHandler;
	}

	@Inject
	public void setBreakStatementHandler(@Named("BreakStatementHandler") StatementHandler breakStatementHandler) {
		this.breakStatementHandler = breakStatementHandler;
	}

	@Inject
	public void setContinueStatementHandler(
			@Named("ContinueStatementHandler") StatementHandler continueStatementHandler) {
		this.continueStatementHandler = continueStatementHandler;
	}

	@Inject
	public void setDoStatementHandler(@Named("DoStatementHandler") StatementHandler doStatementHandler) {
		this.doStatementHandler = doStatementHandler;
	}

	@Inject
	public void setEmptyStatementHandler(@Named("EmptyStatementHandler") StatementHandler emptyStatementHandler) {
		this.emptyStatementHandler = emptyStatementHandler;
	}

	@Inject
	public void setEnhancedForStatementHandler(
			@Named("EnhancedForStatementHandler") StatementHandler enhancedForStatementHandler) {
		this.enhancedForStatementHandler = enhancedForStatementHandler;
	}

	@Inject
	public void setExpressionStatementHandler(
			@Named("ExpressionStatementHandler") StatementHandler expressionStatementHandler) {
		this.expressionStatementHandler = expressionStatementHandler;
	}

	@Inject
	public void setForStatementHandler(@Named("ForStatementHandler") StatementHandler forStatementHandler) {
		this.forStatementHandler = forStatementHandler;
	}

	@Inject
	public void setIfStatementHandler(@Named("IfStatementHandler") StatementHandler ifStatementHandler) {
		this.ifStatementHandler = ifStatementHandler;
	}

	@Inject
	public void setLabeledStatementHandler(@Named("LabeledStatementHandler") StatementHandler labeledStatementHandler) {
		this.labeledStatementHandler = labeledStatementHandler;
	}

	@Inject
	public void setOtherHandler(@Named("OtherHandler") StatementHandler otherHandler) {
		this.otherHandler = otherHandler;
	}

	@Inject
	public void setReturnStatementHandler(@Named("ReturnStatementHandler") StatementHandler returnStatementHandler) {
		this.returnStatementHandler = returnStatementHandler;
	}

	@Inject
	public void setSwitchStatementHandler(@Named("SwitchStatementHandler") StatementHandler switchStatementHandler) {
		this.switchStatementHandler = switchStatementHandler;
	}

	@Inject
	public void setSynchonizedStatementHandler(
			@Named("SynchonizedStatementHandler") StatementHandler synchonizedStatementHandler) {
		this.synchonizedStatementHandler = synchonizedStatementHandler;
	}

	@Inject
	public void setThrowStatementHandler(@Named("ThrowStatementHandler") StatementHandler throwStatementHandler) {
		this.throwStatementHandler = throwStatementHandler;
	}

	@Inject
	public void setTryStatementHandler(@Named("TryStatementHandler") StatementHandler tryStatementHandler) {
		this.tryStatementHandler = tryStatementHandler;
	}

	@Inject
	public void setTypeDeclarationStatementHandler(
			@Named("TypeDeclarationStatementHandler") StatementHandler typeDeclarationStatementHandler) {
		this.typeDeclarationStatementHandler = typeDeclarationStatementHandler;
	}

	@Inject
	public void setVariableDeclarationStatementHandler(
			@Named("VariableDeclarationStatementHandler") StatementHandler variableDeclarationStatementHandler) {
		this.variableDeclarationStatementHandler = variableDeclarationStatementHandler;
	}

	@Inject
	public void setWhileStatementHandler(@Named("WhileStatementHandler") StatementHandler whileStatementHandler) {
		this.whileStatementHandler = whileStatementHandler;
	}

	@Inject
	public void setYieldStatementHandler(@Named("YieldStatementHandler") StatementHandler yieldStatementHandler) {
		this.yieldStatementHandler = yieldStatementHandler;
	}

}
