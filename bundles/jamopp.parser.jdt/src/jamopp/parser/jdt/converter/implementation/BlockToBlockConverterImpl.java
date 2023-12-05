package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.BlockToBlockConverter;
import jamopp.parser.jdt.converter.interfaces.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class BlockToBlockConverterImpl
		implements BlockToBlockConverter, ToConverter<Block, org.emftext.language.java.statements.Block> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final StatementToStatementConverter statementToStatementConverter;

	@Inject
	BlockToBlockConverterImpl(StatementsFactory statementsFactory,
			StatementToStatementConverter statementToStatementConverter, UtilLayout layoutInformationConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@SuppressWarnings("unchecked")
	public org.emftext.language.java.statements.Block convert(Block block) {
		org.emftext.language.java.statements.Block result = statementsFactory.createBlock();
		result.setName("");
		block.statements()
				.forEach(obj -> result.getStatements().add(statementToStatementConverter.convert((Statement) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, block);
		return result;
	}

}
