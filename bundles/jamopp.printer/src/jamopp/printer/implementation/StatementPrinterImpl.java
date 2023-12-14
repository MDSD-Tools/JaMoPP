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

	private final Provider<Printer<Assert>> AssertPrinter;
	private final Provider<Printer<Block>> BlockPrinter;
	private final Provider<Printer<Break>> BreakPrinter;
	private final Provider<Printer<ConcreteClassifier>> ConcreteClassifierPrinter;
	private final Provider<Printer<Condition>> ConditionPrinter;
	private final Provider<Printer<Continue>> ContinuePrinter;
	private final Provider<Printer<DoWhileLoop>> DoWhileLoopPrinter;
	private final Provider<EmptyStatementPrinterInt> EmptyStatementPrinter;
	private final Provider<Printer<ExpressionStatement>> ExpressionStatementPrinter;
	private final Provider<Printer<ForEachLoop>> ForEachLoopPrinter;
	private final Provider<Printer<ForLoop>> ForLoopPrinter;
	private final Provider<Printer<JumpLabel>> JumpLabelPrinter;
	private final Provider<Printer<LocalVariableStatement>> LocalVariableStatementPrinter;
	private final Provider<Printer<Return>> ReturnPrinter;
	private final Provider<Printer<Switch>> SwitchPrinter;
	private final Provider<Printer<SynchronizedBlock>> SynchronizedBlockPrinter;
	private final Provider<Printer<Throw>> ThrowPrinter;
	private final Provider<Printer<TryBlock>> TryBlockPrinter;
	private final Provider<Printer<WhileLoop>> WhileLoopPrinter;
	private final Provider<Printer<YieldStatement>> YieldStatementPrinter;

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
		ConcreteClassifierPrinter = concreteClassifierPrinter;
		AssertPrinter = assertPrinter;
		BlockPrinter = blockPrinter;
		ConditionPrinter = conditionPrinter;
		EmptyStatementPrinter = emptyStatementPrinter;
		ExpressionStatementPrinter = expressionStatementPrinter;
		ForLoopPrinter = forLoopPrinter;
		ForEachLoopPrinter = forEachLoopPrinter;
		BreakPrinter = breakPrinter;
		ContinuePrinter = continuePrinter;
		JumpLabelPrinter = jumpLabelPrinter;
		LocalVariableStatementPrinter = localVariableStatementPrinter;
		ReturnPrinter = returnPrinter;
		SwitchPrinter = switchPrinter;
		SynchronizedBlockPrinter = synchronizedBlockPrinter;
		ThrowPrinter = throwPrinter;
		TryBlockPrinter = tryBlockPrinter;
		DoWhileLoopPrinter = doWhileLoopPrinter;
		WhileLoopPrinter = whileLoopPrinter;
		YieldStatementPrinter = yieldStatementPrinter;
	}

	@Override
	public void print(Statement element, BufferedWriter writer) throws IOException {
		if (element instanceof ConcreteClassifier) {
			ConcreteClassifierPrinter.get().print((ConcreteClassifier) element, writer);
		} else if (element instanceof Assert) {
			AssertPrinter.get().print((Assert) element, writer);
		} else if (element instanceof Block) {
			BlockPrinter.get().print((Block) element, writer);
		} else if (element instanceof Condition) {
			ConditionPrinter.get().print((Condition) element, writer);
		} else if (element instanceof EmptyStatement) {
			EmptyStatementPrinter.get().print(writer);
		} else if (element instanceof ExpressionStatement) {
			ExpressionStatementPrinter.get().print((ExpressionStatement) element, writer);
		} else if (element instanceof ForLoop) {
			ForLoopPrinter.get().print((ForLoop) element, writer);
		} else if (element instanceof ForEachLoop) {
			ForEachLoopPrinter.get().print((ForEachLoop) element, writer);
		} else if (element instanceof Break) {
			BreakPrinter.get().print((Break) element, writer);
		} else if (element instanceof Continue) {
			ContinuePrinter.get().print((Continue) element, writer);
		} else if (element instanceof JumpLabel) {
			JumpLabelPrinter.get().print((JumpLabel) element, writer);
		} else if (element instanceof LocalVariableStatement) {
			LocalVariableStatementPrinter.get().print((LocalVariableStatement) element, writer);
		} else if (element instanceof Return) {
			ReturnPrinter.get().print((Return) element, writer);
		} else if (element instanceof Switch) {
			SwitchPrinter.get().print((Switch) element, writer);
		} else if (element instanceof SynchronizedBlock) {
			SynchronizedBlockPrinter.get().print((SynchronizedBlock) element, writer);
		} else if (element instanceof Throw) {
			ThrowPrinter.get().print((Throw) element, writer);
		} else if (element instanceof TryBlock) {
			TryBlockPrinter.get().print((TryBlock) element, writer);
		} else if (element instanceof DoWhileLoop) {
			DoWhileLoopPrinter.get().print((DoWhileLoop) element, writer);
		} else if (element instanceof WhileLoop) {
			WhileLoopPrinter.get().print((WhileLoop) element, writer);
		} else {
			YieldStatementPrinter.get().print((YieldStatement) element, writer);
		}
	}

}
