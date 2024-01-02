package tools.mdsd.jamopp.printer.injection;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.arrays.ArrayInitializer;
import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiation;
import tools.mdsd.jamopp.model.java.arrays.ArraySelector;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Implementor;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.AndExpression;
import tools.mdsd.jamopp.model.java.expressions.AndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.CastExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.LambdaExpression;
import tools.mdsd.jamopp.model.java.expressions.LambdaParameters;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.NestedExpression;
import tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.SuffixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.generics.CallTypeArgumentable;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportingElement;
import tools.mdsd.jamopp.model.java.instantiations.Instantiation;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.literals.Self;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.ExceptionThrower;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.modules.AccessProvidingModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ExportsModuleDirective;
import tools.mdsd.jamopp.model.java.modules.OpensModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ProvidesModuleDirective;
import tools.mdsd.jamopp.model.java.modules.RequiresModuleDirective;
import tools.mdsd.jamopp.model.java.modules.UsesModuleDirective;
import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;
import tools.mdsd.jamopp.model.java.operators.AssignmentOperator;
import tools.mdsd.jamopp.model.java.operators.EqualityOperator;
import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;
import tools.mdsd.jamopp.model.java.operators.UnaryModificationOperator;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;
import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.references.Argumentable;
import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.SelfReference;
import tools.mdsd.jamopp.model.java.references.StringReference;
import tools.mdsd.jamopp.model.java.references.TextBlockReference;
import tools.mdsd.jamopp.model.java.statements.Assert;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.Break;
import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.model.java.statements.Condition;
import tools.mdsd.jamopp.model.java.statements.Continue;
import tools.mdsd.jamopp.model.java.statements.DefaultSwitchCase;
import tools.mdsd.jamopp.model.java.statements.DefaultSwitchRule;
import tools.mdsd.jamopp.model.java.statements.DoWhileLoop;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.model.java.statements.ForEachLoop;
import tools.mdsd.jamopp.model.java.statements.ForLoop;
import tools.mdsd.jamopp.model.java.statements.ForLoopInitializer;
import tools.mdsd.jamopp.model.java.statements.JumpLabel;
import tools.mdsd.jamopp.model.java.statements.LocalVariableStatement;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchCase;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchRule;
import tools.mdsd.jamopp.model.java.statements.Return;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.model.java.statements.SwitchCase;
import tools.mdsd.jamopp.model.java.statements.SynchronizedBlock;
import tools.mdsd.jamopp.model.java.statements.Throw;
import tools.mdsd.jamopp.model.java.statements.TryBlock;
import tools.mdsd.jamopp.model.java.statements.WhileLoop;
import tools.mdsd.jamopp.model.java.statements.YieldStatement;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.Resource;
import tools.mdsd.jamopp.printer.implementation.*;
import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ModulePrinterInjection extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		binder().disableCircularProxies();

		bind(new TypeLiteral<Printer<AdditionalField>>() {
		}).to(AdditionalFieldPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditionalLocalVariable>>() {
		}).to(AdditionalLocalVariablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditiveExpressionChild>>() {
		}).to(AdditiveExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditiveExpression>>() {
		}).to(AdditiveExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditiveOperator>>() {
		}).to(AdditiveOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AndExpressionChild>>() {
		}).to(AndExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AndExpression>>() {
		}).to(AndExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotableAndModifiable>>() {
		}).to(AnnotableAndModifiablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Annotable>>() {
		}).to(AnnotablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotationInstanceOrModifier>>() {
		}).to(AnnotationInstanceOrModifierPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotationInstance>>() {
		}).to(AnnotationInstancePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Annotation>>() {
		}).to(AnnotationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotationValue>>() {
		}).to(AnnotationValuePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnonymousClass>>() {
		}).to(AnonymousClassPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Argumentable>>() {
		}).to(ArgumentablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<List<ArrayDimension>>>() {
		}).to(ArrayDimensionsPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ArrayInitializer>>() {
		}).to(ArrayInitializerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ArrayInstantiation>>() {
		}).to(ArrayInstantiationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ArraySelector>>() {
		}).to(ArraySelectorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Assert>>() {
		}).to(AssertPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AssignmentExpressionChild>>() {
		}).to(AssignmentExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AssignmentExpression>>() {
		}).to(AssignmentExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AssignmentOperator>>() {
		}).to(AssignmentOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Block>>() {
		}).to(BlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Break>>() {
		}).to(BreakPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CallTypeArgumentable>>() {
		}).to(CallTypeArgumentablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CastExpression>>() {
		}).to(CastExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CatchBlock>>() {
		}).to(CatchBlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CatchParameter>>() {
		}).to(CatchParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ClassifierReference>>() {
		}).to(ClassifierReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ClassMethod>>() {
		}).to(ClassMethodPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<tools.mdsd.jamopp.model.java.classifiers.Class>>() {
		}).to(ClassPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CompilationUnit>>() {
		}).to(CompilationUnitPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConcreteClassifier>>() {
		}).to(ConcreteClassifierPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalAndExpressionChild>>() {
		}).to(ConditionalAndExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalAndExpression>>() {
		}).to(ConditionalAndExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalExpressionChild>>() {
		}).to(ConditionalExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalExpression>>() {
		}).to(ConditionalExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalOrExpressionChild>>() {
		}).to(ConditionalOrExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalOrExpression>>() {
		}).to(ConditionalOrExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Condition>>() {
		}).to(ConditionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Constructor>>() {
		}).to(ConstructorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Continue>>() {
		}).to(ContinuePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<DefaultSwitchCase>>() {
		}).to(DefaultSwitchCasePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<DefaultSwitchRule>>() {
		}).to(DefaultSwitchRulePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<DoWhileLoop>>() {
		}).to(DoWhileLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ElementReference>>() {
		}).to(ElementReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EnumConstant>>() {
		}).to(EnumConstantPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Enumeration>>() {
		}).to(EnumerationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EqualityExpressionChild>>() {
		}).to(EqualityExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EqualityExpression>>() {
		}).to(EqualityExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EqualityOperator>>() {
		}).to(EqualityOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExceptionThrower>>() {
		}).to(ExceptionThrowerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExclusiveOrExpressionChild>>() {
		}).to(ExclusiveOrExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExclusiveOrExpression>>() {
		}).to(ExclusiveOrExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExportsModuleDirective>>() {
		}).to(ExportsModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Expression>>() {
		}).to(ExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExpressionStatement>>() {
		}).to(ExpressionStatementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Field>>() {
		}).to(FieldPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ForEachLoop>>() {
		}).to(ForEachLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ForLoopInitializer>>() {
		}).to(ForLoopInitializerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ForLoop>>() {
		}).to(ForLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<IdentifierReference>>() {
		}).to(IdentifierReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Implementor>>() {
		}).to(ImplementorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ImportingElement>>() {
		}).to(ImportingElementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Import>>() {
		}).to(ImportPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InclusiveOrExpressionChild>>() {
		}).to(InclusiveOrExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InclusiveOrExpression>>() {
		}).to(InclusiveOrExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InstanceOfExpressionChild>>() {
		}).to(InstanceOfExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InstanceOfExpression>>() {
		}).to(InstanceOfExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Instantiation>>() {
		}).to(InstantiationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InterfaceMethod>>() {
		}).to(InterfaceMethodPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Interface>>() {
		}).to(InterfacePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<JavaRoot>>() {
		}).to(JavaRootPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<JumpLabel>>() {
		}).to(JumpLabelPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LambdaExpression>>() {
		}).to(LambdaExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LambdaParameters>>() {
		}).to(LambdaParametersPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Literal>>() {
		}).to(LiteralPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LocalVariable>>() {
		}).to(LocalVariablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LocalVariableStatement>>() {
		}).to(LocalVariableStatementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MemberContainer>>() {
		}).to(MemberContainerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Member>>() {
		}).to(MemberPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MethodCall>>() {
		}).to(MethodCallPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MethodReferenceExpressionChild>>() {
		}).to(MethodReferenceExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MethodReferenceExpression>>() {
		}).to(MethodReferenceExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Modifier>>() {
		}).to(ModifierPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<tools.mdsd.jamopp.model.java.containers.Module>>() {
		}).to(ModulePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MultiplicativeExpressionChild>>() {
		}).to(MultiplicativeExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MultiplicativeExpression>>() {
		}).to(MultiplicativeExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MultiplicativeOperator>>() {
		}).to(MultiplicativeOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NamespaceClassifierReference>>() {
		}).to(NamespaceClassifierReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NestedExpression>>() {
		}).to(NestedExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NormalSwitchCase>>() {
		}).to(NormalSwitchCasePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NormalSwitchRule>>() {
		}).to(NormalSwitchRulePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<OpensModuleDirective>>() {
		}).to(OpensModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<OrdinaryParameter>>() {
		}).to(OrdinaryParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Parametrizable>>() {
		}).to(ParametrizablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<PrefixUnaryModificationExpression>>() {
		}).to(PrefixUnaryModificationExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<PrimitiveType>>() {
		}).to(PrimitiveTypePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<PrimitiveTypeReference>>() {
		}).to(PrimitiveTypeReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ProvidesModuleDirective>>() {
		}).to(ProvidesModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ReceiverParameter>>() {
		}).to(ReceiverParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Reference>>() {
		}).to(ReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RelationExpressionChild>>() {
		}).to(RelationExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RelationExpression>>() {
		}).to(RelationExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RelationOperator>>() {
		}).to(RelationOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AccessProvidingModuleDirective>>() {
		}).to(RemainingAccessProvidingModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RequiresModuleDirective>>() {
		}).to(RequiresModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Resource>>() {
		}).to(ResourcePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Return>>() {
		}).to(ReturnPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Self>>() {
		}).to(SelfPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SelfReference>>() {
		}).to(SelfReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ShiftExpressionChild>>() {
		}).to(ShiftExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ShiftExpression>>() {
		}).to(ShiftExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ShiftOperator>>() {
		}).to(ShiftOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Statement>>() {
		}).to(StatementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<StringReference>>() {
		}).to(StringReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SuffixUnaryModificationExpression>>() {
		}).to(SuffixUnaryModificationExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SwitchCase>>() {
		}).to(SwitchCasePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Switch>>() {
		}).to(SwitchPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SynchronizedBlock>>() {
		}).to(SynchronizedBlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TextBlockReference>>() {
		}).to(TextBlockReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Throw>>() {
		}).to(ThrowPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TryBlock>>() {
		}).to(TryBlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeArgumentable>>() {
		}).to(TypeArgumentablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeArgument>>() {
		}).to(TypeArgumentPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeParameter>>() {
		}).to(TypeParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeParametrizable>>() {
		}).to(TypeParametrizablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeReference>>() {
		}).to(TypeReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryExpressionChild>>() {
		}).to(UnaryExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryExpression>>() {
		}).to(UnaryExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryModificationExpressionChild>>() {
		}).to(UnaryModificationExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryModificationOperator>>() {
		}).to(UnaryModificationOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryOperator>>() {
		}).to(UnaryOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UsesModuleDirective>>() {
		}).to(UsesModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<VariableLengthParameter>>() {
		}).to(VariableLengthParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<WhileLoop>>() {
		}).to(WhileLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<YieldStatement>>() {
		}).to(YieldStatementPrinterImpl.class).in(Singleton.class);

		bind(new TypeLiteral<EmptyPrinter>() {
		}).annotatedWith(Names.named("ReflectiveClassReferencePrinter")).to(ReflectiveClassReferencePrinterImpl.class)
				.in(Singleton.class);
		bind(new TypeLiteral<EmptyPrinter>() {
		}).annotatedWith(Names.named("InferableTypePrinter")).to(InferableTypePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<EmptyPrinter>() {
		}).annotatedWith(Names.named("EmptyMemberPrinter")).to(EmptyMemberPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<EmptyPrinter>() {
		}).annotatedWith(Names.named("EmptyStatementPrinter")).to(EmptyStatementPrinterImpl.class).in(Singleton.class);

	}

}