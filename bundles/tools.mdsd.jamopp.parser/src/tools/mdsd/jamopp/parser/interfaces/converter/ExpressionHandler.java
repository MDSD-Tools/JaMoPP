package tools.mdsd.jamopp.parser.interfaces.converter;

import org.eclipse.jdt.core.dom.Expression;

public interface ExpressionHandler {

	tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr);

}
