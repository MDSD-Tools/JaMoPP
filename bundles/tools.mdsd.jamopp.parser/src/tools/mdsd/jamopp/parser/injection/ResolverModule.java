package tools.mdsd.jamopp.parser.injection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import tools.mdsd.jamopp.parser.implementation.resolver.MethodCheckerImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.MethodCompleterImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.PureTypeBindingsConverterImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.ResolutionCompleterImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.UidManagerImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.UtilJdtResolverImpl;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodChecker;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.PureTypeBindingsConverter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolutionCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.UidManager;

public class ResolverModule extends AbstractModule {

	private static final String SYNTH_CLASS = "SyntheticContainerClass";
	private static final boolean EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS = true;

	@Override
	protected void configure() {
		bind(JdtResolver.class).to(UtilJdtResolverImpl.class).in(Singleton.class);
		bind(UidManager.class).to(UidManagerImpl.class);
		bind(MethodChecker.class).to(MethodCheckerImpl.class);
		bind(MethodCompleter.class).to(MethodCompleterImpl.class);
		bind(ResolutionCompleter.class).to(ResolutionCompleterImpl.class);
		bind(PureTypeBindingsConverter.class).to(PureTypeBindingsConverterImpl.class);

		bind(String.class).annotatedWith(Names.named("synthClass")).toInstance(SYNTH_CLASS);
		bind(Boolean.class).annotatedWith(Names.named("extractAdditionalInfosFromTypeBindings"))
				.toInstance(EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS);

		bind(new TypeLiteral<Set<IModuleBinding>>() {
			/* empty */}).toInstance(new HashSet<>());
		bind(new TypeLiteral<Set<IPackageBinding>>() {
			/* empty */}).toInstance(new HashSet<>());
		bind(new TypeLiteral<Set<ITypeBinding>>() {
			/* empty */}).toInstance(new HashSet<>());
		bind(new TypeLiteral<Set<IMethodBinding>>() {
			/* empty */}).toInstance(new HashSet<>());
		bind(new TypeLiteral<Set<IVariableBinding>>() {
			/* empty */}).toInstance(new HashSet<>());
		bind(new TypeLiteral<Set<EObject>>() {
			/* empty */}).toInstance(new HashSet<>());

		bind(new TypeLiteral<Map<IVariableBinding, Integer>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<IBinding, String>>() {
			/* empty */}).toInstance(new HashMap<>());

		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.containers.Module>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.containers.Package>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.classifiers.Annotation>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.classifiers.Enumeration>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.classifiers.Interface>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.classifiers.Class>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.generics.TypeParameter>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.members.InterfaceMethod>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.members.ClassMethod>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.members.Constructor>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.members.Field>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.members.AdditionalField>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.variables.LocalVariable>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.members.EnumConstant>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.parameters.CatchParameter>>() {
			/* empty */}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, tools.mdsd.jamopp.model.java.classifiers.AnonymousClass>>() {
			/* empty */}).toInstance(new HashMap<>());

	}

}
