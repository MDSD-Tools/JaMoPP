package tools.mdsd.jamopp.parser.jdt.injection;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import tools.mdsd.jamopp.parser.jdt.implementation.converter.BindingInfoToConcreteClassifierConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionAfterAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToArrayDimensionsAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.converter.ToSwitchCasesAndSetConverterImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilArraysImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilLayoutImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilNamedElementImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilReferenceWalkerImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilTypeInstructionSeparationImpl;
import tools.mdsd.jamopp.parser.jdt.implementation.resolver.UtilJdtResolverImpl;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToConcreteClassifierConverterWithExtraInfo;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToSwitchCasesAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class HelperModule extends AbstractModule {

	private static final String SYNTH_CLASS = "SyntheticContainerClass";
	private static final boolean EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS = true;

	@Override
	protected void configure() {
		super.configure();

		bind(UtilArrays.class).to(UtilArraysImpl.class);
		bind(ToConcreteClassifierConverterWithExtraInfo.class)
				.to(BindingInfoToConcreteClassifierConverterImpl.class);
		bind(JdtResolver.class).to(UtilJdtResolverImpl.class).in(Singleton.class);
		bind(UtilLayout.class).to(UtilLayoutImpl.class);
		bind(UtilNamedElement.class).to(UtilNamedElementImpl.class);
		bind(UtilReferenceWalker.class).to(UtilReferenceWalkerImpl.class);
		bind(ToArrayDimensionAfterAndSetConverter.class).to(ToArrayDimensionAfterAndSetConverterImpl.class);
		bind(ToArrayDimensionsAndSetConverter.class).to(ToArrayDimensionsAndSetConverterImpl.class);
		bind(ToSwitchCasesAndSetConverter.class).to(ToSwitchCasesAndSetConverterImpl.class);
		bind(UtilTypeInstructionSeparation.class).to(UtilTypeInstructionSeparationImpl.class);

		bind(String.class).annotatedWith(Names.named("synthClass")).toInstance(SYNTH_CLASS);
		bind(Boolean.class).annotatedWith(Names.named("extractAdditionalInfosFromTypeBindings"))
				.toInstance(EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS);

		bind(new TypeLiteral<HashSet<IModuleBinding>>() {
		}).toInstance(new HashSet<>());
		bind(new TypeLiteral<HashSet<IPackageBinding>>() {
		}).toInstance(new HashSet<>());
		bind(new TypeLiteral<HashSet<ITypeBinding>>() {
		}).toInstance(new HashSet<>());
		bind(new TypeLiteral<HashSet<IMethodBinding>>() {
		}).toInstance(new HashSet<>());
		bind(new TypeLiteral<HashSet<IVariableBinding>>() {
		}).toInstance(new HashSet<>());
		bind(new TypeLiteral<HashSet<EObject>>() {
		}).toInstance(new HashSet<>());

		bind(new TypeLiteral<HashMap<IVariableBinding, Integer>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<IBinding, String>>() {
		}).toInstance(new HashMap<>());

		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.containers.Module>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.containers.Package>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Annotation>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Enumeration>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Interface>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Class>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.generics.TypeParameter>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.members.InterfaceMethod>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.members.ClassMethod>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.members.Constructor>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.members.Field>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.members.AdditionalField>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.variables.LocalVariable>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.members.EnumConstant>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.parameters.CatchParameter>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<HashMap<String, tools.mdsd.jamopp.model.java.classifiers.AnonymousClass>>() {
		}).toInstance(new HashMap<>());

	}

}
