package jamopp.printer;

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

public class StatementPrinter {

	static void printStatement(Statement element, BufferedWriter writer) throws IOException {
		if (element instanceof ConcreteClassifier) {
			ConcreteClassifierPrinter.printConcreteClassifier((ConcreteClassifier) element, writer);
		} else if (element instanceof Assert) {
			AssertPrinter.printAssert((Assert) element, writer);
		} else if (element instanceof Block) {
			BlockPrinter.printBlock((Block) element, writer);
		} else if (element instanceof Condition) {
			ConditionPrinter.printCondition((Condition) element, writer);
		} else if (element instanceof EmptyStatement) {
			EmptyStatementPrinter.printEmptyStatement(writer);
		} else if (element instanceof ExpressionStatement) {
			ExpressionStatementPrinter.printExpressionStatement((ExpressionStatement) element, writer);
		} else if (element instanceof ForLoop) {
			ForLoopPrinter.printForLoop((ForLoop) element, writer);
		} else if (element instanceof ForEachLoop) {
			ForEachLoopPrinter.printForEachLoop((ForEachLoop) element, writer);
		} else if (element instanceof Break) {
			BreakPrinter.printBreak((Break) element, writer);
		} else if (element instanceof Continue) {
			ContinuePrinter.printContinue((Continue) element, writer);
		} else if (element instanceof JumpLabel) {
			JumpLabelPrinter.printJumpLabel((JumpLabel) element, writer);
		} else if (element instanceof LocalVariableStatement) {
			LocalVariableStatementPrinter.printLocalVariableStatement((LocalVariableStatement) element, writer);
		} else if (element instanceof Return) {
			ReturnPrint.printReturn((Return) element, writer);
		} else if (element instanceof Switch) {
			SwitchPrint.printSwitch((Switch) element, writer);
		} else if (element instanceof SynchronizedBlock) {
			SynchronizedBlockPrinter.printSynchronizedBlock((SynchronizedBlock) element, writer);
		} else if (element instanceof Throw) {
			ThrowPrinter.printThrow((Throw) element, writer);
		} else if (element instanceof TryBlock) {
			TryBlockPrinter.printTryBlock((TryBlock) element, writer);
		} else if (element instanceof DoWhileLoop) {
			DoWhileLoopPrinter.printDoWhileLoop((DoWhileLoop) element, writer);
		} else if (element instanceof WhileLoop) {
			WhileLoopPrinter.printWhileLoop((WhileLoop) element, writer);
		} else {
			YieldStatementPrinter.printYieldStatement((YieldStatement) element, writer);
		}
	}

}
