package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;

public class BlockToBlockConverterImpl implements Converter<Block, org.emftext.language.java.statements.Block> {

	private final StatementsFactory statementsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final Converter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter;

	@Inject
	BlockToBlockConverterImpl(StatementsFactory statementsFactory,
			Converter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter,
			IUtilLayout layoutInformationConverter) {
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
