package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class BlockToBlockConverterImpl implements Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	BlockToBlockConverterImpl(StatementsFactory statementsFactory,
			Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			UtilLayout layoutInformationConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@SuppressWarnings("unchecked")
	public tools.mdsd.jamopp.model.java.statements.Block convert(Block block) {
		tools.mdsd.jamopp.model.java.statements.Block result = statementsFactory.createBlock();
		result.setName("");
		block.statements()
				.forEach(obj -> result.getStatements().add(statementToStatementConverter.convert((Statement) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, block);
		return result;
	}

}
