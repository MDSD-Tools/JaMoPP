package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

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
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToReferenceConverterFromExpression implements Converter<Expression, Reference> {

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
	private Converter<Type, Reference> toReferenceConverterFromType;
	private Converter<Type, TypeArgument> typeToTypeArgumentConverter;
	private final Map<Integer, Function<Expression, Reference>> mappings;

	public ToReferenceConverterFromExpression() {
		mappings = new HashMap<>();
		mappings.put(ASTNode.ARRAY_ACCESS, this::handleArrayAcces);
		mappings.put(ASTNode.ARRAY_CREATION, this::handleArrayCreation);
		mappings.put(ASTNode.ARRAY_INITIALIZER, this::handleArrayInitiliazer);
		mappings.put(ASTNode.CLASS_INSTANCE_CREATION, this::handleClassInstanceCreation);
		mappings.put(ASTNode.FIELD_ACCESS, this::handleFieldAcces);
		mappings.put(ASTNode.METHOD_INVOCATION,
				expr -> toReferenceConverterFromMethodInvocation.convert((MethodInvocation) expr));
		mappings.put(ASTNode.QUALIFIED_NAME, this::handleQualifiedName);
		mappings.put(ASTNode.SIMPLE_NAME, this::handleSimpleName);
		mappings.put(ASTNode.PARENTHESIZED_EXPRESSION, this::handleParanthesizedExpression);
		mappings.put(ASTNode.STRING_LITERAL, this::handleStringLiteral);
		mappings.put(ASTNode.SUPER_FIELD_ACCESS, this::handleSuperFieldAcces);
		mappings.put(ASTNode.SUPER_METHOD_INVOCATION, this::handleSuperMethodInvocation);
		mappings.put(ASTNode.THIS_EXPRESSION, this::handleThisExpression);
		mappings.put(ASTNode.TYPE_LITERAL, this::handleTypeLiteral);
	}

	@Inject
	public void setMembers(final UtilNamedElement utilNamedElement,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<Type, Reference> toReferenceConverterFromType,
			final Converter<SimpleName, IdentifierReference> toReferenceConverterFromName,
			final Converter<MethodInvocation, MethodCall> toReferenceConverterFromMethodInvocation,
			final Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> toArrayInitialisierConverter,
			final Converter<AnonymousClassDeclaration, AnonymousClass> toAnonymousClassConverter,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter,
			final ReferencesFactory referencesFactory, final LiteralsFactory literalsFactory,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final InstantiationsFactory instantiationsFactory, final ExpressionsFactory expressionsFactory,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final ArraysFactory arraysFactory, final Converter<Type, TypeArgument> typeToTypeArgumentConverter,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
		this.typeToTypeArgumentConverter = typeToTypeArgumentConverter;
	}

	@Override
	public Reference convert(final Expression expression) {

		Reference result = null;
		if (expression instanceof Annotation) {
			result = toAnnotationInstanceConverter.convert((Annotation) expression);
		} else {
			for (final Entry<Integer, Function<Expression, Reference>> entry : mappings.entrySet()) {
				if (expression.getNodeType() == entry.getKey()) {
					result = entry.getValue().apply(expression);
					break;
				}
			}
		}
		return result;
	}

	private Reference handleTypeLiteral(final Expression expr) {
		final TypeLiteral arr = (TypeLiteral) expr;
		final tools.mdsd.jamopp.model.java.references.ReflectiveClassReference result = referencesFactory
				.createReflectiveClassReference();
		final Reference parent = toReferenceConverterFromType.convert(arr.getType());
		parent.setNext(result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private Reference handleThisExpression(final Expression expr) {
		final ThisExpression arr = (ThisExpression) expr;
		final tools.mdsd.jamopp.model.java.references.SelfReference result = referencesFactory.createSelfReference();
		result.setSelf(literalsFactory.createThis());
		if (arr.getQualifier() != null) {
			final Reference parent = convert(arr.getQualifier());
			parent.setNext(result);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Reference handleSuperMethodInvocation(final Expression expr) {
		final SuperMethodInvocation arr = (SuperMethodInvocation) expr;
		final tools.mdsd.jamopp.model.java.references.SelfReference partOne = referencesFactory.createSelfReference();
		partOne.setSelf(literalsFactory.createSuper());
		if (arr.getQualifier() != null) {
			final Reference parent = convert(arr.getQualifier());
			parent.setNext(partOne);
		}
		final MethodCall partTwo = referencesFactory.createMethodCall();
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

	private Reference handleSuperFieldAcces(final Expression expr) {
		final SuperFieldAccess arr = (SuperFieldAccess) expr;
		final tools.mdsd.jamopp.model.java.references.SelfReference partOne = referencesFactory.createSelfReference();
		partOne.setSelf(literalsFactory.createSuper());
		if (arr.getQualifier() != null) {
			final Reference parent = convert(arr.getQualifier());
			parent.setNext(partOne);
		}
		final IdentifierReference partTwo = toReferenceConverterFromName.convert(arr.getName());
		partOne.setNext(partTwo);
		return partTwo;
	}

	private Reference handleStringLiteral(final Expression expr) {
		final StringLiteral arr = (StringLiteral) expr;
		final tools.mdsd.jamopp.model.java.references.StringReference result = referencesFactory
				.createStringReference();
		result.setValue(arr.getEscapedValue().substring(1, arr.getEscapedValue().length() - 1));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private Reference handleParanthesizedExpression(final Expression expr) {
		final ParenthesizedExpression arr = (ParenthesizedExpression) expr;
		final tools.mdsd.jamopp.model.java.expressions.NestedExpression result = expressionsFactory
				.createNestedExpression();
		result.setExpression(expressionConverterUtility.convert(arr.getExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private Reference handleSimpleName(final Expression expr) {
		return toReferenceConverterFromName.convert((SimpleName) expr);
	}

	private Reference handleQualifiedName(final Expression expr) {
		final QualifiedName arr = (QualifiedName) expr;
		final IdentifierReference result = toReferenceConverterFromName.convert(arr.getName());
		final Reference parent = convert(arr.getQualifier());
		parent.setNext(result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

	private Reference handleFieldAcces(final Expression expr) {
		final FieldAccess arr = (FieldAccess) expr;
		final Reference parent = convert(arr.getExpression());
		final IdentifierReference result = toReferenceConverterFromName.convert(arr.getName());
		parent.setNext(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Reference handleClassInstanceCreation(final Expression expr) {
		final ClassInstanceCreation arr = (ClassInstanceCreation) expr;
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
			final Reference parent = convert(arr.getExpression());
			parent.setNext(result);
		}
		return result;
	}

	private Reference handleArrayInitiliazer(final Expression expr) {
		final tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationByValuesUntyped result = arraysFactory
				.createArrayInstantiationByValuesUntyped();
		result.setArrayInitializer(toArrayInitialisierConverter.convert((ArrayInitializer) expr));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Reference handleArrayCreation(final Expression expr) {
		final ArrayCreation arr = (ArrayCreation) expr;
		Reference reference;
		if (arr.getInitializer() != null) {
			final tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationByValuesTyped result = arraysFactory
					.createArrayInstantiationByValuesTyped();
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			utilToArrayDimensionsAndSetConverter.convert(arr.getType(), result);
			result.setArrayInitializer(toArrayInitialisierConverter.convert(arr.getInitializer()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			reference = result;
		} else {
			final tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationBySize result = arraysFactory
					.createArrayInstantiationBySize();
			result.setTypeReference(toTypeReferenceConverter.convert(arr.getType()));
			utilToArrayDimensionsAndSetConverter.convert(arr.getType(), result, arr.dimensions().size());
			arr.dimensions()
					.forEach(obj -> result.getSizes().add(expressionConverterUtility.convert((Expression) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, arr);
			reference = result;
		}
		return reference;
	}

	private Reference handleArrayAcces(final Expression expr) {
		final ArrayAccess arr = (ArrayAccess) expr;
		final Reference parent = convert(arr.getArray());
		final tools.mdsd.jamopp.model.java.arrays.ArraySelector selector = arraysFactory.createArraySelector();
		selector.setPosition(expressionConverterUtility.convert(arr.getIndex()));
		parent.getArraySelectors().add(selector);
		return parent;
	}

}
