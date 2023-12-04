package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Statement;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public interface StatementToStatementConverter
		extends ToConverter<Statement, org.emftext.language.java.statements.Statement> {

	public org.emftext.language.java.statements.Statement convert(Statement statement);

}
