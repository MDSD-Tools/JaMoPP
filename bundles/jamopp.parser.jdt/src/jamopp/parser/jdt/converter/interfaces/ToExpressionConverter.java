package jamopp.parser.jdt.converter.interfaces;

public interface ToExpressionConverter
		extends ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> {

	public org.emftext.language.java.expressions.Expression convert(org.eclipse.jdt.core.dom.Expression expr);

}
