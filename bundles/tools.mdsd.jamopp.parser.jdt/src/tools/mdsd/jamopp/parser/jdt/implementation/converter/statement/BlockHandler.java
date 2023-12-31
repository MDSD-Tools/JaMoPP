package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class BlockHandler {

	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;

	@Inject
	public BlockHandler(Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.blockToBlockConverter = blockToBlockConverter;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		return blockToBlockConverter.convert((Block) statement);
	}
}
