package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

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
	private final Map<Integer, StatementHandler> mapping;

	@Inject
	public StatementToStatementConverterImpl() {
		mapping = new HashMap<>();
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement convert(final Statement statement) {

		if (mapping.isEmpty()) {
			mapping.put(ASTNode.ASSERT_STATEMENT, assertStatementHandler);
			mapping.put(ASTNode.BLOCK, blockHandler);
			mapping.put(ASTNode.BREAK_STATEMENT, breakStatementHandler);
			mapping.put(ASTNode.CONTINUE_STATEMENT, continueStatementHandler);
			mapping.put(ASTNode.DO_STATEMENT, doStatementHandler);
			mapping.put(ASTNode.EMPTY_STATEMENT, emptyStatementHandler);
			mapping.put(ASTNode.ENHANCED_FOR_STATEMENT, enhancedForStatementHandler);
			mapping.put(ASTNode.EXPRESSION_STATEMENT, expressionStatementHandler);
			mapping.put(ASTNode.FOR_STATEMENT, forStatementHandler);
			mapping.put(ASTNode.IF_STATEMENT, ifStatementHandler);
			mapping.put(ASTNode.LABELED_STATEMENT, labeledStatementHandler);
			mapping.put(ASTNode.RETURN_STATEMENT, returnStatementHandler);
			mapping.put(ASTNode.SWITCH_STATEMENT, switchStatementHandler);
			mapping.put(ASTNode.SYNCHRONIZED_STATEMENT, synchonizedStatementHandler);
			mapping.put(ASTNode.THROW_STATEMENT, throwStatementHandler);
			mapping.put(ASTNode.TRY_STATEMENT, tryStatementHandler);
			mapping.put(ASTNode.TYPE_DECLARATION_STATEMENT, typeDeclarationStatementHandler);
			mapping.put(ASTNode.VARIABLE_DECLARATION_STATEMENT, variableDeclarationStatementHandler);
			mapping.put(ASTNode.WHILE_STATEMENT, whileStatementHandler);
			mapping.put(ASTNode.YIELD_STATEMENT, yieldStatementHandler);
		}

		return mapping.getOrDefault(statement.getNodeType(), otherHandler).handle(statement);
	}

	@Inject
	public void setAssertStatementHandler(
			@Named("AssertStatementHandler") final StatementHandler assertStatementHandler) {
		this.assertStatementHandler = assertStatementHandler;
	}

	@Inject
	public void setBlockHandler(@Named("BlockHandler") final StatementHandler blockHandler) {
		this.blockHandler = blockHandler;
	}

	@Inject
	public void setBreakStatementHandler(@Named("BreakStatementHandler") final StatementHandler breakStatementHandler) {
		this.breakStatementHandler = breakStatementHandler;
	}

	@Inject
	public void setContinueStatementHandler(
			@Named("ContinueStatementHandler") final StatementHandler continueStatementHandler) {
		this.continueStatementHandler = continueStatementHandler;
	}

	@Inject
	public void setDoStatementHandler(@Named("DoStatementHandler") final StatementHandler doStatementHandler) {
		this.doStatementHandler = doStatementHandler;
	}

	@Inject
	public void setEmptyStatementHandler(@Named("EmptyStatementHandler") final StatementHandler emptyStatementHandler) {
		this.emptyStatementHandler = emptyStatementHandler;
	}

	@Inject
	public void setEnhancedForStatementHandler(
			@Named("EnhancedForStatementHandler") final StatementHandler enhancedForStatementHandler) {
		this.enhancedForStatementHandler = enhancedForStatementHandler;
	}

	@Inject
	public void setExpressionStatementHandler(
			@Named("ExpressionStatementHandler") final StatementHandler expressionStatementHandler) {
		this.expressionStatementHandler = expressionStatementHandler;
	}

	@Inject
	public void setForStatementHandler(@Named("ForStatementHandler") final StatementHandler forStatementHandler) {
		this.forStatementHandler = forStatementHandler;
	}

	@Inject
	public void setIfStatementHandler(@Named("IfStatementHandler") final StatementHandler ifStatementHandler) {
		this.ifStatementHandler = ifStatementHandler;
	}

	@Inject
	public void setLabeledStatementHandler(
			@Named("LabeledStatementHandler") final StatementHandler labeledStatementHandler) {
		this.labeledStatementHandler = labeledStatementHandler;
	}

	@Inject
	public void setOtherHandler(@Named("OtherHandler") final StatementHandler otherHandler) {
		this.otherHandler = otherHandler;
	}

	@Inject
	public void setReturnStatementHandler(
			@Named("ReturnStatementHandler") final StatementHandler returnStatementHandler) {
		this.returnStatementHandler = returnStatementHandler;
	}

	@Inject
	public void setSwitchStatementHandler(
			@Named("SwitchStatementHandler") final StatementHandler switchStatementHandler) {
		this.switchStatementHandler = switchStatementHandler;
	}

	@Inject
	public void setSynchonizedStatementHandler(
			@Named("SynchonizedStatementHandler") final StatementHandler synchonizedStatementHandler) {
		this.synchonizedStatementHandler = synchonizedStatementHandler;
	}

	@Inject
	public void setThrowStatementHandler(@Named("ThrowStatementHandler") final StatementHandler throwStatementHandler) {
		this.throwStatementHandler = throwStatementHandler;
	}

	@Inject
	public void setTryStatementHandler(@Named("TryStatementHandler") final StatementHandler tryStatementHandler) {
		this.tryStatementHandler = tryStatementHandler;
	}

	@Inject
	public void setTypeDeclarationStatementHandler(
			@Named("TypeDeclarationStatementHandler") final StatementHandler typeDeclarationStatementHandler) {
		this.typeDeclarationStatementHandler = typeDeclarationStatementHandler;
	}

	@Inject
	public void setVariableDeclarationStatementHandler(
			@Named("VariableDeclarationStatementHandler") final StatementHandler variableDeclarationStatementHandler) {
		this.variableDeclarationStatementHandler = variableDeclarationStatementHandler;
	}

	@Inject
	public void setWhileStatementHandler(@Named("WhileStatementHandler") final StatementHandler whileStatementHandler) {
		this.whileStatementHandler = whileStatementHandler;
	}

	@Inject
	public void setYieldStatementHandler(@Named("YieldStatementHandler") final StatementHandler yieldStatementHandler) {
		this.yieldStatementHandler = yieldStatementHandler;
	}

}
