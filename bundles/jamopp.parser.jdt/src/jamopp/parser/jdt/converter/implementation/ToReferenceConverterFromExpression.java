package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
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
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;
import jamopp.parser.jdt.converter.helper.UtilReferenceWalker;
import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ReferenceConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToReferenceConverterFromExpression implements ReferenceConverter<Expression>,
		ToConverter<Expression, org.emftext.language.java.references.Reference> {

	private ExpressionsFactory expressionsFactory;
	private LiteralsFactory literalsFactory;
	private ReferencesFactory referencesFactory;
	private InstantiationsFactory instantiationsFactory;
	private ArraysFactory arraysFactory;
	private UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private UtilLayout layoutInformationConverter;
	private UtilJdtResolver jdtResolverUtility;
	private UtilNamedElement utilNamedElement;
	private UtilReferenceWalker utilReferenceWalker;
	private ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierConverter;
	private ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private ToConverter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter;
	private ToConverter<SimpleName, IdentifierReference> toReferenceConverterFromName;
	private ToConverter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation;
	private ToReferenceConverterFromType toReferenceConverterFromType;
	private ToConverter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	void ToReferenceConverterFromExpression(UtilNamedElement utilNamedElement,
			ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToReferenceConverterFromType toReferenceConverterFromType,
			ToConverter<SimpleName, IdentifierReference> toReferenceConverterFromName,
			ToConverter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation,
			ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierConverter,
			ToConverter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter,
			ToConverter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ReferencesFactory referencesFactory, UtilReferenceWalker utilReferenceWalker,
			LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			InstantiationsFactory instantiationsFactory, ExpressionsFactory expressionsFactory,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			ArraysFactory arraysFactory, ToConverter<Type, TypeArgument> typeArgumentConverter,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
		this.utilReferenceWalker = utilReferenceWalker;
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.toReferenceConverterFromMethodInvocation = toReferenceConverterFromMethodInvocation;
		this.toReferenceConverterFromType = toReferenceConverterFromType;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.typeToTypeArgumentConverter = typeArgumentConverter;
	}

	public org.emftext.language.java.references.Reference convertToReference(Expression expr) {
		return utilReferenceWalker.walkUp(convert(expr));
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.references.Reference convert(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convert((Annotation) expr);
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
				utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(arr.getType(), result);
				result.setArrayInitializer(toArrayInitialisierConverter.convert(arr.getInitializer()));
				layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
				return result;
			}
			org.emftext.language.java.arrays.ArrayInstantiationBySize result = arraysFactory
					.createArrayInstantiationBySize();
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(arr.getType(), result,
					arr.dimensions().size());
			arr.dimensions()
					.forEach(obj -> result.getSizes().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped result = arraysFactory
					.createArrayInstantiationByValuesUntyped();
			result.setArrayInitializer(toArrayInitialisierConverter.convert((ArrayInitializer) expr));
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
			arr.typeArguments()
					.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			arr.arguments()
					.forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			if (arr.getAnonymousClassDeclaration() != null) {
				result.setAnonymousClass(toAnonymousClassConverter.convert(arr.getAnonymousClassDeclaration()));
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
			org.emftext.language.java.references.IdentifierReference result = toReferenceConverterFromName
					.convert(arr.getName());
			parent.setNext(result);
			return result;
		}
		if (expr.getNodeType() == ASTNode.METHOD_INVOCATION) {
			return toReferenceConverterFromMethodInvocation.convert((MethodInvocation) expr);
		}
		if (expr.getNodeType() == ASTNode.QUALIFIED_NAME) {
			QualifiedName arr = (QualifiedName) expr;
			org.emftext.language.java.references.IdentifierReference result = toReferenceConverterFromName
					.convert(arr.getName());
			org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.SIMPLE_NAME) {
			return toReferenceConverterFromName.convert((SimpleName) expr);
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
			org.emftext.language.java.references.IdentifierReference partTwo = toReferenceConverterFromName
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
			arr.typeArguments().forEach(
					obj -> partTwo.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
			arr.arguments()
					.forEach(obj -> partTwo.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
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
			org.emftext.language.java.references.Reference parent = toReferenceConverterFromType
					.internalConvertToReference(arr.getType());
			parent.setNext(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		return null;
	}

}
