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
		binder().disableCircularProxies();

		bind(new TypeLiteral<Printer<AdditionalField>>() {/* empty */
		}).to(AdditionalFieldPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditionalLocalVariable>>() {/* empty */
		}).to(AdditionalLocalVariablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditiveExpressionChild>>() {/* empty */
		}).to(AdditiveExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditiveExpression>>() {/* empty */
		}).to(AdditiveExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AdditiveOperator>>() {/* empty */
		}).to(AdditiveOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AndExpressionChild>>() {/* empty */
		}).to(AndExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AndExpression>>() {/* empty */
		}).to(AndExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotableAndModifiable>>() {/* empty */
		}).to(AnnotableAndModifiablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Annotable>>() {/* empty */
		}).to(AnnotablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotationInstanceOrModifier>>() {/* empty */
		}).to(AnnotationInstanceOrModifierPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotationInstance>>() {/* empty */
		}).to(AnnotationInstancePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Annotation>>() {/* empty */
		}).to(AnnotationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnnotationValue>>() {/* empty */
		}).to(AnnotationValuePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AnonymousClass>>() {/* empty */
		}).to(AnonymousClassPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Argumentable>>() {/* empty */
		}).to(ArgumentablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<List<ArrayDimension>>>() {/* empty */
		}).to(ArrayDimensionsPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ArrayInitializer>>() {/* empty */
		}).to(ArrayInitializerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ArrayInstantiation>>() {/* empty */
		}).to(ArrayInstantiationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ArraySelector>>() {/* empty */
		}).to(ArraySelectorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Assert>>() {/* empty */
		}).to(AssertPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AssignmentExpressionChild>>() {/* empty */
		}).to(AssignmentExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AssignmentExpression>>() {/* empty */
		}).to(AssignmentExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AssignmentOperator>>() {/* empty */
		}).to(AssignmentOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Block>>() {/* empty */
		}).to(BlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Break>>() {/* empty */
		}).to(BreakPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CallTypeArgumentable>>() {/* empty */
		}).to(CallTypeArgumentablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CastExpression>>() {/* empty */
		}).to(CastExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CatchBlock>>() {/* empty */
		}).to(CatchBlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CatchParameter>>() {/* empty */
		}).to(CatchParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ClassifierReference>>() {/* empty */
		}).to(ClassifierReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ClassMethod>>() {/* empty */
		}).to(ClassMethodPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<tools.mdsd.jamopp.model.java.classifiers.Class>>() {/* empty */
		}).to(ClassPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<CompilationUnit>>() {/* empty */
		}).to(CompilationUnitPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConcreteClassifier>>() {/* empty */
		}).to(ConcreteClassifierPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalAndExpressionChild>>() {/* empty */
		}).to(ConditionalAndExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalAndExpression>>() {/* empty */
		}).to(ConditionalAndExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalExpressionChild>>() {/* empty */
		}).to(ConditionalExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalExpression>>() {/* empty */
		}).to(ConditionalExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalOrExpressionChild>>() {/* empty */
		}).to(ConditionalOrExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ConditionalOrExpression>>() {/* empty */
		}).to(ConditionalOrExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Condition>>() {/* empty */
		}).to(ConditionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Constructor>>() {/* empty */
		}).to(ConstructorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Continue>>() {/* empty */
		}).to(ContinuePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<DefaultSwitchCase>>() {/* empty */
		}).to(DefaultSwitchCasePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<DefaultSwitchRule>>() {/* empty */
		}).to(DefaultSwitchRulePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<DoWhileLoop>>() {/* empty */
		}).to(DoWhileLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ElementReference>>() {/* empty */
		}).to(ElementReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EnumConstant>>() {/* empty */
		}).to(EnumConstantPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Enumeration>>() {/* empty */
		}).to(EnumerationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EqualityExpressionChild>>() {/* empty */
		}).to(EqualityExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EqualityExpression>>() {/* empty */
		}).to(EqualityExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<EqualityOperator>>() {/* empty */
		}).to(EqualityOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExceptionThrower>>() {/* empty */
		}).to(ExceptionThrowerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExclusiveOrExpressionChild>>() {/* empty */
		}).to(ExclusiveOrExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExclusiveOrExpression>>() {/* empty */
		}).to(ExclusiveOrExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExportsModuleDirective>>() {/* empty */
		}).to(ExportsModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Expression>>() {/* empty */
		}).to(ExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ExpressionStatement>>() {/* empty */
		}).to(ExpressionStatementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Field>>() {/* empty */
		}).to(FieldPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ForEachLoop>>() {/* empty */
		}).to(ForEachLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ForLoopInitializer>>() {/* empty */
		}).to(ForLoopInitializerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ForLoop>>() {/* empty */
		}).to(ForLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<IdentifierReference>>() {/* empty */
		}).to(IdentifierReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Implementor>>() {/* empty */
		}).to(ImplementorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ImportingElement>>() {/* empty */
		}).to(ImportingElementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Import>>() {/* empty */
		}).to(ImportPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InclusiveOrExpressionChild>>() {/* empty */
		}).to(InclusiveOrExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InclusiveOrExpression>>() {/* empty */
		}).to(InclusiveOrExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InstanceOfExpressionChild>>() {/* empty */
		}).to(InstanceOfExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InstanceOfExpression>>() {/* empty */
		}).to(InstanceOfExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Instantiation>>() {/* empty */
		}).to(InstantiationPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<InterfaceMethod>>() {/* empty */
		}).to(InterfaceMethodPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Interface>>() {/* empty */
		}).to(InterfacePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<JavaRoot>>() {/* empty */
		}).to(JavaRootPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<JumpLabel>>() {/* empty */
		}).to(JumpLabelPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LambdaExpression>>() {/* empty */
		}).to(LambdaExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LambdaParameters>>() {/* empty */
		}).to(LambdaParametersPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Literal>>() {/* empty */
		}).to(LiteralPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LocalVariable>>() {/* empty */
		}).to(LocalVariablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<LocalVariableStatement>>() {/* empty */
		}).to(LocalVariableStatementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MemberContainer>>() {/* empty */
		}).to(MemberContainerPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Member>>() {/* empty */
		}).to(MemberPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MethodCall>>() {/* empty */
		}).to(MethodCallPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MethodReferenceExpressionChild>>() {/* empty */
		}).to(MethodReferenceExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MethodReferenceExpression>>() {/* empty */
		}).to(MethodReferenceExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Modifier>>() {/* empty */
		}).to(ModifierPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<tools.mdsd.jamopp.model.java.containers.Module>>() {/* empty */
		}).to(ModulePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MultiplicativeExpressionChild>>() {/* empty */
		}).to(MultiplicativeExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MultiplicativeExpression>>() {/* empty */
		}).to(MultiplicativeExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<MultiplicativeOperator>>() {/* empty */
		}).to(MultiplicativeOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NamespaceClassifierReference>>() {/* empty */
		}).to(NamespaceClassifierReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NestedExpression>>() {/* empty */
		}).to(NestedExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NormalSwitchCase>>() {/* empty */
		}).to(NormalSwitchCasePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<NormalSwitchRule>>() {/* empty */
		}).to(NormalSwitchRulePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<OpensModuleDirective>>() {/* empty */
		}).to(OpensModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<OrdinaryParameter>>() {/* empty */
		}).to(OrdinaryParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Parametrizable>>() {/* empty */
		}).to(ParametrizablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<PrefixUnaryModificationExpression>>() {/* empty */
		}).to(PrefixUnaryModificationExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<PrimitiveType>>() {/* empty */
		}).to(PrimitiveTypePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<PrimitiveTypeReference>>() {/* empty */
		}).to(PrimitiveTypeReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ProvidesModuleDirective>>() {/* empty */
		}).to(ProvidesModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ReceiverParameter>>() {/* empty */
		}).to(ReceiverParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Reference>>() {/* empty */
		}).to(ReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RelationExpressionChild>>() {/* empty */
		}).to(RelationExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RelationExpression>>() {/* empty */
		}).to(RelationExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RelationOperator>>() {/* empty */
		}).to(RelationOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<AccessProvidingModuleDirective>>() {/* empty */
		}).to(RemainingAccessProvidingModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<RequiresModuleDirective>>() {/* empty */
		}).to(RequiresModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Resource>>() {/* empty */
		}).to(ResourcePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Return>>() {/* empty */
		}).to(ReturnPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Self>>() {/* empty */
		}).to(SelfPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SelfReference>>() {/* empty */
		}).to(SelfReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ShiftExpressionChild>>() {/* empty */
		}).to(ShiftExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ShiftExpression>>() {/* empty */
		}).to(ShiftExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<ShiftOperator>>() {/* empty */
		}).to(ShiftOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Statement>>() {/* empty */
		}).to(StatementPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<StringReference>>() {/* empty */
		}).to(StringReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SuffixUnaryModificationExpression>>() {/* empty */
		}).to(SuffixUnaryModificationExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SwitchCase>>() {/* empty */
		}).to(SwitchCasePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Switch>>() {/* empty */
		}).to(SwitchPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<SynchronizedBlock>>() {/* empty */
		}).to(SynchronizedBlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TextBlockReference>>() {/* empty */
		}).to(TextBlockReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<Throw>>() {/* empty */
		}).to(ThrowPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TryBlock>>() {/* empty */
		}).to(TryBlockPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeArgumentable>>() {/* empty */
		}).to(TypeArgumentablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeArgument>>() {/* empty */
		}).to(TypeArgumentPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeParameter>>() {/* empty */
		}).to(TypeParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeParametrizable>>() {/* empty */
		}).to(TypeParametrizablePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<TypeReference>>() {/* empty */
		}).to(TypeReferencePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryExpressionChild>>() {/* empty */
		}).to(UnaryExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryExpression>>() {/* empty */
		}).to(UnaryExpressionPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryModificationExpressionChild>>() {/* empty */
		}).to(UnaryModificationExpressionChildPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryModificationOperator>>() {/* empty */
		}).to(UnaryModificationOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UnaryOperator>>() {/* empty */
		}).to(UnaryOperatorPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<UsesModuleDirective>>() {/* empty */
		}).to(UsesModuleDirectivePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<VariableLengthParameter>>() {/* empty */
		}).to(VariableLengthParameterPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<WhileLoop>>() {/* empty */
		}).to(WhileLoopPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<Printer<YieldStatement>>() {/* empty */
		}).to(YieldStatementPrinterImpl.class).in(Singleton.class);

		bind(new TypeLiteral<EmptyPrinter>() {/* empty */
		}).annotatedWith(Names.named("ReflectiveClassReferencePrinter")).to(ReflectiveClassReferencePrinterImpl.class)
				.in(Singleton.class);
		bind(new TypeLiteral<EmptyPrinter>() {/* empty */
		}).annotatedWith(Names.named("InferableTypePrinter")).to(InferableTypePrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<EmptyPrinter>() {/* empty */
		}).annotatedWith(Names.named("EmptyMemberPrinter")).to(EmptyMemberPrinterImpl.class).in(Singleton.class);
		bind(new TypeLiteral<EmptyPrinter>() {/* empty */
		}).annotatedWith(Names.named("EmptyStatementPrinter")).to(EmptyStatementPrinterImpl.class).in(Singleton.class);

	}

}