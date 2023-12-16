package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToReferenceConverterFromMethodInvocation implements Converter<MethodInvocation, MethodCall> {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression;
	private final Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	ToReferenceConverterFromMethodInvocation(UtilNamedElement utilNamedElement,
			ToTypeReferenceConverter toTypeReferenceConverter, ReferencesFactory referencesFactory,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			Converter<Expression, org.emftext.language.java.references.Reference> toReferenceConverterFromExpression,
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
		org.emftext.language.java.references.Reference parent = null;
		if (arr.getExpression() != null) {
			parent = toReferenceConverterFromExpression.convert(arr.getExpression());
		}
		org.emftext.language.java.references.MethodCall result = referencesFactory.createMethodCall();
		arr.typeArguments()
				.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		arr.arguments().forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
		IMethodBinding methBind = arr.resolveMethodBinding();
		org.emftext.language.java.members.Method methodProxy = null;
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
