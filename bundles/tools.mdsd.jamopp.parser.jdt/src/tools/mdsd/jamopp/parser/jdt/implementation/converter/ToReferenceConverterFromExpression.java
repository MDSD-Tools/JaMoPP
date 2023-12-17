package tools.mdsd.jamopp.parser.jdt.implementation.converter;

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

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;

public class ToReferenceConverterFromExpression
		implements Converter<Expression, org.emftext.language.java.references.Reference> {

	private ExpressionsFactory expressionsFactory;
	private LiteralsFactory literalsFactory;
	private ReferencesFactory referencesFactory;
	private InstantiationsFactory instantiationsFactory;
	private ArraysFactory arraysFactory;
	private UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private UtilLayout layoutInformationConverter;
	private UtilJdtResolver jdtResolverUtility;
	private UtilNamedElement utilNamedElement;
	private Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private Converter<Type, TypeReference> toTypeReferenceConverter;
	private Converter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierConverter;
	private Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;
	private Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter;
	private Converter<SimpleName, IdentifierReference> toReferenceConverterFromName;
	private Converter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation;
	private Converter<Type, org.emftext.language.java.references.Reference> toReferenceConverterFromType;
	private Converter<Type, TypeArgument> typeToTypeArgumentConverter;

	@Inject
	void setMembers(UtilNamedElement utilNamedElement, Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<Type, org.emftext.language.java.references.Reference> toReferenceConverterFromType,
			Converter<SimpleName, IdentifierReference> toReferenceConverterFromName,
			Converter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation,
			Converter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierConverter,
			Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			ReferencesFactory referencesFactory, UtilReferenceWalker utilReferenceWalker,
			LiteralsFactory literalsFactory, UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			InstantiationsFactory instantiationsFactory, ExpressionsFactory expressionsFactory,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			ArraysFactory arraysFactory, Converter<Type, TypeArgument> typeArgumentConverter,
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
		this.toReferenceConverterFromName = toReferenceConverterFromName;
		this.toReferenceConverterFromMethodInvocation = toReferenceConverterFromMethodInvocation;
		this.toReferenceConverterFromType = toReferenceConverterFromType;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.typeToTypeArgumentConverter = typeArgumentConverter;
	}

	@Override
	public org.emftext.language.java.references.Reference convert(Expression expr) {
		if (expr instanceof Annotation) {
			return this.toAnnotationInstanceConverter.convert((Annotation) expr);
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
			return this.toReferenceConverterFromMethodInvocation.convert((MethodInvocation) expr);
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

	private org.emftext.language.java.references.Reference handleTypeLiteral(Expression expr) {
		TypeLiteral arr = (TypeLiteral) expr;
		org.emftext.language.java.references.ReflectiveClassReference result = this.referencesFactory
				.createReflectiveClassReference();
		org.emftext.language.java.references.Reference parent = this.toReferenceConverterFromType
				.convert(arr.getType());
		parent.setNext(result);
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private org.emftext.language.java.references.Reference handleThisExpression(Expression expr) {
		ThisExpression arr = (ThisExpression) expr;
		org.emftext.language.java.references.SelfReference result = this.referencesFactory.createSelfReference();
		result.setSelf(this.literalsFactory.createThis());
		if (arr.getQualifier() != null) {
			org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(result);
		}
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference handleSuperMethodInvocation(Expression expr) {
		SuperMethodInvocation arr = (SuperMethodInvocation) expr;
		org.emftext.language.java.references.SelfReference partOne = this.referencesFactory.createSelfReference();
		partOne.setSelf(this.literalsFactory.createSuper());
		if (arr.getQualifier() != null) {
			org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(partOne);
		}
		org.emftext.language.java.references.MethodCall partTwo = this.referencesFactory.createMethodCall();
		arr.typeArguments().forEach(
				obj -> partTwo.getCallTypeArguments().add(this.typeToTypeArgumentConverter.convert((Type) obj)));
		arr.arguments()
				.forEach(obj -> partTwo.getArguments().add(this.expressionConverterUtility.convert((Expression) obj)));
		org.emftext.language.java.members.Method proxy;
		if (arr.getName().resolveBinding() != null) {
			proxy = this.jdtResolverUtility.getMethod((IMethodBinding) arr.getName().resolveBinding());
		} else {
			proxy = this.jdtResolverUtility.getClassMethod(arr.getName().getIdentifier());
			proxy.setName(arr.getName().getIdentifier());
		}
		this.utilNamedElement.setNameOfElement(arr.getName(), proxy);
		partTwo.setTarget(proxy);
		partOne.setNext(partTwo);
		this.layoutInformationConverter.convertToMinimalLayoutInformation(partTwo, arr);
		return partTwo;
	}

	private org.emftext.language.java.references.Reference handleSuperFieldAcces(Expression expr) {
		SuperFieldAccess arr = (SuperFieldAccess) expr;
		org.emftext.language.java.references.SelfReference partOne = this.referencesFactory.createSelfReference();
		partOne.setSelf(this.literalsFactory.createSuper());
		if (arr.getQualifier() != null) {
			org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
			parent.setNext(partOne);
		}
		org.emftext.language.java.references.IdentifierReference partTwo = this.toReferenceConverterFromName
				.convert(arr.getName());
		partOne.setNext(partTwo);
		return partTwo;
	}

	private org.emftext.language.java.references.Reference handleStringLiteral(Expression expr) {
		StringLiteral arr = (StringLiteral) expr;
		org.emftext.language.java.references.StringReference result = this.referencesFactory.createStringReference();
		result.setValue(arr.getEscapedValue().substring(1, arr.getEscapedValue().length() - 1));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private org.emftext.language.java.references.Reference handleParanthesizedExpression(Expression expr) {
		ParenthesizedExpression arr = (ParenthesizedExpression) expr;
		org.emftext.language.java.expressions.NestedExpression result = this.expressionsFactory
				.createNestedExpression();
		result.setExpression(this.expressionConverterUtility.convert(arr.getExpression()));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private org.emftext.language.java.references.Reference handleSimpleName(Expression expr) {
		return this.toReferenceConverterFromName.convert((SimpleName) expr);
	}

	private org.emftext.language.java.references.Reference handleQualifiedName(Expression expr) {
		QualifiedName arr = (QualifiedName) expr;
		org.emftext.language.java.references.IdentifierReference result = this.toReferenceConverterFromName
				.convert(arr.getName());
		org.emftext.language.java.references.Reference parent = convert(arr.getQualifier());
		parent.setNext(result);
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private org.emftext.language.java.references.Reference handleFieldAcces(Expression expr) {
		FieldAccess arr = (FieldAccess) expr;
		org.emftext.language.java.references.Reference parent = convert(arr.getExpression());
		org.emftext.language.java.references.IdentifierReference result = this.toReferenceConverterFromName
				.convert(arr.getName());
		parent.setNext(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference handleClassInstanceCreation(Expression expr) {
		ClassInstanceCreation arr = (ClassInstanceCreation) expr;
		org.emftext.language.java.instantiations.NewConstructorCall result;
		if (arr.getType().isParameterizedType() && ((ParameterizedType) arr.getType()).typeArguments().isEmpty()) {
			result = this.instantiationsFactory.createNewConstructorCallWithInferredTypeArguments();
		} else {
			result = this.instantiationsFactory.createNewConstructorCall();
		}
		arr.typeArguments().forEach(
				obj -> result.getCallTypeArguments().add(this.typeToTypeArgumentConverter.convert((Type) obj)));
		result.setTypeReference(this.toTypeReferenceConverter.convert(arr.getType()));
		arr.arguments()
				.forEach(obj -> result.getArguments().add(this.expressionConverterUtility.convert((Expression) obj)));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		if (arr.getAnonymousClassDeclaration() != null) {
			result.setAnonymousClass(this.toAnonymousClassConverter.convert(arr.getAnonymousClassDeclaration()));
		}
		if (arr.getExpression() != null) {
			org.emftext.language.java.references.Reference parent = convert(arr.getExpression());
			parent.setNext(result);
		}
		return result;
	}

	private org.emftext.language.java.references.Reference handleArrayInitiliazer(Expression expr) {
		org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped result = this.arraysFactory
				.createArrayInstantiationByValuesUntyped();
		result.setArrayInitializer(this.toArrayInitialisierConverter.convert((ArrayInitializer) expr));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference handleArrayCreation(Expression expr) {
		ArrayCreation arr = (ArrayCreation) expr;
		if (arr.getInitializer() != null) {
			org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped result = this.arraysFactory
					.createArrayInstantiationByValuesTyped();
			result.setTypeReference(this.toTypeReferenceConverter.convert(arr.getType()));
			this.utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(arr.getType(), result);
			result.setArrayInitializer(this.toArrayInitialisierConverter.convert(arr.getInitializer()));
			this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		org.emftext.language.java.arrays.ArrayInstantiationBySize result = this.arraysFactory
				.createArrayInstantiationBySize();
		result.setTypeReference(this.toTypeReferenceConverter.convert(arr.getType()));
		this.utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(arr.getType(), result,
				arr.dimensions().size());
		arr.dimensions()
				.forEach(obj -> result.getSizes().add(this.expressionConverterUtility.convert((Expression) obj)));
		this.layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private org.emftext.language.java.references.Reference handleArrayAcces(Expression expr) {
		ArrayAccess arr = (ArrayAccess) expr;
		org.emftext.language.java.references.Reference parent = convert(arr.getArray());
		org.emftext.language.java.arrays.ArraySelector selector = this.arraysFactory.createArraySelector();
		selector.setPosition(this.expressionConverterUtility.convert(arr.getIndex()));
		parent.getArraySelectors().add(selector);
		return parent;
	}

}
