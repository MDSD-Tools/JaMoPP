package jamopp.parser.jdt.injector;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.commons.layout.LayoutFactory;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.classifiers.ClassifiersFactory;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.PrimaryExpression;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.MembersFactory;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.operators.AdditiveOperator;
import org.emftext.language.java.operators.AssignmentOperator;
import org.emftext.language.java.operators.EqualityOperator;
import org.emftext.language.java.operators.MultiplicativeOperator;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.operators.RelationOperator;
import org.emftext.language.java.operators.ShiftOperator;
import org.emftext.language.java.operators.UnaryOperator;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;
import org.emftext.language.java.variables.AdditionalLocalVariable;
import org.emftext.language.java.variables.VariablesFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

import jamopp.parser.jdt.converter.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilToSwitchCasesAndSetConverterImpl;
import jamopp.parser.jdt.converter.implementation.BindingToAnnotationAttributeSettingConverter;
import jamopp.parser.jdt.converter.implementation.BindingToAnnotationInstanceConverter;
import jamopp.parser.jdt.converter.implementation.BindingToConstructorConverter;
import jamopp.parser.jdt.converter.implementation.BindingToEnumConstantConverter;
import jamopp.parser.jdt.converter.implementation.BindingToFieldConverter;
import jamopp.parser.jdt.converter.implementation.BindingToInternalReferenceConverter;
import jamopp.parser.jdt.converter.implementation.BindingToMethodConverter;
import jamopp.parser.jdt.converter.implementation.BindingToModuleConverter;
import jamopp.parser.jdt.converter.implementation.BindingToNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.converter.implementation.BindingToPackageConverter;
import jamopp.parser.jdt.converter.implementation.BindingToTypeParameterConverter;
import jamopp.parser.jdt.converter.implementation.BlockToBlockConverterImpl;
import jamopp.parser.jdt.converter.implementation.ObjectToAnnotationValueConverter;
import jamopp.parser.jdt.converter.implementation.ObjectToPrimaryExpressionConverter;
import jamopp.parser.jdt.converter.implementation.StatementToStatementConverterImpl;
import jamopp.parser.jdt.converter.implementation.SwitchToSwitchConverter;
import jamopp.parser.jdt.converter.implementation.ToAdditionalFieldConverter;
import jamopp.parser.jdt.converter.implementation.ToAdditionalLocalVariableConverter;
import jamopp.parser.jdt.converter.implementation.ToAdditiveExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToAdditiveOperatorConverter;
import jamopp.parser.jdt.converter.implementation.ToAnnotationInstanceConverter;
import jamopp.parser.jdt.converter.implementation.ToAnnotationValueConverter;
import jamopp.parser.jdt.converter.implementation.ToAnonymousClassConverter;
import jamopp.parser.jdt.converter.implementation.ToArrayDimensionConverter;
import jamopp.parser.jdt.converter.implementation.ToArrayInitialisierConverter;
import jamopp.parser.jdt.converter.implementation.ToAssignmentConverter;
import jamopp.parser.jdt.converter.implementation.ToBlockConverter;
import jamopp.parser.jdt.converter.implementation.ToCatchblockConverter;
import jamopp.parser.jdt.converter.implementation.ToClassMemberConverter;
import jamopp.parser.jdt.converter.implementation.ToClassMethodOrConstructorConverter;
import jamopp.parser.jdt.converter.implementation.ToClassOrInterfaceConverter;
import jamopp.parser.jdt.converter.implementation.ToClassifierOrNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.converter.implementation.ToClassifierReferenceConverter;
import jamopp.parser.jdt.converter.implementation.ToConcreteClassifierConverterImpl;
import jamopp.parser.jdt.converter.implementation.ToConditionalExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToEnumConstantConverter;
import jamopp.parser.jdt.converter.implementation.ToEnumConverter;
import jamopp.parser.jdt.converter.implementation.ToEqualityExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToEqualityOperatorConverter;
import jamopp.parser.jdt.converter.implementation.ToExpressionConverterImpl;
import jamopp.parser.jdt.converter.implementation.ToFieldConverter;
import jamopp.parser.jdt.converter.implementation.ToInterfaceMemberConverter;
import jamopp.parser.jdt.converter.implementation.ToInterfaceMethodConverter;
import jamopp.parser.jdt.converter.implementation.ToInterfaceMethodOrConstructorConverter;
import jamopp.parser.jdt.converter.implementation.ToLocalVariableConverter;
import jamopp.parser.jdt.converter.implementation.ToMethodReferenceExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToModifierConverter;
import jamopp.parser.jdt.converter.implementation.ToModifierOrAnnotationInstanceConverter;
import jamopp.parser.jdt.converter.implementation.ToModifiersConverter;
import jamopp.parser.jdt.converter.implementation.ToMultiplicativeExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToMultiplicativeOperatorConverter;
import jamopp.parser.jdt.converter.implementation.ToNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.converter.implementation.ToNumberLiteralConverter;
import jamopp.parser.jdt.converter.implementation.ToOrdinaryParameterConverter;
import jamopp.parser.jdt.converter.implementation.ToParameterConverter;
import jamopp.parser.jdt.converter.implementation.ToPrimaryExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToReceiverParameterConverter;
import jamopp.parser.jdt.converter.implementation.ToReferenceConverterFromExpression;
import jamopp.parser.jdt.converter.implementation.ToReferenceConverterFromMethodInvocation;
import jamopp.parser.jdt.converter.implementation.ToReferenceConverterFromName;
import jamopp.parser.jdt.converter.implementation.ToReferenceConverterFromStatement;
import jamopp.parser.jdt.converter.implementation.ToReferenceConverterFromType;
import jamopp.parser.jdt.converter.implementation.ToRelationExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToRelationOperatorConverter;
import jamopp.parser.jdt.converter.implementation.ToShiftExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToShiftOperatorConverter;
import jamopp.parser.jdt.converter.implementation.ToSwitchCaseConverter;
import jamopp.parser.jdt.converter.implementation.ToTypeArgumentConverter;
import jamopp.parser.jdt.converter.implementation.ToTypeParameterConverter;
import jamopp.parser.jdt.converter.implementation.ToTypeReferenceConverter;
import jamopp.parser.jdt.converter.implementation.ToTypeReferencesConverter;
import jamopp.parser.jdt.converter.implementation.ToUnaryExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToUnaryOperatorConverter;
import jamopp.parser.jdt.converter.implementation.TypeToTypeArgumentConverter;
import jamopp.parser.jdt.converter.interfaces.BlockToBlockConverter;
import jamopp.parser.jdt.converter.interfaces.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;

public class InjectorGuice extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ToExpressionConverter.class).to(ToExpressionConverterImpl.class);
		bind(BlockToBlockConverter.class).to(BlockToBlockConverterImpl.class);
		bind(StatementToStatementConverter.class).to(StatementToStatementConverterImpl.class);
		bind(ToConcreteClassifierConverter.class).to(ToConcreteClassifierConverterImpl.class);
		bind(UtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverterImpl.class);

		bind(new TypeLiteral<ToConverter<IMemberValuePairBinding, AnnotationAttributeSetting>>() {
		}).to(BindingToAnnotationAttributeSettingConverter.class);
		bind(new TypeLiteral<ToConverter<IAnnotationBinding, AnnotationInstance>>() {
		}).to(BindingToAnnotationInstanceConverter.class);
		bind(new TypeLiteral<ToConverter<IMethodBinding, Constructor>>() {
		}).to(BindingToConstructorConverter.class);
		bind(new TypeLiteral<ToConverter<IVariableBinding, EnumConstant>>() {
		}).to(BindingToEnumConstantConverter.class);
		bind(new TypeLiteral<ToConverter<IVariableBinding, Field>>() {
		}).to(BindingToFieldConverter.class);
		bind(new TypeLiteral<ToConverter<ITypeBinding, Reference>>() {
		}).to(BindingToInternalReferenceConverter.class);
		bind(new TypeLiteral<ToConverter<IMethodBinding, Method>>() {
		}).to(BindingToMethodConverter.class);
		bind(new TypeLiteral<ToConverter<IModuleBinding, org.emftext.language.java.containers.Module>>() {
		}).to(BindingToModuleConverter.class);
		bind(new TypeLiteral<ToConverter<ITypeBinding, NamespaceClassifierReference>>() {
		}).to(BindingToNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<ToConverter<IPackageBinding, org.emftext.language.java.containers.Package>>() {
		}).to(BindingToPackageConverter.class);
		bind(new TypeLiteral<ToConverter<ITypeBinding, TypeParameter>>() {
		}).to(BindingToTypeParameterConverter.class);
		bind(new TypeLiteral<ToConverter<Block, org.emftext.language.java.statements.Block>>() {
		}).to(BlockToBlockConverterImpl.class);
		bind(new TypeLiteral<ToConverter<Object, AnnotationValue>>() {
		}).to(ObjectToAnnotationValueConverter.class);
		bind(new TypeLiteral<ToConverter<Object, PrimaryExpression>>() {
		}).to(ObjectToPrimaryExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<Statement, org.emftext.language.java.statements.Statement>>() {
		}).to(StatementToStatementConverterImpl.class);
		bind(new TypeLiteral<ToConverter<SwitchStatement, Switch>>() {
		}).to(SwitchToSwitchConverter.class);
		bind(new TypeLiteral<ToConverter<VariableDeclarationFragment, AdditionalField>>() {
		}).to(ToAdditionalFieldConverter.class);
		bind(new TypeLiteral<ToConverter<VariableDeclarationFragment, AdditionalLocalVariable>>() {
		}).to(ToAdditionalLocalVariableConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression, AdditiveExpression>>() {
		}).to(ToAdditiveExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression.Operator, AdditiveOperator>>() {
		}).to(ToAdditiveOperatorConverter.class);
		bind(new TypeLiteral<ToConverter<Annotation, AnnotationInstance>>() {
		}).to(ToAnnotationInstanceConverter.class);
		bind(new TypeLiteral<ToConverter<Expression, AnnotationValue>>() {
		}).to(ToAnnotationValueConverter.class);
		bind(new TypeLiteral<ToConverter<AnonymousClassDeclaration, AnonymousClass>>() {
		}).to(ToAnonymousClassConverter.class);
		bind(new TypeLiteral<ToConverter<Dimension, ArrayDimension>>() {
		}).to(ToArrayDimensionConverter.class);
		bind(new TypeLiteral<ToConverter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer>>() {
		}).to(ToArrayInitialisierConverter.class);
		bind(new TypeLiteral<ToConverter<Assignment.Operator, AssignmentOperator>>() {
		}).to(ToAssignmentConverter.class);
		bind(new TypeLiteral<ToConverter<org.eclipse.jdt.core.dom.Initializer, org.emftext.language.java.statements.Block>>() {
		}).to(ToBlockConverter.class);
		bind(new TypeLiteral<ToConverter<CatchClause, CatchBlock>>() {
		}).to(ToCatchblockConverter.class);
		bind(new TypeLiteral<ToConverter<Name, TypeReference>>() {
		}).to(ToClassifierOrNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<ToConverter<SimpleName, ClassifierReference>>() {
		}).to(ToClassifierReferenceConverter.class);
		bind(new TypeLiteral<ToConverter<BodyDeclaration, Member>>() {
		}).to(ToClassMemberConverter.class);
		bind(new TypeLiteral<ToConverter<MethodDeclaration, Member>>() {
		}).to(ToClassMethodOrConstructorConverter.class);
		bind(new TypeLiteral<ToConverter<TypeDeclaration, ConcreteClassifier>>() {
		}).to(ToClassOrInterfaceConverter.class);
		bind(new TypeLiteral<ToConverter<AbstractTypeDeclaration, ConcreteClassifier>>() {
		}).to(ToConcreteClassifierConverterImpl.class);
		bind(new TypeLiteral<ToConverter<org.eclipse.jdt.core.dom.ConditionalExpression, ConditionalExpression>>() {
		}).to(ToConditionalExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<EnumConstantDeclaration, EnumConstant>>() {
		}).to(ToEnumConstantConverter.class);
		bind(new TypeLiteral<ToConverter<EnumDeclaration, Enumeration>>() {
		}).to(ToEnumConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression, EqualityExpression>>() {
		}).to(ToEqualityExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression.Operator, EqualityOperator>>() {
		}).to(ToEqualityOperatorConverter.class);
		bind(new TypeLiteral<ToConverter<Expression, org.emftext.language.java.expressions.Expression>>() {
		}).to(ToExpressionConverterImpl.class);
		bind(new TypeLiteral<ToConverter<FieldDeclaration, Field>>() {
		}).to(ToFieldConverter.class);
		//bind(new TypeLiteral<ToConverter<BodyDeclaration, Member>>() {
		//}).to(ToInterfaceMemberConverter.class);
		bind(new TypeLiteral<ToConverter<AnnotationTypeMemberDeclaration, InterfaceMethod>>() {
		}).to(ToInterfaceMethodConverter.class);
		//bind(new TypeLiteral<ToConverter<MethodDeclaration, Member>>() {
		//}).to(ToInterfaceMethodOrConstructorConverter.class);
		bind(new TypeLiteral<ToConverter<VariableDeclarationExpression, org.emftext.language.java.variables.LocalVariable>>() {
		}).to(ToLocalVariableConverter.class);
		bind(new TypeLiteral<ToConverter<MethodReference, MethodReferenceExpression>>() {
		}).to(ToMethodReferenceExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<Modifier, org.emftext.language.java.modifiers.Modifier>>() {
		}).to(ToModifierConverter.class);
		bind(new TypeLiteral<ToConverter<IExtendedModifier, AnnotationInstanceOrModifier>>() {
		}).to(ToModifierOrAnnotationInstanceConverter.class);
		bind(new TypeLiteral<ToConverter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>>>() {
		}).to(ToModifiersConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression, MultiplicativeExpression>>() {
		}).to(ToMultiplicativeExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression.Operator, MultiplicativeOperator>>() {
		}).to(ToMultiplicativeOperatorConverter.class);
		bind(new TypeLiteral<ToConverter<TypeReference, NamespaceClassifierReference>>() {
		}).to(ToNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<ToConverter<NumberLiteral, org.emftext.language.java.literals.Literal>>() {
		}).to(ToNumberLiteralConverter.class);
		bind(new TypeLiteral<ToConverter<SingleVariableDeclaration, OrdinaryParameter>>() {
		}).to(ToOrdinaryParameterConverter.class);
		bind(new TypeLiteral<ToConverter<SingleVariableDeclaration, Parameter>>() {
		}).to(ToParameterConverter.class);
		bind(new TypeLiteral<ToConverter<Expression, PrimaryExpression>>() {
		}).to(ToPrimaryExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<MethodDeclaration, ReceiverParameter>>() {
		}).to(ToReceiverParameterConverter.class);
		bind(new TypeLiteral<ToConverter<Expression, org.emftext.language.java.references.Reference>>() {
		}).to(ToReferenceConverterFromExpression.class);
		bind(new TypeLiteral<ToConverter<MethodInvocation, MethodCall>>() {
		}).to(ToReferenceConverterFromMethodInvocation.class);
		bind(new TypeLiteral<ToConverter<SimpleName, IdentifierReference>>() {
		}).to(ToReferenceConverterFromName.class);
		bind(new TypeLiteral<ToConverter<Statement, org.emftext.language.java.references.Reference>>() {
		}).to(ToReferenceConverterFromStatement.class);
		bind(new TypeLiteral<ToConverter<Type, Reference>>() {
		}).to(ToReferenceConverterFromType.class);
		bind(new TypeLiteral<ToConverter<InfixExpression, RelationExpression>>() {
		}).to(ToRelationExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression.Operator, RelationOperator>>() {
		}).to(ToRelationOperatorConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression, ShiftExpression>>() {
		}).to(ToShiftExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<InfixExpression.Operator, ShiftOperator>>() {
		}).to(ToShiftOperatorConverter.class);
		bind(new TypeLiteral<ToConverter<SwitchCase, org.emftext.language.java.statements.SwitchCase>>() {
		}).to(ToSwitchCaseConverter.class);
		bind(new TypeLiteral<ToConverter<ITypeBinding, TypeArgument>>() {
		}).to(ToTypeArgumentConverter.class);
		bind(new TypeLiteral<ToConverter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter>>() {
		}).to(ToTypeParameterConverter.class);
		bind(new TypeLiteral<ToConverter<Type, TypeReference>>() {
		}).to(ToTypeReferenceConverter.class);
		bind(new TypeLiteral<ToConverter<ITypeBinding, List<TypeReference>>>() {
		}).to(ToTypeReferencesConverter.class);
		bind(new TypeLiteral<ToConverter<PrefixExpression, UnaryExpression>>() {
		}).to(ToUnaryExpressionConverter.class);
		bind(new TypeLiteral<ToConverter<PrefixExpression.Operator, UnaryOperator>>() {
		}).to(ToUnaryOperatorConverter.class);
		bind(new TypeLiteral<ToConverter<Type, TypeArgument>>() {
		}).to(TypeToTypeArgumentConverter.class);

	}

	@Provides
	ImportsFactory provideImportsFactory() {
		return ImportsFactory.eINSTANCE;
	}

	@Provides
	ModifiersFactory provideModifiersFactory() {
		return ModifiersFactory.eINSTANCE;
	}

	@Provides
	ExpressionsFactory provideExpressionsFactory() {
		return ExpressionsFactory.eINSTANCE;
	}

	@Provides
	LiteralsFactory provideLiteralsFactory() {
		return LiteralsFactory.eINSTANCE;
	}

	@Provides
	OperatorsFactory provideOperatorsFactory() {
		return OperatorsFactory.eINSTANCE;
	}

	@Provides
	LayoutFactory provideLayoutFactory() {
		return LayoutFactory.eINSTANCE;
	}

	@Provides
	AnnotationsFactory provideAnnotationsFactory() {
		return AnnotationsFactory.eINSTANCE;
	}

	@Provides
	ArraysFactory provideArraysFactory() {
		return ArraysFactory.eINSTANCE;
	}

	@Provides
	ClassifiersFactory provideClassifiersFactory() {
		return ClassifiersFactory.eINSTANCE;
	}

	@Provides
	ContainersFactory provideContainersFactory() {
		return ContainersFactory.eINSTANCE;
	}

	@Provides
	GenericsFactory provideGenericsFactory() {
		return GenericsFactory.eINSTANCE;
	}

	@Provides
	MembersFactory provideMembersFactory() {
		return MembersFactory.eINSTANCE;
	}

	@Provides
	ModulesFactory provideModulesFactory() {
		return ModulesFactory.eINSTANCE;
	}

	@Provides
	ParametersFactory provideParametersFactory() {
		return ParametersFactory.eINSTANCE;
	}

	@Provides
	ReferencesFactory provideReferencesFactory() {
		return ReferencesFactory.eINSTANCE;
	}

	@Provides
	StatementsFactory provideStatementsFactory() {
		return StatementsFactory.eINSTANCE;
	}

	@Provides
	TypesFactory provideTypesFactory() {
		return TypesFactory.eINSTANCE;
	}

	@Provides
	VariablesFactory provideVariablesFactory() {
		return VariablesFactory.eINSTANCE;
	}

	@Provides
	InstantiationsFactory provideInstantiationsFactory() {
		return InstantiationsFactory.eINSTANCE;
	}
}
