package jamopp.parser.jdt.converter.interfaces;

import org.eclipse.jdt.core.dom.Expression;

public interface ToExpressionConverter
		extends ToConverter<Expression, org.emftext.language.java.expressions.Expression> {

	public org.emftext.language.java.expressions.Expression convert(Expression expr);

}
