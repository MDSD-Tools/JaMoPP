package jamopp.parser.jdt.converter.implementation;

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

import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.helper.UtilReferenceWalker;
import jamopp.parser.jdt.converter.interfaces.ReferenceConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToReferenceConverterFromStatement implements ReferenceConverter<Statement>,
		ToConverter<Statement, org.emftext.language.java.references.Reference> {

	private final LiteralsFactory literalsFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilReferenceWalker utilReferenceWalker;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private final ToConverter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression;
	private final ToConverter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToReferenceConverterFromStatement(UtilReferenceWalker utilReferenceWalker, LiteralsFactory literalsFactory,
			UtilLayout layoutInformationConverter, InstantiationsFactory instantiationsFactory,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			ToConverter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression,
			ToConverter<Type, TypeArgument> typeToTypeArgumentConverter) {
		this.literalsFactory = literalsFactory;
		this.instantiationsFactory = instantiationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilReferenceWalker = utilReferenceWalker;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	public org.emftext.language.java.references.Reference convert(Statement st) {
		return utilReferenceWalker.walkUp(internalConvertToReference(st));
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference internalConvertToReference(Statement st) {
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
