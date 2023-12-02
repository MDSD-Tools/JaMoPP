package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;

abstract class Handler {

	abstract org.emftext.language.java.expressions.Expression handle(Expression expr);

}
