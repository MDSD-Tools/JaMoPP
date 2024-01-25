package tools.mdsd.jamopp.parser.interfaces.converter;

import org.eclipse.jdt.core.dom.Statement;

public interface StatementHandler {

	tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement);

}
