package tools.mdsd.jamopp.parser.jdt.injection;

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
