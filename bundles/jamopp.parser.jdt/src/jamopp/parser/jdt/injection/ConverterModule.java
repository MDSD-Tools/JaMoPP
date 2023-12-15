package jamopp.parser.jdt.injection;

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
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.PrimaryExpression;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modules.ModuleReference;
import org.emftext.language.java.operators.AdditiveOperator;
import org.emftext.language.java.operators.AssignmentOperator;
import org.emftext.language.java.operators.EqualityOperator;
import org.emftext.language.java.operators.MultiplicativeOperator;
import org.emftext.language.java.operators.RelationOperator;
import org.emftext.language.java.operators.ShiftOperator;
import org.emftext.language.java.operators.UnaryOperator;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import jamopp.parser.jdt.implementation.converter.BindingToAnnotationAttributeSettingConverter;
import jamopp.parser.jdt.implementation.converter.BindingToAnnotationInstanceConverter;
import jamopp.parser.jdt.implementation.converter.BindingToConstructorConverter;
import jamopp.parser.jdt.implementation.converter.BindingToEnumConstantConverter;
import jamopp.parser.jdt.implementation.converter.BindingToFieldConverter;
import jamopp.parser.jdt.implementation.converter.BindingToInternalReferenceConverter;
import jamopp.parser.jdt.implementation.converter.BindingToMethodConverter;
import jamopp.parser.jdt.implementation.converter.BindingToModuleConverter;
import jamopp.parser.jdt.implementation.converter.BindingToNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.implementation.converter.BindingToPackageConverter;
import jamopp.parser.jdt.implementation.converter.BindingToTypeParameterConverter;
import jamopp.parser.jdt.implementation.converter.BlockToBlockConverterImpl;
import jamopp.parser.jdt.implementation.converter.ObjectToAnnotationValueConverter;
import jamopp.parser.jdt.implementation.converter.ObjectToPrimaryExpressionConverter;
import jamopp.parser.jdt.implementation.converter.StatementToStatementConverterImpl;
import jamopp.parser.jdt.implementation.converter.SwitchToSwitchConverter;
import jamopp.parser.jdt.implementation.converter.ToAdditionalFieldConverter;
import jamopp.parser.jdt.implementation.converter.ToAdditionalLocalVariableConverter;
import jamopp.parser.jdt.implementation.converter.ToAdditiveExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToAdditiveOperatorConverter;
import jamopp.parser.jdt.implementation.converter.ToAnnotationInstanceConverter;
import jamopp.parser.jdt.implementation.converter.ToAnnotationValueConverter;
import jamopp.parser.jdt.implementation.converter.ToAnonymousClassConverter;
import jamopp.parser.jdt.implementation.converter.ToArrayDimensionConverter;
import jamopp.parser.jdt.implementation.converter.ToArrayInitialisierConverter;
import jamopp.parser.jdt.implementation.converter.ToAssignmentConverter;
import jamopp.parser.jdt.implementation.converter.ToBlockConverter;
import jamopp.parser.jdt.implementation.converter.ToCatchblockConverter;
import jamopp.parser.jdt.implementation.converter.ToClassMemberConverter;
import jamopp.parser.jdt.implementation.converter.ToClassMethodOrConstructorConverter;
import jamopp.parser.jdt.implementation.converter.ToClassOrInterfaceConverter;
import jamopp.parser.jdt.implementation.converter.ToClassifierOrNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.implementation.converter.ToClassifierReferenceConverter;
import jamopp.parser.jdt.implementation.converter.ToCompilationUnitConverter;
import jamopp.parser.jdt.implementation.converter.ToConcreteClassifierConverterImpl;
import jamopp.parser.jdt.implementation.converter.ToConditionalExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToDirectiveConverter;
import jamopp.parser.jdt.implementation.converter.ToEnumConstantConverter;
import jamopp.parser.jdt.implementation.converter.ToEnumConverter;
import jamopp.parser.jdt.implementation.converter.ToEqualityExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToEqualityOperatorConverter;
import jamopp.parser.jdt.implementation.converter.ToExpressionConverterImpl;
import jamopp.parser.jdt.implementation.converter.ToFieldConverter;
import jamopp.parser.jdt.implementation.converter.ToImportConverter;
import jamopp.parser.jdt.implementation.converter.ToInterfaceMemberConverter;
import jamopp.parser.jdt.implementation.converter.ToInterfaceMethodConverter;
import jamopp.parser.jdt.implementation.converter.ToInterfaceMethodOrConstructorConverter;
import jamopp.parser.jdt.implementation.converter.ToLocalVariableConverter;
import jamopp.parser.jdt.implementation.converter.ToMethodReferenceExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToModifierConverter;
import jamopp.parser.jdt.implementation.converter.ToModifierOrAnnotationInstanceConverter;
import jamopp.parser.jdt.implementation.converter.ToModifiersConverter;
import jamopp.parser.jdt.implementation.converter.ToModuleConverter;
import jamopp.parser.jdt.implementation.converter.ToModuleReferenceConverter;
import jamopp.parser.jdt.implementation.converter.ToMultiplicativeExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToMultiplicativeOperatorConverter;
import jamopp.parser.jdt.implementation.converter.ToNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.implementation.converter.ToNonOnDemandNonStaticConverter;
import jamopp.parser.jdt.implementation.converter.ToNonOnDemandStaticConverter;
import jamopp.parser.jdt.implementation.converter.ToNumberLiteralConverter;
import jamopp.parser.jdt.implementation.converter.ToOnDemandNonStaticConverter;
import jamopp.parser.jdt.implementation.converter.ToOnDemandStaticConverter;
import jamopp.parser.jdt.implementation.converter.ToOrdinaryParameterConverter;
import jamopp.parser.jdt.implementation.converter.ToParameterConverter;
import jamopp.parser.jdt.implementation.converter.ToPrimaryExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToReceiverParameterConverter;
import jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromExpression;
import jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromMethodInvocation;
import jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromName;
import jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromSimpleName;
import jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromStatement;
import jamopp.parser.jdt.implementation.converter.ToReferenceConverterFromType;
import jamopp.parser.jdt.implementation.converter.ToRelationExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToRelationOperatorConverter;
import jamopp.parser.jdt.implementation.converter.ToShiftExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToShiftOperatorConverter;
import jamopp.parser.jdt.implementation.converter.ToSwitchCaseConverter;
import jamopp.parser.jdt.implementation.converter.ToTypeArgumentConverter;
import jamopp.parser.jdt.implementation.converter.ToTypeParameterConverter;
import jamopp.parser.jdt.implementation.converter.ToTypeReferenceConverter;
import jamopp.parser.jdt.implementation.converter.ToTypeReferencesConverter;
import jamopp.parser.jdt.implementation.converter.ToUnaryExpressionConverter;
import jamopp.parser.jdt.implementation.converter.ToUnaryOperatorConverter;
import jamopp.parser.jdt.implementation.converter.TypeToTypeArgumentConverter;
import jamopp.parser.jdt.interfaces.converter.Converter;

public class ConverterModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

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
		bind(new TypeLiteral<Converter<IModuleBinding, org.emftext.language.java.containers.Module>>() {
		}).to(BindingToModuleConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, NamespaceClassifierReference>>() {
		}).to(BindingToNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<Converter<IPackageBinding, org.emftext.language.java.containers.Package>>() {
		}).to(BindingToPackageConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, TypeParameter>>() {
		}).to(BindingToTypeParameterConverter.class);
		bind(new TypeLiteral<Converter<Block, org.emftext.language.java.statements.Block>>() {
		}).to(BlockToBlockConverterImpl.class);
		bind(new TypeLiteral<Converter<Object, AnnotationValue>>() {
		}).to(ObjectToAnnotationValueConverter.class);
		bind(new TypeLiteral<Converter<Object, PrimaryExpression>>() {
		}).to(ObjectToPrimaryExpressionConverter.class);
		bind(new TypeLiteral<Converter<Statement, org.emftext.language.java.statements.Statement>>() {
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
		bind(new TypeLiteral<Converter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer>>() {
		}).to(ToArrayInitialisierConverter.class);
		bind(new TypeLiteral<Converter<Assignment.Operator, AssignmentOperator>>() {
		}).to(ToAssignmentConverter.class);
		bind(new TypeLiteral<Converter<org.eclipse.jdt.core.dom.Initializer, org.emftext.language.java.statements.Block>>() {
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
		bind(new TypeLiteral<Converter<Expression, org.emftext.language.java.expressions.Expression>>() {
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
		bind(new TypeLiteral<Converter<VariableDeclarationExpression, org.emftext.language.java.variables.LocalVariable>>() {
		}).to(ToLocalVariableConverter.class);
		bind(new TypeLiteral<Converter<MethodReference, MethodReferenceExpression>>() {
		}).to(ToMethodReferenceExpressionConverter.class);
		bind(new TypeLiteral<Converter<Modifier, org.emftext.language.java.modifiers.Modifier>>() {
		}).to(ToModifierConverter.class);
		bind(new TypeLiteral<Converter<IExtendedModifier, AnnotationInstanceOrModifier>>() {
		}).to(ToModifierOrAnnotationInstanceConverter.class);
		bind(new TypeLiteral<Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>>>() {
		}).to(ToModifiersConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression, MultiplicativeExpression>>() {
		}).to(ToMultiplicativeExpressionConverter.class);
		bind(new TypeLiteral<Converter<InfixExpression.Operator, MultiplicativeOperator>>() {
		}).to(ToMultiplicativeOperatorConverter.class);
		bind(new TypeLiteral<Converter<TypeReference, NamespaceClassifierReference>>() {
		}).to(ToNamespaceClassifierReferenceConverter.class);
		bind(new TypeLiteral<Converter<NumberLiteral, org.emftext.language.java.literals.Literal>>() {
		}).to(ToNumberLiteralConverter.class);
		bind(new TypeLiteral<Converter<SingleVariableDeclaration, OrdinaryParameter>>() {
		}).to(ToOrdinaryParameterConverter.class);
		bind(new TypeLiteral<Converter<SingleVariableDeclaration, Parameter>>() {
		}).to(ToParameterConverter.class);
		bind(new TypeLiteral<Converter<Expression, PrimaryExpression>>() {
		}).to(ToPrimaryExpressionConverter.class);
		bind(new TypeLiteral<Converter<MethodDeclaration, ReceiverParameter>>() {
		}).to(ToReceiverParameterConverter.class);
		bind(new TypeLiteral<Converter<Expression, org.emftext.language.java.references.Reference>>() {
		}).to(ToReferenceConverterFromExpression.class);
		bind(new TypeLiteral<Converter<MethodInvocation, MethodCall>>() {
		}).to(ToReferenceConverterFromMethodInvocation.class);
		bind(new TypeLiteral<Converter<Name, IdentifierReference>>() {
		}).to(ToReferenceConverterFromName.class);
		bind(new TypeLiteral<Converter<SimpleName, IdentifierReference>>() {
		}).to(ToReferenceConverterFromSimpleName.class);
		bind(new TypeLiteral<Converter<Statement, org.emftext.language.java.references.Reference>>() {
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
		bind(new TypeLiteral<Converter<SwitchCase, org.emftext.language.java.statements.SwitchCase>>() {
		}).to(ToSwitchCaseConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding, TypeArgument>>() {
		}).to(ToTypeArgumentConverter.class);
		bind(new TypeLiteral<Converter<org.eclipse.jdt.core.dom.TypeParameter, org.emftext.language.java.generics.TypeParameter>>() {
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

		bind(new TypeLiteral<Converter<CompilationUnit, org.emftext.language.java.containers.CompilationUnit>>() {
		}).to(ToCompilationUnitConverter.class);
		bind(new TypeLiteral<Converter<Name, ModuleReference>>() {
		}).to(ToModuleReferenceConverter.class);
		bind(new TypeLiteral<Converter<ModuleDirective, org.emftext.language.java.modules.ModuleDirective>>() {
		}).to(ToDirectiveConverter.class);
		bind(new TypeLiteral<Converter<ModuleDeclaration, org.emftext.language.java.containers.Module>>() {
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