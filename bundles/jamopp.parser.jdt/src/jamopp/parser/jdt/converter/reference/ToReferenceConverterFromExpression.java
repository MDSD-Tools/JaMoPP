package jamopp.parser.jdt.converter.reference;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.references.ReferencesFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.ToAnonymousClassConverter;
import jamopp.parser.jdt.converter.ToArrayInitialisierConverter;
import jamopp.parser.jdt.converter.ToExpressionConverter;
import jamopp.parser.jdt.converter.ToTypeReferenceConverter;
import jamopp.parser.jdt.util.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;
import jamopp.parser.jdt.visitorhelper.ToAnnotationInstanceConverter;

public class ToReferenceConverterFromExpression implements ReferenceConverter<Expression> {

	private final ExpressionsFactory expressionsFactory;
	private final LiteralsFactory literalsFactory;
	private final ReferencesFactory referencesFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final ArraysFactory arraysFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToArrayInitialisierConverter toArrayInitialisierConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ToAnonymousClassConverter toAnonymousClassConverter;
	private final ReferenceWalker referenceWalker;
	private final Provider<ToReferenceConverterFromName> toReferenceConverterFromName;
	private final Provider<ToReferenceConverterFromMethodInvocation> toReferenceConverterFromMethodInvocation;
	private final Provider<ToReferenceConverterFromType> toReferenceConverterFromType;

	@Inject
	ToReferenceConverterFromExpression(UtilNamedElement utilNamedElement,
			ToTypeReferenceConverter toTypeReferenceConverter,
			Provider<ToReferenceConverterFromType> toReferenceConverterFromType,
			Provider<ToReferenceConverterFromName> toReferenceConverterFromName,
			Provider<ToReferenceConverterFromMethodInvocation> toReferenceConverterFromMethodInvocation,
			ToArrayInitialisierConverter toArrayInitialisierConverter,
			ToAnonymousClassConverter toAnonymousClassConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter, ReferencesFactory referencesFactory,
			ReferenceWalker referenceWalker, LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility, InstantiationsFactory instantiationsFactory,
			ExpressionsFactory expressionsFactory, ToExpressionConverter expressionConverterUtility,
			ArraysFactory arraysFactory) {
		this.expressionsFactory = expressionsFactory;
		this.literalsFactory = literalsFactory;
		this.referencesFactory = referencesFactory;
		this.instantiationsFactory = instantiationsFactory;
		this.arraysFactory = arraysFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toArrayInitialisierConverter = toArrayInitialisierConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
		this.toAnonymousClassConverter = toAnonymousClassConverter;
		this.referenceWalker = referenceWalker;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.toReferenceConverterFromMethodInvocation = toReferenceConverterFromMethodInvocation;
		this.toReferenceConverterFromType = toReferenceConverterFromType;
	}

	public org.emftext.language.java.references.Reference convertToReference(Expression expr) {
		return referenceWalker.walkUp(convert(expr));
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.references.Reference convert(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_ACCESS) {
			ArrayAccess arr = (ArrayAccess) expr;
			org.emftext.language.java.references.Reference parent = convert(arr.getArray());
			org.emftext.language.java.arrays.ArraySelector selector = arraysFactory.createArraySelector();
			selector.setPosition(expressionConverterUtility.convert(arr.getIndex()));
			parent.getArraySelectors().add(selector);
			return parent;
		}
		if (expr.getNodeType() == ASTNode.ARRAY_CREATION) {
			ArrayCreation arr = (ArrayCreation) expr;
			if (arr.getInitializer() != null) {
				org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped result = arraysFactory
						.createArrayInstantiationByValuesTyped();
				result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr.getType(), result);
				result.setArrayInitializer(
						toArrayInitialisierConverter.convertToArrayInitializer(arr.getInitializer()));
				layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
				return result;
			}
			org.emftext.language.java.arrays.ArrayInstantiationBySize result = arraysFactory
					.createArrayInstantiationBySize();
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr.getType(), result, arr.dimensions().size());
			arr.dimensions().forEach(
					obj -> result.getSizes().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped result = arraysFactory
					.createArrayInstantiationByValuesUntyped();
			result.setArrayInitializer(toArrayInitialisierConverter.convertToArrayInitializer((ArrayInitializer) expr));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
			ClassInstanceCreation arr = (ClassInstanceCreation) expr;
			org.emftext.language.java.instantiations.NewConstructorCall result;
			if (arr.getType().isParameterizedType() && ((ParameterizedType) arr.getType()).typeArguments().isEmpty()) {
				result = instantiationsFactory.createNewConstructorCallWithInferredTypeArguments();
			} else {
				result = instantiationsFactory.createNewConstructorCall();
			}
			arr.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			arr.arguments().forEach(
					obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			if (arr.getAnonymousClassDeclaration() != null) {
				result.setAnonymousClass(
						toAnonymousClassConverter.convertToAnonymousClass(arr.getAnonymousClassDeclaration()));
			}
			if (arr.getExpression() != null) {
				org.emftext.language.java.references.Reference parent = convert(arr.getExpression());
				parent.setNext(result);
			}
			return result;
		}
		if (expr.getNodeType() == ASTNode.FIELD_ACCESS) {
			FieldAccess arr = (FieldAccess) expr;
			org.emftext.language.java.references.Reference parent = convert(arr.getExpression());
			org.emftext.language.java.references.IdentifierReference result = toReferenceConverterFromName.get()
					.convert(arr.getName());
			parent.setNext(result);
			return result;
		}
		if (expr.getNodeType() == ASTNode.METHOD_INVOCATION) {
			return toReferenceConverterFromMethodInvocation.get().convert((MethodInvocation) expr);
		}
		if (expr.getNodeType() == ASTNode.QUALIFIED_NAME) {
			QualifiedName arr = (QualifiedName) expr;
			org.emftext.language.java.references.IdentifierReference result = toReferenceConverterFromName.get()
					.convert(arr.getName());
			org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.SIMPLE_NAME) {
			return toReferenceConverterFromName.get().convert((SimpleName) expr);
		}
		if (expr.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION) {
			ParenthesizedExpression arr = (ParenthesizedExpression) expr;
			org.emftext.language.java.expressions.NestedExpression result = expressionsFactory.createNestedExpression();
			result.setExpression(expressionConverterUtility.convert(arr.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.STRING_LITERAL) {
			StringLiteral arr = (StringLiteral) expr;
			org.emftext.language.java.references.StringReference result = referencesFactory.createStringReference();
			result.setValue(arr.getEscapedValue().substring(1, arr.getEscapedValue().length() - 1));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.SUPER_FIELD_ACCESS) {
			SuperFieldAccess arr = (SuperFieldAccess) expr;
			org.emftext.language.java.references.SelfReference partOne = referencesFactory.createSelfReference();
			partOne.setSelf(literalsFactory.createSuper());
			if (arr.getQualifier() != null) {
				org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
				parent.setNext(partOne);
			}
			org.emftext.language.java.references.IdentifierReference partTwo = toReferenceConverterFromName.get()
					.convert(arr.getName());
			partOne.setNext(partTwo);
			return partTwo;
		}
		if (expr.getNodeType() == ASTNode.SUPER_METHOD_INVOCATION) {
			SuperMethodInvocation arr = (SuperMethodInvocation) expr;
			org.emftext.language.java.references.SelfReference partOne = referencesFactory.createSelfReference();
			partOne.setSelf(literalsFactory.createSuper());
			if (arr.getQualifier() != null) {
				org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
				parent.setNext(partOne);
			}
			org.emftext.language.java.references.MethodCall partTwo = referencesFactory.createMethodCall();
			arr.typeArguments().forEach(obj -> partTwo.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			arr.arguments().forEach(obj -> partTwo.getArguments()
					.add(expressionConverterUtility.convert((Expression) obj)));
			org.emftext.language.java.members.Method proxy;
			if (arr.getName().resolveBinding() != null) {
				proxy = jdtResolverUtility.getMethod((IMethodBinding) arr.getName().resolveBinding());
			} else {
				proxy = jdtResolverUtility.getClassMethod(arr.getName().getIdentifier());
				proxy.setName(arr.getName().getIdentifier());
			}
			utilNamedElement.setNameOfElement(arr.getName(), proxy);
			partTwo.setTarget(proxy);
			partOne.setNext(partTwo);
			layoutInformationConverter.convertToMinimalLayoutInformation(partTwo, arr);
			return partTwo;
		}
		if (expr.getNodeType() == ASTNode.THIS_EXPRESSION) {
			ThisExpression arr = (ThisExpression) expr;
			org.emftext.language.java.references.SelfReference result = referencesFactory.createSelfReference();
			result.setSelf(literalsFactory.createThis());
			if (arr.getQualifier() != null) {
				org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
				parent.setNext(result);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.TYPE_LITERAL) {
			TypeLiteral arr = (TypeLiteral) expr;
			org.emftext.language.java.references.ReflectiveClassReference result = referencesFactory
					.createReflectiveClassReference();
			org.emftext.language.java.references.Reference parent = toReferenceConverterFromType.get()
					.internalConvertToReference(arr.getType());
			parent.setNext(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		return null;
	}

}
