package jamopp.parser.jdt.converter.implementation.handler;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

public class HandlerLambdaExpression implements ExpressionHandler {

	private final TypesFactory typesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final UtilJdtResolver utilJdtResolver;
	private final ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final ToConverter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final ToConverter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter;

	@Inject
	HandlerLambdaExpression(UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToConverter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory, TypesFactory typesFactory,
			ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			ToConverter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter) {
		this.typesFactory = typesFactory;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilJdtResolver = utilJdtResolver;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.blockToBlockConverter = blockToBlockConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		LambdaExpression lambda = (LambdaExpression) expr;
		org.emftext.language.java.expressions.LambdaExpression result = expressionsFactory.createLambdaExpression();
		if (!lambda.parameters().isEmpty() && lambda.parameters().get(0) instanceof VariableDeclarationFragment) {
			org.emftext.language.java.expressions.ImplicitlyTypedLambdaParameters param;
			if (!lambda.hasParentheses()) {
				param = expressionsFactory.createSingleImplicitLambdaParameter();
			} else {
				param = expressionsFactory.createImplicitlyTypedLambdaParameters();
			}
			lambda.parameters().forEach(obj -> {
				VariableDeclarationFragment frag = (VariableDeclarationFragment) obj;
				IVariableBinding binding = frag.resolveBinding();
				org.emftext.language.java.parameters.OrdinaryParameter nextParam;
				if (binding != null) {
					nextParam = utilJdtResolver.getOrdinaryParameter(binding);
					nextParam.setTypeReference(toTypeReferencesConverter.convert(binding.getType()).get(0));
				} else {
					nextParam = utilJdtResolver.getOrdinaryParameter(frag.getName().getIdentifier() + frag.hashCode());
					nextParam.setTypeReference(typesFactory.createVoid());
				}
				nextParam.setName(frag.getName().getIdentifier());
				param.getParameters().add(nextParam);
			});
			result.setParameters(param);
		} else {
			org.emftext.language.java.expressions.ExplicitlyTypedLambdaParameters param = expressionsFactory
					.createExplicitlyTypedLambdaParameters();
			lambda.parameters().forEach(obj -> param.getParameters()
					.add(toOrdinaryParameterConverter.convert((SingleVariableDeclaration) obj)));
			result.setParameters(param);
		}
		if (lambda.getBody() instanceof Expression) {
			result.setBody(toExpressionConverter.convert((Expression) lambda.getBody()));
		} else {
			result.setBody(blockToBlockConverter.convert((Block) lambda.getBody()));
		}
		utilLayout.convertToMinimalLayoutInformation(result, lambda);
		return result;
	}

}
