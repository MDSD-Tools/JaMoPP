package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class StatementToStatementConverterImpl
		implements Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> {

	private AssertStatementHandler assertStatementHandler;
	private BlockHandler blockHandler;
	private BreakStatementHandler breakStatementHandler;
	private ContinueStatementHandler continueStatementHandler;
	private DoStatementHandler doStatementHandler;
	private EmptyStatementHandler emptyStatementHandler;
	private EnhancedForStatementHandler enhancedForStatementHandler;
	private ExpressionStatementHandler expressionStatementHandler;
	private ForStatementHandler forStatementHandler;
	private IfStatementHandler ifStatementHandler;
	private LabeledStatementHandler labeledStatementHandler;
	private OtherHandler otherHandler;
	private ReturnStatementHandler returnStatementHandler;
	private SwitchStatementHandler switchStatementHandler;
	private SynchonizedStatementHandler synchonizedStatementHandler;
	private ThrowStatementHandler throwStatementHandler;
	private TryStatementHandler tryStatementHandler;
	private TypeDeclarationStatementHandler typeDeclarationStatementHandler;
	private VariableDeclarationStatementHandler variableDeclarationStatementHandler;
	private WhileStatementHandler whileStatementHandler;
	private YieldStatementHandler yieldStatementHandler;

	// Circular Dep. exists. Constructor can't be used.
	@Inject
	public void setFields(YieldStatementHandler yieldStatementHandler, WhileStatementHandler whileStatementHandler,
			VariableDeclarationStatementHandler variableDeclarationStatementHandler,
			TypeDeclarationStatementHandler typeDeclarationStatementHandler, TryStatementHandler tryStatementHandler,
			ThrowStatementHandler throwStatementHandler, SynchonizedStatementHandler synchonizedStatementHandler,
			SwitchStatementHandler switchStatementHandler, ReturnStatementHandler returnStatementHandler,
			OtherHandler otherHandler, LabeledStatementHandler labeledStatementHandler,
			IfStatementHandler ifStatementHandler, ForStatementHandler forStatementHandler,
			ExpressionStatementHandler expressionStatementHandler,
			EnhancedForStatementHandler enhancedForStatementHandler, EmptyStatementHandler emptyStatementHandler,
			DoStatementHandler doStatementHandler, ContinueStatementHandler continueStatementHandler,
			BreakStatementHandler breakStatementHandler, BlockHandler blockHandler,
			AssertStatementHandler assertStatementHandler) {
		this.assertStatementHandler = assertStatementHandler;
		this.blockHandler = blockHandler;
		this.breakStatementHandler = breakStatementHandler;
		this.continueStatementHandler = continueStatementHandler;
		this.doStatementHandler = doStatementHandler;
		this.emptyStatementHandler = emptyStatementHandler;
		this.enhancedForStatementHandler = enhancedForStatementHandler;
		this.expressionStatementHandler = expressionStatementHandler;
		this.forStatementHandler = forStatementHandler;
		this.ifStatementHandler = ifStatementHandler;
		this.labeledStatementHandler = labeledStatementHandler;
		this.otherHandler = otherHandler;
		this.returnStatementHandler = returnStatementHandler;
		this.switchStatementHandler = switchStatementHandler;
		this.synchonizedStatementHandler = synchonizedStatementHandler;
		this.throwStatementHandler = throwStatementHandler;
		this.tryStatementHandler = tryStatementHandler;
		this.typeDeclarationStatementHandler = typeDeclarationStatementHandler;
		this.variableDeclarationStatementHandler = variableDeclarationStatementHandler;
		this.whileStatementHandler = whileStatementHandler;
		this.yieldStatementHandler = yieldStatementHandler;
	}

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

}
