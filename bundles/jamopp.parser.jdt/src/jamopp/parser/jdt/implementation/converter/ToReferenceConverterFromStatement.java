package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToReferenceConverterFromStatement
		implements Converter<Statement, org.emftext.language.java.references.Reference> {

	private final LiteralsFactory literalsFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression;
	private final Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToReferenceConverterFromStatement(LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter,
			InstantiationsFactory instantiationsFactory,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression,
			Converter<Type, TypeArgument> typeToTypeArgumentConverter) {
		this.literalsFactory = literalsFactory;
		this.instantiationsFactory = instantiationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	@SuppressWarnings("unchecked")
	public org.emftext.language.java.references.Reference convert(Statement st) {
		if (st.getNodeType() == ASTNode.CONSTRUCTOR_INVOCATION) {
			ConstructorInvocation invoc = (ConstructorInvocation) st;
			org.emftext.language.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setCallTarget(literalsFactory.createThis());
			invoc.arguments()
					.forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			return result;
		}
		if (st.getNodeType() == ASTNode.SUPER_CONSTRUCTOR_INVOCATION) {
			SuperConstructorInvocation invoc = (SuperConstructorInvocation) st;
			org.emftext.language.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setCallTarget(literalsFactory.createSuper());
			invoc.arguments()
					.forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			if (invoc.getExpression() != null) {
				org.emftext.language.java.references.Reference parent = toReferenceConverterFromExpression
						.convert(invoc.getExpression());
				parent.setNext(result);
			}
			return result;
		}
		return null;
	}

}