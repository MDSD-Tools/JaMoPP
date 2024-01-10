package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

import org.eclipse.jdt.core.dom.Statement;

public interface StatementHandler {

	tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement);

}
