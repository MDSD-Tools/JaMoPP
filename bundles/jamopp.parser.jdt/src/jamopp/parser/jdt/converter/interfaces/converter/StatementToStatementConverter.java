package jamopp.parser.jdt.converter.interfaces.converter;

import org.eclipse.jdt.core.dom.Statement;

public interface StatementToStatementConverter
		extends ToConverter<Statement, org.emftext.language.java.statements.Statement> {

	public org.emftext.language.java.statements.Statement convert(Statement statement);

}
