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


import jamopp.printer.interfaces.printer.AssertPrinterInt;
import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.BreakPrinterInt;
import jamopp.printer.interfaces.printer.ConcreteClassifierPrinterInt;
import jamopp.printer.interfaces.printer.ConditionPrinterInt;
import jamopp.printer.interfaces.printer.ContinuePrinterInt;
import jamopp.printer.interfaces.printer.DoWhileLoopPrinterInt;
import jamopp.printer.interfaces.printer.EmptyStatementPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionStatementPrinterInt;
import jamopp.printer.interfaces.printer.ForEachLoopPrinterInt;
import jamopp.printer.interfaces.printer.ForLoopPrinterInt;
import jamopp.printer.interfaces.printer.JumpLabelPrinterInt;
import jamopp.printer.interfaces.printer.LocalVariableStatementPrinterInt;
import jamopp.printer.interfaces.printer.ReturnPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;
import jamopp.printer.interfaces.printer.SwitchPrinterInt;
import jamopp.printer.interfaces.printer.SynchronizedBlockPrinterInt;
import jamopp.printer.interfaces.printer.ThrowPrinterInt;
import jamopp.printer.interfaces.printer.TryBlockPrinterInt;
import jamopp.printer.interfaces.printer.WhileLoopPrinterInt;
import jamopp.printer.interfaces.printer.YieldStatementPrinterInt;

public class StatementPrinterImpl implements StatementPrinterInt {

	private final ConcreteClassifierPrinterInt ConcreteClassifierPrinter;
	private final AssertPrinterInt AssertPrinter;
	private final BlockPrinterInt BlockPrinter;
	private final ConditionPrinterInt ConditionPrinter;
	private final EmptyStatementPrinterInt EmptyStatementPrinter;
	private final ExpressionStatementPrinterInt ExpressionStatementPrinter;
	private final ForLoopPrinterInt ForLoopPrinter;
	private final ForEachLoopPrinterInt ForEachLoopPrinter;
	private final BreakPrinterInt BreakPrinter;
	private final ContinuePrinterInt ContinuePrinter;
	private final JumpLabelPrinterInt JumpLabelPrinter;
	private final LocalVariableStatementPrinterInt LocalVariableStatementPrinter;
	private final ReturnPrinterInt ReturnPrinter;
	private final SwitchPrinterInt SwitchPrinter;
	private final SynchronizedBlockPrinterInt SynchronizedBlockPrinter;
	private final ThrowPrinterInt ThrowPrinter;
	private final TryBlockPrinterInt TryBlockPrinter;
	private final DoWhileLoopPrinterInt DoWhileLoopPrinter;
	private final WhileLoopPrinterInt WhileLoopPrinter;
	private final YieldStatementPrinterInt YieldStatementPrinter;

	@Inject
	public StatementPrinterImpl(ConcreteClassifierPrinterInt concreteClassifierPrinter, AssertPrinterInt assertPrinter,
			BlockPrinterInt blockPrinter, ConditionPrinterInt conditionPrinter,
			EmptyStatementPrinterInt emptyStatementPrinter, ExpressionStatementPrinterInt expressionStatementPrinter,
			ForLoopPrinterInt forLoopPrinter, ForEachLoopPrinterInt forEachLoopPrinter, BreakPrinterInt breakPrinter,
			ContinuePrinterInt continuePrinter, JumpLabelPrinterInt jumpLabelPrinter,
			LocalVariableStatementPrinterInt localVariableStatementPrinter, ReturnPrinterInt returnPrinter,
			SwitchPrinterInt switchPrinter, SynchronizedBlockPrinterInt synchronizedBlockPrinter,
			ThrowPrinterInt throwPrinter, TryBlockPrinterInt tryBlockPrinter, DoWhileLoopPrinterInt doWhileLoopPrinter,
			WhileLoopPrinterInt whileLoopPrinter, YieldStatementPrinterInt yieldStatementPrinter) {
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
