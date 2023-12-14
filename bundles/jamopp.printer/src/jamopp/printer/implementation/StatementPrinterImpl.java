package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.statements.Assert;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Break;
import org.emftext.language.java.statements.Condition;
import org.emftext.language.java.statements.Continue;
import org.emftext.language.java.statements.DoWhileLoop;
import org.emftext.language.java.statements.EmptyStatement;
import org.emftext.language.java.statements.ExpressionStatement;
import org.emftext.language.java.statements.ForEachLoop;
import org.emftext.language.java.statements.ForLoop;
import org.emftext.language.java.statements.JumpLabel;
import org.emftext.language.java.statements.LocalVariableStatement;
import org.emftext.language.java.statements.Return;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SynchronizedBlock;
import org.emftext.language.java.statements.Throw;
import org.emftext.language.java.statements.TryBlock;
import org.emftext.language.java.statements.WhileLoop;
import org.emftext.language.java.statements.YieldStatement;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.EmptyStatementPrinterInt;
import jamopp.printer.interfaces.Printer;

public class StatementPrinterImpl implements Printer<Statement> {

	private final Provider<Printer<Assert>> assertPrinter;
	private final Provider<Printer<Block>> blockPrinter;
	private final Provider<Printer<Break>> breakPrinter;
	private final Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter;
	private final Provider<Printer<Condition>> conditionPrinter;
	private final Provider<Printer<Continue>> continuePrinter;
	private final Provider<Printer<DoWhileLoop>> doWhileLoopPrinter;
	private final Provider<EmptyStatementPrinterInt> emptyStatementPrinter;
	private final Provider<Printer<ExpressionStatement>> expressionStatementPrinter;
	private final Provider<Printer<ForEachLoop>> forEachLoopPrinter;
	private final Provider<Printer<ForLoop>> forLoopPrinter;
	private final Provider<Printer<JumpLabel>> jumpLabelPrinter;
	private final Provider<Printer<LocalVariableStatement>> localVariableStatementPrinter;
	private final Provider<Printer<Return>> returnPrinter;
	private final Provider<Printer<Switch>> switchPrinter;
	private final Provider<Printer<SynchronizedBlock>> synchronizedBlockPrinter;
	private final Provider<Printer<Throw>> throwPrinter;
	private final Provider<Printer<TryBlock>> tryBlockPrinter;
	private final Provider<Printer<WhileLoop>> whileLoopPrinter;
	private final Provider<Printer<YieldStatement>> yieldStatementPrinter;

	@Inject
	public StatementPrinterImpl(Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter,
			Provider<Printer<Assert>> assertPrinter, Provider<Printer<Block>> blockPrinter,
			Provider<Printer<Condition>> conditionPrinter, Provider<EmptyStatementPrinterInt> emptyStatementPrinter,
			Provider<Printer<ExpressionStatement>> expressionStatementPrinter,
			Provider<Printer<ForLoop>> forLoopPrinter, Provider<Printer<ForEachLoop>> forEachLoopPrinter,
			Provider<Printer<Break>> breakPrinter, Provider<Printer<Continue>> continuePrinter,
			Provider<Printer<JumpLabel>> jumpLabelPrinter,
			Provider<Printer<LocalVariableStatement>> localVariableStatementPrinter,
			Provider<Printer<Return>> returnPrinter, Provider<Printer<Switch>> switchPrinter,
			Provider<Printer<SynchronizedBlock>> synchronizedBlockPrinter, Provider<Printer<Throw>> throwPrinter,
			Provider<Printer<TryBlock>> tryBlockPrinter, Provider<Printer<DoWhileLoop>> doWhileLoopPrinter,
			Provider<Printer<WhileLoop>> whileLoopPrinter, Provider<Printer<YieldStatement>> yieldStatementPrinter) {
		this.concreteClassifierPrinter = concreteClassifierPrinter;
		this.assertPrinter = assertPrinter;
		this.blockPrinter = blockPrinter;
		this.conditionPrinter = conditionPrinter;
		this.emptyStatementPrinter = emptyStatementPrinter;
		this.expressionStatementPrinter = expressionStatementPrinter;
		this.forLoopPrinter = forLoopPrinter;
		this.forEachLoopPrinter = forEachLoopPrinter;
		this.breakPrinter = breakPrinter;
		this.continuePrinter = continuePrinter;
		this.jumpLabelPrinter = jumpLabelPrinter;
		this.localVariableStatementPrinter = localVariableStatementPrinter;
		this.returnPrinter = returnPrinter;
		this.switchPrinter = switchPrinter;
		this.synchronizedBlockPrinter = synchronizedBlockPrinter;
		this.throwPrinter = throwPrinter;
		this.tryBlockPrinter = tryBlockPrinter;
		this.doWhileLoopPrinter = doWhileLoopPrinter;
		this.whileLoopPrinter = whileLoopPrinter;
		this.yieldStatementPrinter = yieldStatementPrinter;
	}

	@Override
	public void print(Statement element, BufferedWriter writer) throws IOException {
		if (element instanceof ConcreteClassifier) {
			this.concreteClassifierPrinter.get().print((ConcreteClassifier) element, writer);
		} else if (element instanceof Assert) {
			this.assertPrinter.get().print((Assert) element, writer);
		} else if (element instanceof Block) {
			this.blockPrinter.get().print((Block) element, writer);
		} else if (element instanceof Condition) {
			this.conditionPrinter.get().print((Condition) element, writer);
		} else if (element instanceof EmptyStatement) {
			this.emptyStatementPrinter.get().print(writer);
		} else if (element instanceof ExpressionStatement) {
			this.expressionStatementPrinter.get().print((ExpressionStatement) element, writer);
		} else if (element instanceof ForLoop) {
			this.forLoopPrinter.get().print((ForLoop) element, writer);
		} else if (element instanceof ForEachLoop) {
			this.forEachLoopPrinter.get().print((ForEachLoop) element, writer);
		} else if (element instanceof Break) {
			this.breakPrinter.get().print((Break) element, writer);
		} else if (element instanceof Continue) {
			this.continuePrinter.get().print((Continue) element, writer);
		} else if (element instanceof JumpLabel) {
			this.jumpLabelPrinter.get().print((JumpLabel) element, writer);
		} else if (element instanceof LocalVariableStatement) {
			this.localVariableStatementPrinter.get().print((LocalVariableStatement) element, writer);
		} else if (element instanceof Return) {
			this.returnPrinter.get().print((Return) element, writer);
		} else if (element instanceof Switch) {
			this.switchPrinter.get().print((Switch) element, writer);
		} else if (element instanceof SynchronizedBlock) {
			this.synchronizedBlockPrinter.get().print((SynchronizedBlock) element, writer);
		} else if (element instanceof Throw) {
			this.throwPrinter.get().print((Throw) element, writer);
		} else if (element instanceof TryBlock) {
			this.tryBlockPrinter.get().print((TryBlock) element, writer);
		} else if (element instanceof DoWhileLoop) {
			this.doWhileLoopPrinter.get().print((DoWhileLoop) element, writer);
		} else if (element instanceof WhileLoop) {
			this.whileLoopPrinter.get().print((WhileLoop) element, writer);
		} else {
			this.yieldStatementPrinter.get().print((YieldStatement) element, writer);
		}
	}

}
