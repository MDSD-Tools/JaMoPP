/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
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

class UtilReferenceConverter {

	private final ExpressionsFactory expressionsFactory;
	private final LiteralsFactory literalsFactory;
	private final ReferencesFactory referencesFactory;
	private final InstantiationsFactory instantiationsFactory;
	private final ArraysFactory arraysFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilExpressionConverter expressionConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToArrayInitialisierConverter toArrayInitialisierConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;
	private final ToAnonymousClassConverter toAnonymousClassConverter;

	@Inject
	UtilReferenceConverter(UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			UtilExpressionConverter expressionConverterUtility, UtilNamedElement utilNamedElement,
			ToTypeReferenceConverter toTypeReferenceConverter,
			ToArrayInitialisierConverter toArrayInitialisierConverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter,
			ToAnonymousClassConverter toAnonymousClassConverter, ArraysFactory arraysFactory,
			InstantiationsFactory instantiationsFactory, ReferencesFactory referencesFactory,
			LiteralsFactory literalsFactory, ExpressionsFactory expressionsFactory) {
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
	}

	org.emftext.language.java.references.Reference convertToReference(Expression expr) {
		return walkUp(internalConvertToReference(expr));
	}

	private org.emftext.language.java.references.Reference walkUp(org.emftext.language.java.references.Reference ref) {
		org.emftext.language.java.references.Reference result = ref;
		org.emftext.language.java.references.Reference parent = result.getPrevious();
		while (parent != null) {
			result = parent;
			parent = result.getPrevious();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference internalConvertToReference(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_ACCESS) {
			ArrayAccess arr = (ArrayAccess) expr;
			org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getArray());
			org.emftext.language.java.arrays.ArraySelector selector = arraysFactory.createArraySelector();
			selector.setPosition(expressionConverterUtility.convertToExpression(arr.getIndex()));
			parent.getArraySelectors().add(selector);
			return parent;
		}
		if (expr.getNodeType() == ASTNode.ARRAY_CREATION) {
			ArrayCreation arr = (ArrayCreation) expr;
			if (arr.getInitializer() != null) {
				org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped result = arraysFactory
						.createArrayInstantiationByValuesTyped();
				result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(arr.getType()));
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr.getType(), result);
				result.setArrayInitializer(
						toArrayInitialisierConverter.convertToArrayInitializer(arr.getInitializer()));
				layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
				return result;
			}
			org.emftext.language.java.arrays.ArrayInstantiationBySize result = arraysFactory
					.createArrayInstantiationBySize();
			result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(arr.getType()));
			toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr.getType(), result, arr.dimensions().size());
			arr.dimensions().forEach(
					obj -> result.getSizes().add(expressionConverterUtility.convertToExpression((Expression) obj)));
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
			result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(arr.getType()));
			arr.arguments().forEach(
					obj -> result.getArguments().add(expressionConverterUtility.convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			if (arr.getAnonymousClassDeclaration() != null) {
				result.setAnonymousClass(
						toAnonymousClassConverter.convertToAnonymousClass(arr.getAnonymousClassDeclaration()));
			}
			if (arr.getExpression() != null) {
				org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getExpression());
				parent.setNext(result);
			}
			return result;
		}
		if (expr.getNodeType() == ASTNode.FIELD_ACCESS) {
			FieldAccess arr = (FieldAccess) expr;
			org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getExpression());
			org.emftext.language.java.references.IdentifierReference result = convertToIdentifierReference(
					arr.getName());
			parent.setNext(result);
			return result;
		}
		if (expr.getNodeType() == ASTNode.METHOD_INVOCATION) {
			return convertToMethodCall((MethodInvocation) expr);
		}
		if (expr.getNodeType() == ASTNode.QUALIFIED_NAME) {
			QualifiedName arr = (QualifiedName) expr;
			org.emftext.language.java.references.IdentifierReference result = convertToIdentifierReference(
					arr.getName());
			org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getQualifier());
			parent.setNext(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.SIMPLE_NAME) {
			return convertToIdentifierReference((SimpleName) expr);
		}
		if (expr.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION) {
			ParenthesizedExpression arr = (ParenthesizedExpression) expr;
			org.emftext.language.java.expressions.NestedExpression result = expressionsFactory.createNestedExpression();
			result.setExpression(expressionConverterUtility.convertToExpression(arr.getExpression()));
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
				org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getQualifier());
				parent.setNext(partOne);
			}
			org.emftext.language.java.references.IdentifierReference partTwo = convertToIdentifierReference(
					arr.getName());
			partOne.setNext(partTwo);
			return partTwo;
		}
		if (expr.getNodeType() == ASTNode.SUPER_METHOD_INVOCATION) {
			SuperMethodInvocation arr = (SuperMethodInvocation) expr;
			org.emftext.language.java.references.SelfReference partOne = referencesFactory.createSelfReference();
			partOne.setSelf(literalsFactory.createSuper());
			if (arr.getQualifier() != null) {
				org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getQualifier());
				parent.setNext(partOne);
			}
			org.emftext.language.java.references.MethodCall partTwo = referencesFactory.createMethodCall();
			arr.typeArguments().forEach(obj -> partTwo.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			arr.arguments().forEach(obj -> partTwo.getArguments()
					.add(expressionConverterUtility.convertToExpression((Expression) obj)));
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
				org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getQualifier());
				parent.setNext(result);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		if (expr.getNodeType() == ASTNode.TYPE_LITERAL) {
			TypeLiteral arr = (TypeLiteral) expr;
			org.emftext.language.java.references.ReflectiveClassReference result = referencesFactory
					.createReflectiveClassReference();
			org.emftext.language.java.references.Reference parent = internalConvertToReference(arr.getType());
			parent.setNext(result);
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.MethodCall convertToMethodCall(MethodInvocation arr) {
		org.emftext.language.java.references.Reference parent = null;
		if (arr.getExpression() != null) {
			parent = internalConvertToReference(arr.getExpression());
		}
		org.emftext.language.java.references.MethodCall result = referencesFactory.createMethodCall();
		arr.typeArguments().forEach(
				obj -> result.getCallTypeArguments().add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
		arr.arguments().forEach(
				obj -> result.getArguments().add(expressionConverterUtility.convertToExpression((Expression) obj)));
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

	private org.emftext.language.java.references.IdentifierReference convertToIdentifierReference(Name name) {
		if (name.isSimpleName()) {
			return convertToIdentifierReference((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		org.emftext.language.java.references.IdentifierReference parent = convertToIdentifierReference(
				qualifiedName.getQualifier());
		org.emftext.language.java.references.IdentifierReference child = convertToIdentifierReference(
				qualifiedName.getName());
		parent.setNext(child);
		return child;
	}

	private org.emftext.language.java.references.IdentifierReference convertToIdentifierReference(SimpleName name) {
		org.emftext.language.java.references.IdentifierReference result = referencesFactory.createIdentifierReference();
		IBinding b = name.resolveBinding();
		org.emftext.language.java.references.ReferenceableElement target = null;
		if (b == null || b.isRecovered()) {
			target = jdtResolverUtility.getReferenceableElementByNameMatching(name.getIdentifier());
		} else if (b instanceof ITypeBinding) {
			target = jdtResolverUtility.getClassifier((ITypeBinding) b);
		} else if (b instanceof IVariableBinding) {
			target = jdtResolverUtility.getReferencableElement((IVariableBinding) b);
		} else if (b instanceof IMethodBinding) {
			target = jdtResolverUtility.getMethod((IMethodBinding) b);
		} else if (b instanceof IPackageBinding) {
			target = jdtResolverUtility.getPackage((IPackageBinding) b);
		} else {
			target = jdtResolverUtility.getReferenceableElementByNameMatching(name.getIdentifier());
		}
		target.setName(name.getIdentifier());
		result.setTarget(target);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, name);
		return result;
	}

	org.emftext.language.java.references.Reference convertToReference(Type t) {
		return walkUp(internalConvertToReference(t));
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference internalConvertToReference(Type t) {
		if (t.isNameQualifiedType()) {
			NameQualifiedType nqType = (NameQualifiedType) t;
			org.emftext.language.java.references.IdentifierReference parent = convertToIdentifierReference(
					nqType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = convertToIdentifierReference(
					nqType.getName());
			parent.setNext(child);
			nqType.annotations().forEach(obj -> child.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(child, nqType);
			return child;
		}
		if (t.isQualifiedType()) {
			QualifiedType qType = (QualifiedType) t;
			org.emftext.language.java.references.Reference parent = internalConvertToReference(qType.getQualifier());
			org.emftext.language.java.references.IdentifierReference child = convertToIdentifierReference(
					qType.getName());
			qType.annotations().forEach(obj -> child.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			parent.setNext(child);
			layoutInformationConverter.convertToMinimalLayoutInformation(child, qType);
			return child;
		}
		if (t.isSimpleType()) {
			SimpleType sType = (SimpleType) t;
			org.emftext.language.java.references.IdentifierReference result = convertToIdentifierReference(
					sType.getName());
			sType.annotations().forEach(obj -> result.getAnnotations()
					.add(toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, sType);
			return result;
		}
		if (t.isPrimitiveType()) {
			org.emftext.language.java.types.TypeReference typeRef = toTypeReferenceConverter.convertToTypeReference(t);
			org.emftext.language.java.references.PrimitiveTypeReference temp = referencesFactory
					.createPrimitiveTypeReference();
			temp.setPrimitiveType((org.emftext.language.java.types.PrimitiveType) typeRef);
			temp.getLayoutInformations().addAll(typeRef.getLayoutInformations());
			return temp;
		}
		if (t.isArrayType()) {
			ArrayType arr = (ArrayType) t;
			org.emftext.language.java.references.Reference result = internalConvertToReference(arr.getElementType());
			if (arr.getElementType().isPrimitiveType()) {
				org.emftext.language.java.references.PrimitiveTypeReference primRef = (org.emftext.language.java.references.PrimitiveTypeReference) result;
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr, primRef);
			} else {
				org.emftext.language.java.references.IdentifierReference idRef = (org.emftext.language.java.references.IdentifierReference) result;
				toTypeReferenceConverter.convertToArrayDimensionsAndSet(arr, idRef);
			}
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			return result;
		}
		return null;
	}

	org.emftext.language.java.references.Reference convertToReference(Statement st) {
		return walkUp(internalConvertToReference(st));
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.references.Reference internalConvertToReference(Statement st) {
		if (st.getNodeType() == ASTNode.CONSTRUCTOR_INVOCATION) {
			ConstructorInvocation invoc = (ConstructorInvocation) st;
			org.emftext.language.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setCallTarget(literalsFactory.createThis());
			invoc.arguments().forEach(
					obj -> result.getArguments().add(expressionConverterUtility.convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			return result;
		}
		if (st.getNodeType() == ASTNode.SUPER_CONSTRUCTOR_INVOCATION) {
			SuperConstructorInvocation invoc = (SuperConstructorInvocation) st;
			org.emftext.language.java.instantiations.ExplicitConstructorCall result = instantiationsFactory
					.createExplicitConstructorCall();
			invoc.typeArguments().forEach(obj -> result.getCallTypeArguments()
					.add(toTypeReferenceConverter.convertToTypeArgument((Type) obj)));
			result.setCallTarget(literalsFactory.createSuper());
			invoc.arguments().forEach(
					obj -> result.getArguments().add(expressionConverterUtility.convertToExpression((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, invoc);
			if (invoc.getExpression() != null) {
				org.emftext.language.java.references.Reference parent = internalConvertToReference(
						invoc.getExpression());
				parent.setNext(result);
			}
			return result;
		}
		return null;
	}
}
