package tools.mdsd.jamopp.parser.jdt.injection;

import tools.mdsd.jamopp.commons.layout.LayoutFactory;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.model.java.instantiations.InstantiationsFactory;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.model.java.modules.ModulesFactory;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class FactoryModule extends AbstractModule {

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
