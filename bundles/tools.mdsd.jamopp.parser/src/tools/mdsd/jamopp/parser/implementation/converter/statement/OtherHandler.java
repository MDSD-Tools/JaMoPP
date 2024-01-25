package tools.mdsd.jamopp.parser.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilReferenceWalker;

public class OtherHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilReferenceWalker utilReferenceWalker;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromStatement;

	@Inject
	public OtherHandler(final UtilReferenceWalker utilReferenceWalker, final StatementsFactory statementsFactory,
			final Converter<Statement, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromStatement) {
		this.statementsFactory = statementsFactory;
		this.utilReferenceWalker = utilReferenceWalker;
		this.toReferenceConverterFromStatement = toReferenceConverterFromStatement;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final tools.mdsd.jamopp.model.java.statements.ExpressionStatement result = statementsFactory
				.createExpressionStatement();
		result.setExpression(utilReferenceWalker.walkUp(toReferenceConverterFromStatement.convert(statement)));
		return result;
	}

}
