package jamopp.parser.jdt.converter.interfaces.handler;

import org.eclipse.jdt.core.dom.Expression;

public interface ExpressionHandler {

	org.emftext.language.java.expressions.Expression handle(Expression expr);

}
