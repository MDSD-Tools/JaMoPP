package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Expression;

public interface ToExpressionConverter{

	public org.emftext.language.java.expressions.Expression convert(Expression expr);

}
