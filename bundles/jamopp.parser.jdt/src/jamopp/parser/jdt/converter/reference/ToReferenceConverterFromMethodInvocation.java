package jamopp.parser.jdt.converter.reference;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToExpressionConverter;
import jamopp.parser.jdt.converter.ToTypeReferenceConverter;
import jamopp.parser.jdt.util.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToReferenceConverterFromMethodInvocation implements ReferenceConverter<MethodInvocation> {

	private final ReferencesFactory referencesFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToReferenceConverterFromExpression toReferenceConverterFromExpression;

	@Inject
	ToReferenceConverterFromMethodInvocation(UtilNamedElement utilNamedElement,
			ToTypeReferenceConverter toTypeReferenceConverter, ReferencesFactory referencesFactory,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ToExpressionConverter expressionConverterUtility,
			ToReferenceConverterFromExpression toReferenceConverterFromExpression) {
		this.referencesFactory = referencesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toReferenceConverterFromExpression = toReferenceConverterFromExpression;
	}

	@SuppressWarnings("unchecked")
	public MethodCall convert(MethodInvocation arr) {
		org.emftext.language.java.references.Reference parent = null;
		if (arr.getExpression() != null) {
			parent = toReferenceConverterFromExpression.convert(arr.getExpression());
		}
		org.emftext.language.java.references.MethodCall result = referencesFactory.createMethodCall();
		arr.typeArguments().forEach(
				obj -> result.getCallTypeArguments().add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
		arr.arguments().forEach(
				obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
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
