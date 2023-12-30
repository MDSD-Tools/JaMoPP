package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToReferenceConverterFromMethodInvocation implements Converter<MethodInvocation, MethodCall> {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression;
	private final Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToReferenceConverterFromMethodInvocation(UtilNamedElement utilNamedElement,
			ToTypeReferenceConverter toTypeReferenceConverter, ReferencesFactory referencesFactory,
			UtilLayout layoutInformationConverter, JdtResolver jdtResolverUtility,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromExpression,
			Converter<Type, TypeArgument> typeToTypeArgumentConverter) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	@SuppressWarnings("unchecked")
	public MethodCall convert(MethodInvocation arr) {
		tools.mdsd.jamopp.model.java.references.Reference parent = null;
		if (arr.getExpression() != null) {
			parent = toReferenceConverterFromExpression.convert(arr.getExpression());
		}
		tools.mdsd.jamopp.model.java.references.MethodCall result = referencesFactory.createMethodCall();
		arr.typeArguments()
				.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		arr.arguments().forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
		IMethodBinding methBind = arr.resolveMethodBinding();
		tools.mdsd.jamopp.model.java.members.Method methodProxy = null;
		if (methBind != null) {
			methodProxy = jdtResolverUtility.getMethod(methBind);
		} else {
			methodProxy = jdtResolverUtility.getClassMethod(arr.getName().getIdentifier());
			methodProxy.setName(arr.getName().getIdentifier());
		}
		utilNamedElement.setNameOfElement(arr.getName(), methodProxy);
		result.setTarget(methodProxy);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		if (parent != null) {
			parent.setNext(result);
		}
		return result;
	}

}
