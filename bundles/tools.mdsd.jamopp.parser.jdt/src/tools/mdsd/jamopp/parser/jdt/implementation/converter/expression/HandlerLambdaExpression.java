package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class HandlerLambdaExpression implements ExpressionHandler {

	private final TypesFactory typesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final JdtResolver iUtilJdtResolver;
	private final Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;

	@Inject
	HandlerLambdaExpression(UtilLayout utilLayout, JdtResolver iUtilJdtResolver,
			Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory, TypesFactory typesFactory,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.typesFactory = typesFactory;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.blockToBlockConverter = blockToBlockConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		LambdaExpression lambda = (LambdaExpression) expr;
		tools.mdsd.jamopp.model.java.expressions.LambdaExpression result = expressionsFactory.createLambdaExpression();
		if (!lambda.parameters().isEmpty() && lambda.parameters().get(0) instanceof VariableDeclarationFragment) {
			tools.mdsd.jamopp.model.java.expressions.ImplicitlyTypedLambdaParameters param;
			if (!lambda.hasParentheses()) {
				param = expressionsFactory.createSingleImplicitLambdaParameter();
			} else {
				param = expressionsFactory.createImplicitlyTypedLambdaParameters();
			}
			lambda.parameters().forEach(obj -> {
				VariableDeclarationFragment frag = (VariableDeclarationFragment) obj;
				IVariableBinding binding = frag.resolveBinding();
				tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter nextParam;
				if (binding != null) {
					nextParam = iUtilJdtResolver.getOrdinaryParameter(binding);
					nextParam.setTypeReference(toTypeReferencesConverter.convert(binding.getType()).get(0));
				} else {
					nextParam = iUtilJdtResolver.getOrdinaryParameter(frag.getName().getIdentifier() + frag.hashCode());
					nextParam.setTypeReference(typesFactory.createVoid());
				}
				nextParam.setName(frag.getName().getIdentifier());
				param.getParameters().add(nextParam);
			});
			result.setParameters(param);
		} else {
			tools.mdsd.jamopp.model.java.expressions.ExplicitlyTypedLambdaParameters param = expressionsFactory
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
