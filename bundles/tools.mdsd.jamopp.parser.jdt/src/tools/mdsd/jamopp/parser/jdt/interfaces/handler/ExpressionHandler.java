package tools.mdsd.jamopp.parser.jdt.interfaces.handler;

import org.eclipse.jdt.core.dom.Expression;

public interface ExpressionHandler {

	tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr);

}
