package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.converter.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

@SuppressWarnings("unused")
public class BlockToBlockConverterImpl implements ToConverter<Block, org.emftext.language.java.statements.Block> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToConverter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter;

	@Inject
	BlockToBlockConverterImpl(StatementsFactory statementsFactory,
			ToConverter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter,
			UtilLayout layoutInformationConverter) {
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
