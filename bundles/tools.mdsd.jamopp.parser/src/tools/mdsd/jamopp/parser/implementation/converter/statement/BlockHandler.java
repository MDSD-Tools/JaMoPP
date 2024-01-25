package tools.mdsd.jamopp.parser.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;

public class BlockHandler implements StatementHandler {

	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;

	@Inject
	public BlockHandler(final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.blockToBlockConverter = blockToBlockConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		return blockToBlockConverter.convert((Block) statement);
	}
}
