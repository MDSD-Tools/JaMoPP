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

import jamopp.printer.interfaces.Printer;

class StatementPrinter implements Printer<Statement> {

	private final ConcreteClassifierPrinter ConcreteClassifierPrinter;
	private final AssertPrinter AssertPrinter;
	private final BlockPrinter BlockPrinter;
	private final ConditionPrinter ConditionPrinter;
	private final EmptyStatementPrinter EmptyStatementPrinter;
	private final ExpressionStatementPrinter ExpressionStatementPrinter;
	private final ForLoopPrinter ForLoopPrinter;
	private final ForEachLoopPrinter ForEachLoopPrinter;
	private final BreakPrinter BreakPrinter;
	private final ContinuePrinter ContinuePrinter;
	private final JumpLabelPrinter JumpLabelPrinter;
	private final LocalVariableStatementPrinter LocalVariableStatementPrinter;
	private final ReturnPrinter ReturnPrinter;
	private final SwitchPrinter SwitchPrinter;
	private final SynchronizedBlockPrinter SynchronizedBlockPrinter;
	private final ThrowPrinter ThrowPrinter;
	private final TryBlockPrinter TryBlockPrinter;
	private final DoWhileLoopPrinter DoWhileLoopPrinter;
	private final WhileLoopPrinter WhileLoopPrinter;
	private final YieldStatementPrinter YieldStatementPrinter;

	@Inject
	public StatementPrinter(jamopp.printer.implementation.ConcreteClassifierPrinter concreteClassifierPrinter,
			jamopp.printer.implementation.AssertPrinter assertPrinter,
			jamopp.printer.implementation.BlockPrinter blockPrinter,
			jamopp.printer.implementation.ConditionPrinter conditionPrinter,
			jamopp.printer.implementation.EmptyStatementPrinter emptyStatementPrinter,
			jamopp.printer.implementation.ExpressionStatementPrinter expressionStatementPrinter,
			jamopp.printer.implementation.ForLoopPrinter forLoopPrinter,
			jamopp.printer.implementation.ForEachLoopPrinter forEachLoopPrinter,
			jamopp.printer.implementation.BreakPrinter breakPrinter,
			jamopp.printer.implementation.ContinuePrinter continuePrinter,
			jamopp.printer.implementation.JumpLabelPrinter jumpLabelPrinter,
			jamopp.printer.implementation.LocalVariableStatementPrinter localVariableStatementPrinter,
			jamopp.printer.implementation.ReturnPrinter returnPrinter,
			jamopp.printer.implementation.SwitchPrinter switchPrinter,
			jamopp.printer.implementation.SynchronizedBlockPrinter synchronizedBlockPrinter,
			jamopp.printer.implementation.ThrowPrinter throwPrinter,
			jamopp.printer.implementation.TryBlockPrinter tryBlockPrinter,
			jamopp.printer.implementation.DoWhileLoopPrinter doWhileLoopPrinter,
			jamopp.printer.implementation.WhileLoopPrinter whileLoopPrinter,
			jamopp.printer.implementation.YieldStatementPrinter yieldStatementPrinter) {
		super();
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

	public void print(Statement element, BufferedWriter writer) throws IOException {
		if (element instanceof ConcreteClassifier) {
			ConcreteClassifierPrinter.print((ConcreteClassifier) element, writer);
		} else if (element instanceof Assert) {
			AssertPrinter.print((Assert) element, writer);
		} else if (element instanceof Block) {
			BlockPrinter.print((Block) element, writer);
		} else if (element instanceof Condition) {
			ConditionPrinter.print((Condition) element, writer);
		} else if (element instanceof EmptyStatement) {
			EmptyStatementPrinter.print(writer);
		} else if (element instanceof ExpressionStatement) {
			ExpressionStatementPrinter.print((ExpressionStatement) element, writer);
		} else if (element instanceof ForLoop) {
			ForLoopPrinter.print((ForLoop) element, writer);
		} else if (element instanceof ForEachLoop) {
			ForEachLoopPrinter.print((ForEachLoop) element, writer);
		} else if (element instanceof Break) {
			BreakPrinter.print((Break) element, writer);
		} else if (element instanceof Continue) {
			ContinuePrinter.print((Continue) element, writer);
		} else if (element instanceof JumpLabel) {
			JumpLabelPrinter.print((JumpLabel) element, writer);
		} else if (element instanceof LocalVariableStatement) {
			LocalVariableStatementPrinter.print((LocalVariableStatement) element, writer);
		} else if (element instanceof Return) {
			ReturnPrinter.print((Return) element, writer);
		} else if (element instanceof Switch) {
			SwitchPrinter.print((Switch) element, writer);
		} else if (element instanceof SynchronizedBlock) {
			SynchronizedBlockPrinter.print((SynchronizedBlock) element, writer);
		} else if (element instanceof Throw) {
			ThrowPrinter.print((Throw) element, writer);
		} else if (element instanceof TryBlock) {
			TryBlockPrinter.print((TryBlock) element, writer);
		} else if (element instanceof DoWhileLoop) {
			DoWhileLoopPrinter.print((DoWhileLoop) element, writer);
		} else if (element instanceof WhileLoop) {
			WhileLoopPrinter.print((WhileLoop) element, writer);
		} else {
			YieldStatementPrinter.print((YieldStatement) element, writer);
		}
	}

}
