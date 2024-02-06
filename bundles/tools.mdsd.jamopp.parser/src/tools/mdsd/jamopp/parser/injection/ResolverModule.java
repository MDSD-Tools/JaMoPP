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

import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.containers.Module;
import tools.mdsd.jamopp.model.java.containers.Package;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.parser.implementation.resolver.AdditionalFieldResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.AdditionalLocalVariableResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.AnnotationResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.AnonymousClassResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.CatchParameterResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.ClassMethodResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.ClassResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.ClassResolverExtensionImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.ClassifierResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.ConstructorResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.EnumConstantResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.EnumerationResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.FieldResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.InterfaceMethodResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.InterfaceResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.LocalVariableResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.MethodCheckerImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.MethodCompleterImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.MethodResolverImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.ModuleResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.OrdinaryParameterResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.PackageResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.PureTypeBindingsConverterImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.ReferenceableElementResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.ResolutionCompleterImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.ToFieldNameConverter;
import tools.mdsd.jamopp.parser.implementation.resolver.ToMethodNameConverter;
import tools.mdsd.jamopp.parser.implementation.resolver.ToParameterNameConverter;
import tools.mdsd.jamopp.parser.implementation.resolver.ToTypeNameConverter;
import tools.mdsd.jamopp.parser.implementation.resolver.ToTypeParameterNameConverter;
import tools.mdsd.jamopp.parser.implementation.resolver.TypeParameterResolver;
import tools.mdsd.jamopp.parser.implementation.resolver.UidManagerImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.UtilJdtResolverImpl;
import tools.mdsd.jamopp.parser.implementation.resolver.VariableLengthParameterResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ClassResolverExtension;
import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ConverterWithBoolean;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodChecker;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.PureTypeBindingsConverter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolutionCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithName;
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
		bind(MethodResolver.class).to(MethodResolverImpl.class);
		bind(ClassResolverExtension.class).to(ClassResolverExtensionImpl.class);

		bind(new TypeLiteral<Converter<IVariableBinding>>() {
			/* empty */}).to(ToFieldNameConverter.class);
		bind(new TypeLiteral<Converter<IMethodBinding>>() {
			/* empty */}).to(ToMethodNameConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding>>() {
			/* empty */}).annotatedWith(Names.named("ToTypeNameConverter")).to(ToTypeNameConverter.class);
		bind(new TypeLiteral<Converter<ITypeBinding>>() {
			/* empty */}).annotatedWith(Names.named("ToTypeParameterNameConverter"))
				.to(ToTypeParameterNameConverter.class);

		bind(new TypeLiteral<ResolverWithCache<AdditionalField, IVariableBinding>>() {
			/* empty */}).to(AdditionalFieldResolver.class);
		bind(new TypeLiteral<ResolverWithCache<AdditionalLocalVariable, IVariableBinding>>() {
			/* empty */}).to(AdditionalLocalVariableResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Annotation, ITypeBinding>>() {
			/* empty */}).to(AnnotationResolver.class);
		bind(new TypeLiteral<ResolverWithCache<AnonymousClass, ITypeBinding>>() {
			/* empty */}).to(AnonymousClassResolver.class);
		bind(new TypeLiteral<ResolverWithCache<CatchParameter, IVariableBinding>>() {
			/* empty */}).to(CatchParameterResolver.class);
		bind(new TypeLiteral<ResolverWithCache<ClassMethod, IMethodBinding>>() {
			/* empty */}).to(ClassMethodResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Class, ITypeBinding>>() {
			/* empty */}).to(ClassResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Constructor, IMethodBinding>>() {
			/* empty */}).to(ConstructorResolver.class);
		bind(new TypeLiteral<ResolverWithCache<EnumConstant, IVariableBinding>>() {
			/* empty */}).to(EnumConstantResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Enumeration, ITypeBinding>>() {
			/* empty */}).to(EnumerationResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Field, IVariableBinding>>() {
			/* empty */}).to(FieldResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Module, IModuleBinding>>() {
			/* empty */}).to(ModuleResolver.class);
		bind(new TypeLiteral<ResolverWithCache<OrdinaryParameter, IVariableBinding>>() {
			/* empty */}).to(OrdinaryParameterResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Package, IPackageBinding>>() {
			/* empty */}).to(PackageResolver.class);
		bind(new TypeLiteral<ResolverWithCache<TypeParameter, ITypeBinding>>() {
			/* empty */}).to(TypeParameterResolver.class);
		bind(new TypeLiteral<ResolverWithCache<VariableLengthParameter, IVariableBinding>>() {
			/* empty */}).to(VariableLengthParameterResolver.class);
		bind(new TypeLiteral<ResolverWithCache<Interface, ITypeBinding>>() {
			/* empty */}).to(InterfaceResolver.class);
		bind(new TypeLiteral<ResolverWithCache<VariableLengthParameter, IVariableBinding>>() {
			/* empty */}).to(VariableLengthParameterResolver.class);
		bind(new TypeLiteral<ResolverWithCache<LocalVariable, IVariableBinding>>() {
			/* empty */}).to(LocalVariableResolver.class);
		bind(new TypeLiteral<ResolverWithCache<InterfaceMethod, IMethodBinding>>() {
			/* empty */}).to(InterfaceMethodResolver.class);

		bind(new TypeLiteral<Resolver<Classifier, ITypeBinding>>() {
			/* empty */}).to(ClassifierResolver.class);

		bind(new TypeLiteral<ConverterWithBoolean<IVariableBinding>>() {
			/* empty */}).to(ToParameterNameConverter.class);
		bind(new TypeLiteral<ResolverWithName<ReferenceableElement, IVariableBinding>>() {
			/* empty */}).to(ReferenceableElementResolver.class);

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
