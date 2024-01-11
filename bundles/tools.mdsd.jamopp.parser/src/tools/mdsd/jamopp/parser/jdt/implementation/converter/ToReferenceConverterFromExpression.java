package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

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

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.instantiations.InstantiationsFactory;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToReferenceConverterFromExpression
		implements Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference> {

	private ExpressionsFactory expressionsFactory;
	private LiteralsFactory literalsFactory;
	private ReferencesFactory referencesFactory;
	private InstantiationsFactory instantiationsFactory;
	private ArraysFactory arraysFactory;
	private ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private UtilLayout layoutInformationConverter;
	private JdtResolver jdtResolverUtility;
	private UtilNamedElement utilNamedElement;
	private Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private Converter<Type, TypeReference> toTypeReferenceConverter;
	private Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> toArrayInitialisierConverter;
	private Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter;
	private Converter<SimpleName, IdentifierReference> toReferenceConverterFromName;
	private Converter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation;
	private Converter<Type, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromType;
	private Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	public void setMembers(UtilNamedElement utilNamedElement, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<Type, tools.mdsd.jamopp.model.java.references.Reference> toReferenceConverterFromType,
			Converter<SimpleName, IdentifierReference> toReferenceConverterFromName,
			Converter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation,
			Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> toArrayInitialisierConverter,
			Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ReferencesFactory referencesFactory, LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter,
			JdtResolver jdtResolverUtility, InstantiationsFactory instantiationsFactory,
			ExpressionsFactory expressionsFactory,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			ArraysFactory arraysFactory, Converter<Type, TypeArgument> typeArgumentConverter,
			ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.toReferenceConverterFromMethodInvocation = toReferenceConverterFromMethodInvocation;
		this.toReferenceConverterFromType = toReferenceConverterFromType;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		typeToTypeArgumentConverter = typeArgumentConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.Reference convert(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convert((Annotation) expr);
		} else if (expr.getNodeType() == ASTNode.ARRAY_ACCESS) {
			return handleArrayAcces(expr);
		} else if (expr.getNodeType() == ASTNode.ARRAY_CREATION) {
			return handleArrayCreation(expr);
		} else if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			return handleArrayInitiliazer(expr);
		} else if (expr.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
			return handleClassInstanceCreation(expr);
		} else if (expr.getNodeType() == ASTNode.FIELD_ACCESS) {
			return handleFieldAcces(expr);
		} else if (expr.getNodeType() == ASTNode.METHOD_INVOCATION) {
			return toReferenceConverterFromMethodInvocation.convert((MethodInvocation) expr);
		} else if (expr.getNodeType() == ASTNode.QUALIFIED_NAME) {
			return handleQualifiedName(expr);
		} else if (expr.getNodeType() == ASTNode.SIMPLE_NAME) {
			return handleSimpleName(expr);
		} else if (expr.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION) {
			return handleParanthesizedExpression(expr);
		} else if (expr.getNodeType() == ASTNode.STRING_LITERAL) {
			return handleStringLiteral(expr);
		} else if (expr.getNodeType() == ASTNode.SUPER_FIELD_ACCESS) {
			return handleSuperFieldAcces(expr);
		} else if (expr.getNodeType() == ASTNode.SUPER_METHOD_INVOCATION) {
			return handleSuperMethodInvocation(expr);
		} else if (expr.getNodeType() == ASTNode.THIS_EXPRESSION) {
			return handleThisExpression(expr);
		} else if (expr.getNodeType() == ASTNode.TYPE_LITERAL) {
			return handleTypeLiteral(expr);
		}
		return null;

	}

	private tools.mdsd.jamopp.model.java.references.Reference handleTypeLiteral(Expression expr) {
		TypeLiteral arr = (TypeLiteral) expr;
		tools.mdsd.jamopp.model.java.references.ReflectiveClassReference result = referencesFactory
				.createReflectiveClassReference();
		tools.mdsd.jamopp.model.java.references.Reference parent = toReferenceConverterFromType.convert(arr.getType());
		parent.setNext(result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleThisExpression(Expression expr) {
		ThisExpression arr = (ThisExpression) expr;
		tools.mdsd.jamopp.model.java.references.SelfReference result = referencesFactory.createSelfReference();
		result.setSelf(literalsFactory.createThis());
		if (arr.getQualifier() != null) {
			tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(result);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleSuperMethodInvocation(Expression expr) {
		SuperMethodInvocation arr = (SuperMethodInvocation) expr;
		tools.mdsd.jamopp.model.java.references.SelfReference partOne = referencesFactory.createSelfReference();
		partOne.setSelf(literalsFactory.createSuper());
		if (arr.getQualifier() != null) {
			tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(partOne);
		}
		MethodCall partTwo = referencesFactory.createMethodCall();
		arr.typeArguments()
				.forEach(obj -> partTwo.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		arr.arguments()
				.forEach(obj -> partTwo.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
		tools.mdsd.jamopp.model.java.members.Method proxy;
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

	private tools.mdsd.jamopp.model.java.references.Reference handleSuperFieldAcces(Expression expr) {
		SuperFieldAccess arr = (SuperFieldAccess) expr;
		tools.mdsd.jamopp.model.java.references.SelfReference partOne = referencesFactory.createSelfReference();
		partOne.setSelf(literalsFactory.createSuper());
		if (arr.getQualifier() != null) {
			tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(partOne);
		}
		IdentifierReference partTwo = toReferenceConverterFromName.convert(arr.getName());
		partOne.setNext(partTwo);
		return partTwo;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleStringLiteral(Expression expr) {
		StringLiteral arr = (StringLiteral) expr;
		tools.mdsd.jamopp.model.java.references.StringReference result = referencesFactory.createStringReference();
		result.setValue(arr.getEscapedValue().substring(1, arr.getEscapedValue().length() - 1));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleParanthesizedExpression(Expression expr) {
		ParenthesizedExpression arr = (ParenthesizedExpression) expr;
		tools.mdsd.jamopp.model.java.expressions.NestedExpression result = expressionsFactory.createNestedExpression();
		result.setExpression(expressionConverterUtility.convert(arr.getExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleSimpleName(Expression expr) {
		return toReferenceConverterFromName.convert((SimpleName) expr);
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleQualifiedName(Expression expr) {
		QualifiedName arr = (QualifiedName) expr;
		IdentifierReference result = toReferenceConverterFromName.convert(arr.getName());
		tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getQualifier());
		parent.setNext(result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleFieldAcces(Expression expr) {
		FieldAccess arr = (FieldAccess) expr;
		tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getExpression());
		IdentifierReference result = toReferenceConverterFromName.convert(arr.getName());
		parent.setNext(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleClassInstanceCreation(Expression expr) {
		ClassInstanceCreation arr = (ClassInstanceCreation) expr;
		tools.mdsd.jamopp.model.java.instantiations.NewConstructorCall result;
		if (arr.getType().isParameterizedType() && ((ParameterizedType) arr.getType()).typeArguments().isEmpty()) {
			result = instantiationsFactory.createNewConstructorCallWithInferredTypeArguments();
		} else {
			result = instantiationsFactory.createNewConstructorCall();
		}
		arr.typeArguments()
				.forEach(obj -> result.getCallTypeArguments().add(typeToTypeArgumentConverter.convert((Type) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
		arr.arguments().forEach(obj -> result.getArguments().add(expressionConverterUtility.convert((Expression) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		if (arr.getAnonymousClassDeclaration() != null) {
			result.setAnonymousClass(toAnonymousClassConverter.convert(arr.getAnonymousClassDeclaration()));
		}
		if (arr.getExpression() != null) {
			tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getExpression());
			parent.setNext(result);
		}
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleArrayInitiliazer(Expression expr) {
		tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationByValuesUntyped result = arraysFactory
				.createArrayInstantiationByValuesUntyped();
		result.setArrayInitializer(toArrayInitialisierConverter.convert((ArrayInitializer) expr));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.references.Reference handleArrayCreation(Expression expr) {
		ArrayCreation arr = (ArrayCreation) expr;
		if (arr.getInitializer() != null) {
			tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationByValuesTyped result = arraysFactory
					.createArrayInstantiationByValuesTyped();
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			utilToArrayDimensionsAndSetConverter.convert(arr.getType(), result);
			result.setArrayInitializer(toArrayInitialisierConverter.convert(arr.getInitializer()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationBySize result = arraysFactory
				.createArrayInstantiationBySize();
		result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
		utilToArrayDimensionsAndSetConverter.convert(arr.getType(), result, arr.dimensions().size());
		arr.dimensions().forEach(obj -> result.getSizes().add(expressionConverterUtility.convert((Expression) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private tools.mdsd.jamopp.model.java.references.Reference handleArrayAcces(Expression expr) {
		ArrayAccess arr = (ArrayAccess) expr;
		tools.mdsd.jamopp.model.java.references.Reference parent = convert(arr.getArray());
		tools.mdsd.jamopp.model.java.arrays.ArraySelector selector = arraysFactory.createArraySelector();
		selector.setPosition(expressionConverterUtility.convert(arr.getIndex()));
		parent.getArraySelectors().add(selector);
		return parent;
	}

}
