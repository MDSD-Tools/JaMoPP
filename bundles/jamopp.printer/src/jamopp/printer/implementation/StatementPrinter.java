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

import jamopp.printer.interfaces.Printer;

class StatementPrinter implements Printer<Statement>{

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
			SwitchPrint.print((Switch) element, writer);
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
