package jamopp.parser.jdt.injector;

import org.emftext.commons.layout.LayoutFactory;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.classifiers.ClassifiersFactory;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.instantiations.InstantiationsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.MembersFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypesFactory;
import org.emftext.language.java.variables.VariablesFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import jamopp.parser.jdt.converter.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilToSwitchCasesAndSetConverterImpl;
import jamopp.parser.jdt.converter.implementation.BlockToBlockConverterImpl;
import jamopp.parser.jdt.converter.implementation.StatementToStatementConverterImpl;
import jamopp.parser.jdt.converter.implementation.ToConcreteClassifierConverterImpl;
import jamopp.parser.jdt.converter.interfaces.BlockToBlockConverter;
import jamopp.parser.jdt.converter.interfaces.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.ToConcreteClassifierConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.handler.ToExpressionConverterImpl;

public class InjectorGuice extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ToExpressionConverter.class).to(ToExpressionConverterImpl.class);
		bind(BlockToBlockConverter.class).to(BlockToBlockConverterImpl.class);
		bind(StatementToStatementConverter.class).to(StatementToStatementConverterImpl.class);
		bind(ToConcreteClassifierConverter.class).to(ToConcreteClassifierConverterImpl.class);
		bind(UtilToSwitchCasesAndSetConverter.class).to(UtilToSwitchCasesAndSetConverterImpl.class);
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
