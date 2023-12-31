package tools.mdsd.jamopp.parser.jdt.injection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.ModuleDirective;
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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.modules.ModuleReference;
import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;
import tools.mdsd.jamopp.model.java.operators.AssignmentOperator;
import tools.mdsd.jamopp.model.java.operators.EqualityOperator;
import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.model.java.statements.JumpLabel;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingInfoToConcreteClassifierConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToAnnotationAttributeSettingConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToAnnotationInstanceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToConstructorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToEnumConstantConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToFieldConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToInternalReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToMethodConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToModuleConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToNamespaceClassifierReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToPackageConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingToTypeParameterConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.BlockToBlockConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ObjectToAnnotationValueConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ObjectToPrimaryExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.SwitchToSwitchConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAdditionalFieldConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAdditionalLocalVariableConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAdditiveExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAdditiveOperatorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAnnotationInstanceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAnnotationValueConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAnonymousClassConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionAfterAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionsAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayInitialisierConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToAssignmentConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToBlockConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToCatchblockConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToClassMemberConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToClassMethodOrConstructorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToClassOrInterfaceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToClassifierOrNamespaceClassifierReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToClassifierReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToCompilationUnitConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToConcreteClassifierConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToConditionalExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToDirectiveConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToEnumConstantConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToEnumConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToEqualityExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToEqualityOperatorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToFieldConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToImportConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToInterfaceMemberConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToInterfaceMethodConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToInterfaceMethodOrConstructorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToLocalVariableConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToMethodReferenceExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToModifierConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToModifierOrAnnotationInstanceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToModifiersConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToModuleConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToModuleReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToMultiplicativeExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToMultiplicativeOperatorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToNamespaceClassifierReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToNonOnDemandNonStaticConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToNonOnDemandStaticConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToNumberLiteralConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToOnDemandNonStaticConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToOnDemandStaticConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToOrdinaryParameterConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToParameterConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToPrimaryExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReceiverParameterConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromMethodInvocation;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromName;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromSimpleName;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromStatement;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromType;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToRelationExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToRelationOperatorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToShiftExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToShiftOperatorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToSwitchCaseConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToSwitchCasesAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToTypeArgumentConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToTypeParameterConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToTypeReferenceConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToTypeReferencesConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToUnaryExpressionConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToUnaryOperatorConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.TypeToTypeArgumentConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerAssignment;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerCastExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerConditionalExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerInfixExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerInstanceOf;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerLambdaExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerMethodReference;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerPostfixExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerPrefixExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerPrimaryExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.HandlerSwitchExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.expression.ToExpressionConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.AssertStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.BlockHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.BreakStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.ContinueStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.DoStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.EmptyStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.EnhancedForStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.ExpressionStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.ForStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.IfStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.LabeledStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.OtherHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.ReturnStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.StatementToStatementConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.SwitchStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.SynchonizedStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.ThrowStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.TryStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.TypeDeclarationStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.VariableDeclarationStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.WhileStatementHandler;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.statement.YieldStatementHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToConcreteClassifierConverterWithExtraInfo;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToSwitchCasesAndSetConverter;

public class ConverterModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(ToArrayDimensionAfterAndSetConverter.class).to(ToArrayDimensionAfterAndSetConverterImpl.class);
		bind(ToArrayDimensionsAndSetConverter.class).to(ToArrayDimensionsAndSetConverterImpl.class);
		bind(ToSwitchCasesAndSetConverter.class).to(ToSwitchCasesAndSetConverterImpl.class);
		bind(ToConcreteClassifierConverterWithExtraInfo.class).to(BindingInfoToConcreteClassifierConverterImpl.class);

		// Expression
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerAssignment")).to(HandlerAssignment.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerCastExpression"))
				.to(HandlerCastExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerConditionalExpression"))
				.to(HandlerConditionalExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerInfixExpression"))
				.to(HandlerInfixExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerInstanceOf")).to(HandlerInstanceOf.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerLambdaExpression"))
				.to(HandlerLambdaExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerMethodReference"))
				.to(HandlerMethodReference.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerPostfixExpression"))
				.to(HandlerPostfixExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerPrefixExpression"))
				.to(HandlerPrefixExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerPrimaryExpression"))
				.to(HandlerPrimaryExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerSwitchExpression"))
				.to(HandlerSwitchExpression.class);

		// Statement
		bind(StatementHandler.class).annotatedWith(Names.named("AssertStatementHandler"))
				.to(AssertStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("BlockHandler")).to(BlockHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("BreakStatementHandler"))
				.to(BreakStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("ContinueStatementHandler"))
				.to(ContinueStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("DoStatementHandler")).to(DoStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("EmptyStatementHandler"))
				.to(EmptyStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("EnhancedForStatementHandler"))
				.to(EnhancedForStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("ExpressionStatementHandler"))
				.to(ExpressionStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("ForStatementHandler")).to(ForStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("IfStatementHandler")).to(IfStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("LabeledStatementHandler"))
				.to(LabeledStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("OtherHandler")).to(OtherHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("ReturnStatementHandler"))
				.to(ReturnStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("SwitchStatementHandler"))
				.to(SwitchStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("SynchonizedStatementHandler"))
				.to(SynchonizedStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("ThrowStatementHandler"))
				.to(ThrowStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("TryStatementHandler")).to(TryStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("TypeDeclarationStatementHandler"))
				.to(TypeDeclarationStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("VariableDeclarationStatementHandler"))
				.to(VariableDeclarationStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("WhileStatementHandler"))
				.to(WhileStatementHandler.class);
		bind(StatementHandler.class).annotatedWith(Names.named("YieldStatementHandler"))
				.to(YieldStatementHandler.class);
		bind(new TypeLiteral<Set<JumpLabel>>() {
		}).toInstance(new HashSet<>());

		// Converter
		bind(new TypeLiteral<Converter<IMemberValuePairBinding, AnnotationAttributeSetting>>() {
		}).to(BindingToAnnotationAttributeSettingConverter.class);
		bind(new TypeLiteral<Converter<IAnnotationBinding, AnnotationInstance>>() {
		}).to(BindingToAnnotationInstanceConverter.class);
		bind(new TypeLiteral<Converter<IMethodBinding, Constructor>>() {
		}).to(BindingToConstructorConverter.class);
		bind(new TypeLiteral<Converter<IVariableBinding, EnumConstant>>() {
		}).to(BindingToEnumConstantConverter.class);
		bind(new TypeLiteral<Converter<IVariableBinding, Field>>() {
		}).to(BindingToFieldConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, Reference>>() {
		}).to(BindingToInternalReferenceConverter.class);
		bind(new TypeLiteral<Converter<IMethodBinding, Method>>() {
		}).to(BindingToMethodConverter.class);
		bind(new TypeLiteral<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>>() {
		}).to(BindingToModuleConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, NamespaceClassifierReference>>() {
		}).to(BindingToNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>>() {
		}).to(BindingToPackageConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, TypeParameter>>() {
		}).to(BindingToTypeParameterConverter.class);
		bind(new TypeLiteral<Converter<Block, tools.mdsd.jamopp.model.java.statements.Block>>() {
		}).to(BlockToBlockConverterImpl.class);
		bind(new TypeLiteral<Converter<Object, AnnotationValue>>() {
		}).to(ObjectToAnnotationValueConverter.class);
		bind(new TypeLiteral<Converter<Object, PrimaryExpression>>() {
		}).to(ObjectToPrimaryExpressionConverter.class);
		bind(new TypeLiteral<Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement>>() {
		}).to(StatementToStatementConverterImpl.class);
		bind(new TypeLiteral<Converter<SwitchStatement, Switch>>() {
		}).to(SwitchToSwitchConverter.class);
		bind(new TypeLiteral<Converter<VariableDeclarationFragment, AdditionalField>>() {
		}).to(ToAdditionalFieldConverter.class);
		bind(new TypeLiteral<Converter<VariableDeclarationFragment, AdditionalLocalVariable>>() {
		}).to(ToAdditionalLocalVariableConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression, AdditiveExpression>>() {
		}).to(ToAdditiveExpressionConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression.Operator, AdditiveOperator>>() {
		}).to(ToAdditiveOperatorConverter.class);
		bind(new TypeLiteral<Converter<Annotation, AnnotationInstance>>() {
		}).to(ToAnnotationInstanceConverter.class);
		bind(new TypeLiteral<Converter<Expression, AnnotationValue>>() {
		}).to(ToAnnotationValueConverter.class);
		bind(new TypeLiteral<Converter<AnonymousClassDeclaration, AnonymousClass>>() {
		}).to(ToAnonymousClassConverter.class);
		bind(new TypeLiteral<Converter<Dimension, ArrayDimension>>() {
		}).to(ToArrayDimensionConverter.class);
		bind(new TypeLiteral<Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer>>() {
		}).to(ToArrayInitialisierConverter.class);
		bind(new TypeLiteral<Converter<Assignment.Operator, AssignmentOperator>>() {
		}).to(ToAssignmentConverter.class);
		bind(new TypeLiteral<Converter<org.eclipse.jdt.core.dom.Initializer, tools.mdsd.jamopp.model.java.statements.Block>>() {
		}).to(ToBlockConverter.class);
		bind(new TypeLiteral<Converter<CatchClause, CatchBlock>>() {
		}).to(ToCatchblockConverter.class);
		bind(new TypeLiteral<Converter<Name, TypeReference>>() {
		}).to(ToClassifierOrNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<Converter<SimpleName, ClassifierReference>>() {
		}).to(ToClassifierReferenceConverter.class);
		bind(new TypeLiteral<Converter<BodyDeclaration, Member>>() {
		}).annotatedWith(Names.named("ToClassMemberConverter")).to(ToClassMemberConverter.class);
		bind(new TypeLiteral<Converter<MethodDeclaration, Member>>() {
		}).annotatedWith(Names.named("ToClassMethodOrConstructorConverter"))
				.to(ToClassMethodOrConstructorConverter.class);
		bind(new TypeLiteral<Converter<TypeDeclaration, ConcreteClassifier>>() {
		}).to(ToClassOrInterfaceConverter.class);
		bind(new TypeLiteral<Converter<AbstractTypeDeclaration, ConcreteClassifier>>() {
		}).to(ToConcreteClassifierConverterImpl.class);
		bind(new TypeLiteral<Converter<org.eclipse.jdt.core.dom.ConditionalExpression, ConditionalExpression>>() {
		}).to(ToConditionalExpressionConverter.class);
		bind(new TypeLiteral<Converter<EnumConstantDeclaration, EnumConstant>>() {
		}).to(ToEnumConstantConverter.class);
		bind(new TypeLiteral<Converter<EnumDeclaration, Enumeration>>() {
		}).to(ToEnumConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression, EqualityExpression>>() {
		}).to(ToEqualityExpressionConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression.Operator, EqualityOperator>>() {
		}).to(ToEqualityOperatorConverter.class);
		bind(new TypeLiteral<Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression>>() {
		}).to(ToExpressionConverterImpl.class);
		bind(new TypeLiteral<Converter<FieldDeclaration, Field>>() {
		}).to(ToFieldConverter.class);
		bind(new TypeLiteral<Converter<BodyDeclaration, Member>>() {
		}).annotatedWith(Names.named("ToInterfaceMemberConverter")).to(ToInterfaceMemberConverter.class);
		bind(new TypeLiteral<Converter<AnnotationTypeMemberDeclaration, InterfaceMethod>>() {
		}).to(ToInterfaceMethodConverter.class);
		bind(new TypeLiteral<Converter<MethodDeclaration, Member>>() {
		}).annotatedWith(Names.named("ToInterfaceMethodOrConstructorConverter"))
				.to(ToInterfaceMethodOrConstructorConverter.class);
		bind(new TypeLiteral<Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable>>() {
		}).to(ToLocalVariableConverter.class);
		bind(new TypeLiteral<Converter<MethodReference, MethodReferenceExpression>>() {
		}).to(ToMethodReferenceExpressionConverter.class);
		bind(new TypeLiteral<Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier>>() {
		}).to(ToModifierConverter.class);
		bind(new TypeLiteral<Converter<IExtendedModifier, AnnotationInstanceOrModifier>>() {
		}).to(ToModifierOrAnnotationInstanceConverter.class);
		bind(new TypeLiteral<Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>>>() {
		}).to(ToModifiersConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression, MultiplicativeExpression>>() {
		}).to(ToMultiplicativeExpressionConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression.Operator, MultiplicativeOperator>>() {
		}).to(ToMultiplicativeOperatorConverter.class);
		bind(new TypeLiteral<Converter<TypeReference, NamespaceClassifierReference>>() {
		}).to(ToNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<Converter<NumberLiteral, tools.mdsd.jamopp.model.java.literals.Literal>>() {
		}).to(ToNumberLiteralConverter.class);
		bind(new TypeLiteral<Converter<SingleVariableDeclaration, OrdinaryParameter>>() {
		}).to(ToOrdinaryParameterConverter.class);
		bind(new TypeLiteral<Converter<SingleVariableDeclaration, Parameter>>() {
		}).to(ToParameterConverter.class);
		bind(new TypeLiteral<Converter<Expression, PrimaryExpression>>() {
		}).to(ToPrimaryExpressionConverter.class);
		bind(new TypeLiteral<Converter<MethodDeclaration, ReceiverParameter>>() {
		}).to(ToReceiverParameterConverter.class);
		bind(new TypeLiteral<Converter<Expression, tools.mdsd.jamopp.model.java.references.Reference>>() {
		}).to(ToReferenceConverterFromExpression.class);
		bind(new TypeLiteral<Converter<MethodInvocation, MethodCall>>() {
		}).to(ToReferenceConverterFromMethodInvocation.class);
		bind(new TypeLiteral<Converter<Name, IdentifierReference>>() {
		}).to(ToReferenceConverterFromName.class);
		bind(new TypeLiteral<Converter<SimpleName, IdentifierReference>>() {
		}).to(ToReferenceConverterFromSimpleName.class);
		bind(new TypeLiteral<Converter<Statement, tools.mdsd.jamopp.model.java.references.Reference>>() {
		}).to(ToReferenceConverterFromStatement.class);
		bind(new TypeLiteral<Converter<Type, Reference>>() {
		}).to(ToReferenceConverterFromType.class);
		bind(new TypeLiteral<Converter<InfixExpression, RelationExpression>>() {
		}).to(ToRelationExpressionConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression.Operator, RelationOperator>>() {
		}).to(ToRelationOperatorConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression, ShiftExpression>>() {
		}).to(ToShiftExpressionConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression.Operator, ShiftOperator>>() {
		}).to(ToShiftOperatorConverter.class);
		bind(new TypeLiteral<Converter<SwitchCase, tools.mdsd.jamopp.model.java.statements.SwitchCase>>() {
		}).to(ToSwitchCaseConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, TypeArgument>>() {
		}).to(ToTypeArgumentConverter.class);
		bind(new TypeLiteral<Converter<org.eclipse.jdt.core.dom.TypeParameter, tools.mdsd.jamopp.model.java.generics.TypeParameter>>() {
		}).to(ToTypeParameterConverter.class);
		bind(new TypeLiteral<Converter<Type, TypeReference>>() {
		}).to(ToTypeReferenceConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, List<TypeReference>>>() {
		}).to(ToTypeReferencesConverter.class);
		bind(new TypeLiteral<Converter<PrefixExpression, UnaryExpression>>() {
		}).to(ToUnaryExpressionConverter.class);
		bind(new TypeLiteral<Converter<PrefixExpression.Operator, UnaryOperator>>() {
		}).to(ToUnaryOperatorConverter.class);
		bind(new TypeLiteral<Converter<Type, TypeArgument>>() {
		}).to(TypeToTypeArgumentConverter.class);

		bind(new TypeLiteral<Converter<CompilationUnit, tools.mdsd.jamopp.model.java.containers.CompilationUnit>>() {
		}).to(ToCompilationUnitConverter.class);
		bind(new TypeLiteral<Converter<Name, ModuleReference>>() {
		}).to(ToModuleReferenceConverter.class);
		bind(new TypeLiteral<Converter<ModuleDirective, tools.mdsd.jamopp.model.java.modules.ModuleDirective>>() {
		}).to(ToDirectiveConverter.class);
		bind(new TypeLiteral<Converter<ModuleDeclaration, tools.mdsd.jamopp.model.java.containers.Module>>() {
		}).to(ToModuleConverter.class);
		bind(new TypeLiteral<Converter<ImportDeclaration, Import>>() {
		}).annotatedWith(Names.named("ToNonOnDemandNonStaticConverter")).to(ToNonOnDemandNonStaticConverter.class);
		bind(new TypeLiteral<Converter<ImportDeclaration, Import>>() {
		}).annotatedWith(Names.named("ToNonOnDemandStaticConverter")).to(ToNonOnDemandStaticConverter.class);
		bind(new TypeLiteral<Converter<ImportDeclaration, Import>>() {
		}).annotatedWith(Names.named("ToOnDemandNonStaticConverter")).to(ToOnDemandNonStaticConverter.class);
		bind(new TypeLiteral<Converter<ImportDeclaration, Import>>() {
		}).annotatedWith(Names.named("ToOnDemandStaticConverter")).to(ToOnDemandStaticConverter.class);
		bind(new TypeLiteral<Converter<ImportDeclaration, Import>>() {
		}).annotatedWith(Names.named("ToImportConverter")).to(ToImportConverter.class);

	}

}
