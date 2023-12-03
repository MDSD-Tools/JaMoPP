package jamopp.parser.jdt.converter.reference;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToExpressionConverter;
import jamopp.parser.jdt.converter.ToTypeReferenceConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class ToReferenceConverterFromStatement implements ReferenceConverter<Statement> {

	private final LiteralsFactory literalsFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter expressionConverterUtility;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ReferenceWalker referenceWalker;
	private final ToReferenceConverterFromExpression toReferenceConverterFromExpression;

	@Inject
	ToReferenceConverterFromStatement(ToTypeReferenceConverter toTypeReferenceConverter,
			ReferenceWalker referenceWalker, LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter,
			InstantiationsFactory instantiationsFactory, ToExpressionConverter expressionConverterUtility,
			ToReferenceConverterFromExpression toReferenceConverterFromExpression) {
		this.literalsFactory = literalsFactory;
		this.instantiationsFactory = instantiationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.referenceWalker = referenceWalker;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	public org.emftext.language.java.references.Reference convert(Statement st) {
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
					obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
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
					obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
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
