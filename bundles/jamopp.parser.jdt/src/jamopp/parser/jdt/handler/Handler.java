package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.Expression;

public abstract class Handler {

	abstract org.emftext.language.java.expressions.Expression handle(Expression expr);

}
