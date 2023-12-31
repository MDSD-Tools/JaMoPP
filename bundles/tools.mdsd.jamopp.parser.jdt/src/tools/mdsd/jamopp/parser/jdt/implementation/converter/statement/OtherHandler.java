package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;

public class OtherHandler {

	private final StatementsFactory statementsFactory;
	private final UtilReferenceWalker utilReferenceWalker;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromStatement;

	@Inject
	public OtherHandler(UtilReferenceWalker utilReferenceWalker, StatementsFactory statementsFactory,
			Converter<Statement, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromStatement) {
		this.statementsFactory = statementsFactory;
		this.utilReferenceWalker = utilReferenceWalker;
		this.toReferenceConverterFromStatement = toReferenceConverterFromStatement;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		tools.mdsd.jamopp.model.java.statements.ExpressionStatement result = statementsFactory
				.createExpressionStatement();
		result.setExpression(utilReferenceWalker.walkUp(toReferenceConverterFromStatement.convert(statement)));
		return result;
	}

}
