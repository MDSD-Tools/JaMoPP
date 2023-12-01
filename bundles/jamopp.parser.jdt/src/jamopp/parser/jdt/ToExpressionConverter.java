package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;

interface ToExpressionConverter {

	public org.emftext.language.java.expressions.Expression convertToExpression(Expression expr);

}
