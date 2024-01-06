package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.statements.Assert;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.Break;
import tools.mdsd.jamopp.model.java.statements.Condition;
import tools.mdsd.jamopp.model.java.statements.Continue;
import tools.mdsd.jamopp.model.java.statements.DoWhileLoop;
import tools.mdsd.jamopp.model.java.statements.EmptyStatement;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.model.java.statements.ForEachLoop;
import tools.mdsd.jamopp.model.java.statements.ForLoop;
import tools.mdsd.jamopp.model.java.statements.JumpLabel;
import tools.mdsd.jamopp.model.java.statements.LocalVariableStatement;
import tools.mdsd.jamopp.model.java.statements.Return;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.model.java.statements.SynchronizedBlock;
import tools.mdsd.jamopp.model.java.statements.Throw;
import tools.mdsd.jamopp.model.java.statements.TryBlock;
import tools.mdsd.jamopp.model.java.statements.WhileLoop;
import tools.mdsd.jamopp.model.java.statements.YieldStatement;
import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class StatementPrinterImpl implements Printer<Statement> {

	private final Provider<Printer<Assert>> assertPrinter;
	private final Provider<Printer<Block>> blockPrinter;
	private final Provider<Printer<Break>> breakPrinter;
	private final Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter;
	private final Provider<Printer<Condition>> conditionPrinter;
	private final Provider<Printer<Continue>> continuePrinter;
	private final Provider<Printer<DoWhileLoop>> doWhileLoopPrinter;
	private final Provider<EmptyPrinter> emptyStatementPrinter;
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
	private final List<Mapping<? extends Statement>> mappings;

	@Inject
	public StatementPrinterImpl(Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter,
			Provider<Printer<Assert>> assertPrinter, Provider<Printer<Block>> blockPrinter,
			Provider<Printer<Condition>> conditionPrinter,
			@Named("EmptyStatementPrinter") Provider<EmptyPrinter> emptyStatementPrinter,
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
		mappings = new ArrayList<>();
	}

	@Override
	public void print(Statement element, BufferedWriter writer) throws IOException {

		if (mappings.isEmpty()) {
			mappings.add(new Mapping<>(ConcreteClassifier.class, concreteClassifierPrinter));
			mappings.add(new Mapping<>(Assert.class, assertPrinter));
			mappings.add(new Mapping<>(Block.class, blockPrinter));
			mappings.add(new Mapping<>(Condition.class, conditionPrinter));
			mappings.add(new Mapping<>(ExpressionStatement.class, expressionStatementPrinter));
			mappings.add(new Mapping<>(ForLoop.class, forLoopPrinter));
			mappings.add(new Mapping<>(ForEachLoop.class, forEachLoopPrinter));
			mappings.add(new Mapping<>(Break.class, breakPrinter));
			mappings.add(new Mapping<>(Continue.class, continuePrinter));
			mappings.add(new Mapping<>(JumpLabel.class, jumpLabelPrinter));
			mappings.add(new Mapping<>(LocalVariableStatement.class, localVariableStatementPrinter));
			mappings.add(new Mapping<>(Return.class, returnPrinter));
			mappings.add(new Mapping<>(Switch.class, switchPrinter));
			mappings.add(new Mapping<>(SynchronizedBlock.class, synchronizedBlockPrinter));
			mappings.add(new Mapping<>(Throw.class, throwPrinter));
			mappings.add(new Mapping<>(TryBlock.class, tryBlockPrinter));
			mappings.add(new Mapping<>(DoWhileLoop.class, doWhileLoopPrinter));
			mappings.add(new Mapping<>(WhileLoop.class, whileLoopPrinter));
			mappings.add(new Mapping<>(YieldStatement.class, yieldStatementPrinter));
		}

		for (Mapping<? extends Statement> mapping : mappings) {
			boolean printed = mapping.checkAndPrint(element, writer);
			if (printed) {
				return;
			}
		}

		if (element instanceof EmptyStatement) {
			emptyStatementPrinter.get().print(writer);
		}

	}

}
