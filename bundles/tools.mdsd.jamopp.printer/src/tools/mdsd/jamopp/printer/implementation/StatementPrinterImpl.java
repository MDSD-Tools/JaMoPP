package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import com.google.inject.Inject;
import com.google.inject.name.Named;

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
	public StatementPrinterImpl(final Provider<Printer<ConcreteClassifier>> concreteClassifierPrinter,
			final Provider<Printer<Assert>> assertPrinter, final Provider<Printer<Block>> blockPrinter,
			final Provider<Printer<Condition>> conditionPrinter,
			@Named("EmptyStatementPrinter") final Provider<EmptyPrinter> emptyStatementPrinter,
			final Provider<Printer<ExpressionStatement>> expressionStatementPrinter,
			final Provider<Printer<ForLoop>> forLoopPrinter, final Provider<Printer<ForEachLoop>> forEachLoopPrinter,
			final Provider<Printer<Break>> breakPrinter, final Provider<Printer<Continue>> continuePrinter,
			final Provider<Printer<JumpLabel>> jumpLabelPrinter,
			final Provider<Printer<LocalVariableStatement>> localVariableStatementPrinter,
			final Provider<Printer<Return>> returnPrinter, final Provider<Printer<Switch>> switchPrinter,
			final Provider<Printer<SynchronizedBlock>> synchronizedBlockPrinter,
			final Provider<Printer<Throw>> throwPrinter, final Provider<Printer<TryBlock>> tryBlockPrinter,
			final Provider<Printer<DoWhileLoop>> doWhileLoopPrinter,
			final Provider<Printer<WhileLoop>> whileLoopPrinter,
			final Provider<Printer<YieldStatement>> yieldStatementPrinter) {
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
	public void print(final Statement element, final BufferedWriter writer) throws IOException {
		if (mappings.isEmpty()) {
			mappings.add(new Mapping<>(ConcreteClassifier.class, concreteClassifierPrinter.get()));
			mappings.add(new Mapping<>(Assert.class, assertPrinter.get()));
			mappings.add(new Mapping<>(Block.class, blockPrinter.get()));
			mappings.add(new Mapping<>(Condition.class, conditionPrinter.get()));
			mappings.add(new Mapping<>(ExpressionStatement.class, expressionStatementPrinter.get()));
			mappings.add(new Mapping<>(ForLoop.class, forLoopPrinter.get()));
			mappings.add(new Mapping<>(ForEachLoop.class, forEachLoopPrinter.get()));
			mappings.add(new Mapping<>(Break.class, breakPrinter.get()));
			mappings.add(new Mapping<>(Continue.class, continuePrinter.get()));
			mappings.add(new Mapping<>(JumpLabel.class, jumpLabelPrinter.get()));
			mappings.add(new Mapping<>(LocalVariableStatement.class, localVariableStatementPrinter.get()));
			mappings.add(new Mapping<>(Return.class, returnPrinter.get()));
			mappings.add(new Mapping<>(Switch.class, switchPrinter.get()));
			mappings.add(new Mapping<>(SynchronizedBlock.class, synchronizedBlockPrinter.get()));
			mappings.add(new Mapping<>(Throw.class, throwPrinter.get()));
			mappings.add(new Mapping<>(TryBlock.class, tryBlockPrinter.get()));
			mappings.add(new Mapping<>(DoWhileLoop.class, doWhileLoopPrinter.get()));
			mappings.add(new Mapping<>(WhileLoop.class, whileLoopPrinter.get()));
			mappings.add(new Mapping<>(YieldStatement.class, yieldStatementPrinter.get()));
		}

		for (final Mapping<? extends Statement> mapping : mappings) {
			final boolean printed = mapping.checkAndPrint(element, writer);
			if (printed) {
				return;
			}
		}

		if (element instanceof EmptyStatement) {
			emptyStatementPrinter.get().print(writer);
		}

	}

}
