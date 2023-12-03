package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToExpressionConverter;
import jamopp.parser.jdt.converter.ToOrdinaryParameterConverter;
import jamopp.parser.jdt.converter.ToTypeReferencesConverter;
import jamopp.parser.jdt.converter.UtilStatementConverter;
import jamopp.parser.jdt.converter.resolver.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilArrays;
import jamopp.parser.jdt.util.UtilLayout;

public class HandlerLambdaExpression extends Handler {

	private final TypesFactory typesFactory;
	private final ExpressionsFactory expressionsFactory;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;
	private final UtilJdtResolver utilJdtResolver;
	private final UtilArrays utilJdtBindingConverter;
	private final UtilStatementConverter utilStatementConverter;
	private final ToOrdinaryParameterConverter toOrdinaryParameterConverter;
	private final ToTypeReferencesConverter toTypeReferencesConverter;

	@Inject
	HandlerLambdaExpression(UtilStatementConverter utilStatementConverter, UtilLayout utilLayout,
			UtilJdtResolver utilJdtResolver, UtilArrays utilJdtBindingConverter,
			ToOrdinaryParameterConverter toOrdinaryParameterConverter, ToExpressionConverter toExpressionConverter,
			ExpressionsFactory expressionsFactory, TypesFactory typesFactory,
			ToTypeReferencesConverter toTypeReferencesConverter) {
		this.typesFactory = typesFactory;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilJdtResolver = utilJdtResolver;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.utilStatementConverter = utilStatementConverter;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
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
			result.setBody(utilStatementConverter.convertToBlock((Block) lambda.getBody()));
		}
		utilLayout.convertToMinimalLayoutInformation(result, lambda);
		return result;
	}

}
