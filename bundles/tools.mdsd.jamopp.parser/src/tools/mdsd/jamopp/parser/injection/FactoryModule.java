package tools.mdsd.jamopp.parser.injection;

import com.google.inject.AbstractModule;

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

public class FactoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ImportsFactory.class).toInstance(ImportsFactory.eINSTANCE);
		bind(ModifiersFactory.class).toInstance(ModifiersFactory.eINSTANCE);
		bind(ExpressionsFactory.class).toInstance(ExpressionsFactory.eINSTANCE);
		bind(LiteralsFactory.class).toInstance(LiteralsFactory.eINSTANCE);
		bind(OperatorsFactory.class).toInstance(OperatorsFactory.eINSTANCE);
		bind(LayoutFactory.class).toInstance(LayoutFactory.eINSTANCE);
		bind(AnnotationsFactory.class).toInstance(AnnotationsFactory.eINSTANCE);
		bind(ArraysFactory.class).toInstance(ArraysFactory.eINSTANCE);
		bind(ClassifiersFactory.class).toInstance(ClassifiersFactory.eINSTANCE);
		bind(ContainersFactory.class).toInstance(ContainersFactory.eINSTANCE);
		bind(GenericsFactory.class).toInstance(GenericsFactory.eINSTANCE);
		bind(MembersFactory.class).toInstance(MembersFactory.eINSTANCE);
		bind(ModulesFactory.class).toInstance(ModulesFactory.eINSTANCE);
		bind(ParametersFactory.class).toInstance(ParametersFactory.eINSTANCE);
		bind(ReferencesFactory.class).toInstance(ReferencesFactory.eINSTANCE);
		bind(StatementsFactory.class).toInstance(StatementsFactory.eINSTANCE);
		bind(TypesFactory.class).toInstance(TypesFactory.eINSTANCE);
		bind(VariablesFactory.class).toInstance(VariablesFactory.eINSTANCE);
		bind(InstantiationsFactory.class).toInstance(InstantiationsFactory.eINSTANCE);
	}

}
