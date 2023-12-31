package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;

public class TryStatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;
	private final Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> toLocalVariableConverter;
	private final Converter<CatchClause, CatchBlock> toCatchblockConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression;

	@Inject
	public TryStatementHandler(UtilReferenceWalker utilReferenceWalker,
			Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression,
			Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> toLocalVariableConverter,
			Converter<CatchClause, CatchBlock> toCatchblockConverter, StatementsFactory statementsFactory,
			UtilLayout layoutInformationConverter,
			Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilReferenceWalker = utilReferenceWalker;
		this.blockToBlockConverter = blockToBlockConverter;
		this.toLocalVariableConverter = toLocalVariableConverter;
		this.toCatchblockConverter = toCatchblockConverter;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	@SuppressWarnings("unchecked")
	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		TryStatement trySt = (TryStatement) statement;
		tools.mdsd.jamopp.model.java.statements.TryBlock result = statementsFactory.createTryBlock();
		trySt.resources().forEach(obj -> {
			Expression resExpr = (Expression) obj;
			if (resExpr instanceof VariableDeclarationExpression) {
				result.getResources().add(toLocalVariableConverter.convert((VariableDeclarationExpression) resExpr));
			} else {
				result.getResources().add((tools.mdsd.jamopp.model.java.references.ElementReference) utilReferenceWalker
						.walkUp(toReferenceConverterFromExpression.convert(resExpr)));
			}
		});
		result.setBlock(blockToBlockConverter.convert(trySt.getBody()));
		trySt.catchClauses()
				.forEach(obj -> result.getCatchBlocks().add(toCatchblockConverter.convert((CatchClause) obj)));
		if (trySt.getFinally() != null) {
			result.setFinallyBlock(blockToBlockConverter.convert(trySt.getFinally()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, trySt);
		return result;
	}

}
