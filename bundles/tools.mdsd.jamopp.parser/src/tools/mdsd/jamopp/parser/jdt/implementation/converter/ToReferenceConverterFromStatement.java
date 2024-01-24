package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.instantiations.InstantiationsFactory;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToReferenceConverterFromStatement
		implements Converter<Statement, tools.mdsd.jamopp.model.java.references.Reference> {

	private final LiteralsFactory literalsFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression;
	private final Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	public ToReferenceConverterFromStatement(final LiteralsFactory literalsFactory,
			final UtilLayout layoutInformationConverter, final InstantiationsFactory instantiationsFactory,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression,
			final Converter<Type, TypeArgument> typeToTypeArgumentConverter) {
		this.literalsFactory = literalsFactory;
		this.instantiationsFactory = instantiationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public tools.mdsd.jamopp.model.java.references.Reference convert(final Statement statement) {
		tools.mdsd.jamopp.model.java.references.Reference reference = null;
		if (statement.getNodeType() == ASTNode.CONSTRUCTOR_INVOCATION) {
			final ConstructorInvocation invoc = (ConstructorInvocation) statement;
			final tools.mdsd.jamopp.model.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setCallTarget(literalsFactory.createThis());
			invoc.arguments()
					.forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			reference = result;
		} else if (statement.getNodeType() == ASTNode.SUPER_CONSTRUCTOR_INVOCATION) {
			final SuperConstructorInvocation invoc = (SuperConstructorInvocation) statement;
			final tools.mdsd.jamopp.model.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setCallTarget(literalsFactory.createSuper());
			invoc.arguments()
					.forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			if (invoc.getExpression() != null) {
				final tools.mdsd.jamopp.model.java.references.Reference parent = toReferenceConverterFromExpression
						.convert(invoc.getExpression());
				parent.setNext(result);
			}
			reference = result;
		}
		return reference;
	}

}
