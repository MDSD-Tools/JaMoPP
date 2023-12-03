package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import com.google.inject.Inject;

class ToReferenceConverterFromStatement {

	private final LiteralsFactory literalsFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilExpressionConverter expressionConverterUtility;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ReferenceWalker referenceWalker;
	private final ToReferenceConverterFromExpression toReferenceConverterFromExpression;

	@Inject
	ToReferenceConverterFromStatement(ToTypeReferenceConverter toTypeReferenceConverter,
			ReferenceWalker referenceWalker, LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter,
			InstantiationsFactory instantiationsFactory, UtilExpressionConverter expressionConverterUtility,
			ToReferenceConverterFromExpression toReferenceConverterFromExpression) {
		this.literalsFactory = literalsFactory;
		this.instantiationsFactory = instantiationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.referenceWalker = referenceWalker;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	org.emftext.language.java.references.Reference convertToReference(Statement st) {
		return referenceWalker.walkUp(internalConvertToReference(st));
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference internalConvertToReference(Statement st) {
		if (st.getNodeType() == ASTNode.CONSTRUCTOR_INVOCATION) {
			ConstructorInvocation invoc = (ConstructorInvocation) st;
			org.emftext.language.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setCallTarget(literalsFactory.createThis());
			invoc.arguments().forEach(
					obj -> result.getArguments().add(expressionConverterUtility.convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			return result;
		}
		if (st.getNodeType() == ASTNode.SUPER_CONSTRUCTOR_INVOCATION) {
			SuperConstructorInvocation invoc = (SuperConstructorInvocation) st;
			org.emftext.language.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setCallTarget(literalsFactory.createSuper());
			invoc.arguments().forEach(
					obj -> result.getArguments().add(expressionConverterUtility.convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			if (invoc.getExpression() != null) {
				org.emftext.language.java.references.Reference parent = toReferenceConverterFromExpression
						.internalConvertToReference(invoc.getExpression());
				parent.setNext(result);
			}
			return result;
		}
		return null;
	}

}
