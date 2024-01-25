package tools.mdsd.jamopp.parser.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class SynchonizedStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;

	@Inject
	public SynchonizedStatementHandler(final StatementsFactory statementsFactory,
			final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.blockToBlockConverter = blockToBlockConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final SynchronizedStatement synSt = (SynchronizedStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.SynchronizedBlock result = statementsFactory
				.createSynchronizedBlock();
		result.setLockProvider(expressionConverterUtility.convert(synSt.getExpression()));
		result.setBlock(blockToBlockConverter.convert(synSt.getBody()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, synSt);
		return result;
	}

}
