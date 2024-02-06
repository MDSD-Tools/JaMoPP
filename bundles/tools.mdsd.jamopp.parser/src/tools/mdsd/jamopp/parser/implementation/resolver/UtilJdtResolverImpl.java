package tools.mdsd.jamopp.parser.implementation.resolver;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

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
import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolutionCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithName;
import tools.mdsd.jamopp.parser.interfaces.resolver.UidManager;

/**
 * Facade for whole package. This is the only class that should be used outside
 * this package.
 */
public class UtilJdtResolverImpl implements JdtResolver {

	private ResourceSet resourceSet;

	private final ResolutionCompleter resolutionCompleterImpl;
	private final Converter<IMethodBinding> toMethodNameConverter;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;
	private final MethodResolver methodResolverImpl;
	private final ResolverWithCache<Module, IModuleBinding> moduleResolver;
	private final ResolverWithCache<Package, IPackageBinding> packageResolver;
	private final ResolverWithCache<Annotation, ITypeBinding> annotationResolver;
	private final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver;
	private final ResolverWithCache<Interface, ITypeBinding> interfaceResolver;
	private final ResolverWithCache<Class, ITypeBinding> classResolver;
	private final ResolverWithCache<TypeParameter, ITypeBinding> typeParameterResolver;
	private final ResolverWithCache<ClassMethod, IMethodBinding> classMethodResolver;
	private final ResolverWithCache<Constructor, IMethodBinding> constructorResolver;
	private final ResolverWithCache<Field, IVariableBinding> fieldResolver;
	private final ResolverWithCache<AnonymousClass, ITypeBinding> anonymousClassResolver;
	private final ResolverWithCache<EnumConstant, IVariableBinding> enumConstantResolver;
	private final ResolverWithCache<AdditionalField, IVariableBinding> additionalFieldResolver;
	private final ResolverWithCache<CatchParameter, IVariableBinding> catchParameterResolver;
	private final ResolverWithCache<OrdinaryParameter, IVariableBinding> ordinaryParameterResolver;
	private final ResolverWithCache<AdditionalLocalVariable, IVariableBinding> additionalLocalVariableResolver;
	private final ResolverWithCache<VariableLengthParameter, IVariableBinding> variableLengthParameterResolver;
	private final ResolverWithCache<LocalVariable, IVariableBinding> localVariableResolver;
	private final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver;
	private final ResolverWithName<ReferenceableElement, IVariableBinding> referenceableElementResolver;
	private final UidManager uidManagerImpl;

	@Inject
	public UtilJdtResolverImpl(final ResolutionCompleter resolutionCompleterImpl,
			final Converter<IMethodBinding> toMethodNameConverter,
			final Resolver<Classifier, ITypeBinding> classifierResolver, final MethodResolver methodResolverImpl,
			final ResolverWithCache<Module, IModuleBinding> moduleResolver,
			final ResolverWithCache<Package, IPackageBinding> packageResolver,
			final ResolverWithCache<Annotation, ITypeBinding> annotationResolver,
			final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver,
			final ResolverWithCache<Interface, ITypeBinding> interfaceResolver,
			final ResolverWithCache<Class, ITypeBinding> classResolver,
			final ResolverWithCache<TypeParameter, ITypeBinding> typeParameterResolver,
			final ResolverWithCache<ClassMethod, IMethodBinding> classMethodResolver,
			final ResolverWithCache<Constructor, IMethodBinding> constructorResolver,
			final ResolverWithCache<Field, IVariableBinding> fieldResolver,
			final ResolverWithCache<AnonymousClass, ITypeBinding> anonymousClassResolver,
			final ResolverWithCache<EnumConstant, IVariableBinding> enumConstantResolver,
			final ResolverWithCache<AdditionalField, IVariableBinding> additionalFieldResolver,
			final ResolverWithCache<CatchParameter, IVariableBinding> catchParameterResolver,
			final ResolverWithCache<OrdinaryParameter, IVariableBinding> ordinaryParameterResolver,
			final ResolverWithCache<AdditionalLocalVariable, IVariableBinding> additionalLocalVariableResolver,
			final ResolverWithCache<VariableLengthParameter, IVariableBinding> variableLengthParameterResolver,
			final ResolverWithCache<LocalVariable, IVariableBinding> localVariableResolver,
			final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver,
			final ResolverWithName<ReferenceableElement, IVariableBinding> referenceableElementResolver,
			final UidManager uidManagerImpl) {
		this.resolutionCompleterImpl = resolutionCompleterImpl;
		this.toMethodNameConverter = toMethodNameConverter;
		this.classifierResolver = classifierResolver;
		this.methodResolverImpl = methodResolverImpl;
		this.moduleResolver = moduleResolver;
		this.packageResolver = packageResolver;
		this.annotationResolver = annotationResolver;
		this.enumerationResolver = enumerationResolver;
		this.interfaceResolver = interfaceResolver;
		this.classResolver = classResolver;
		this.typeParameterResolver = typeParameterResolver;
		this.classMethodResolver = classMethodResolver;
		this.constructorResolver = constructorResolver;
		this.fieldResolver = fieldResolver;
		this.anonymousClassResolver = anonymousClassResolver;
		this.enumConstantResolver = enumConstantResolver;
		this.additionalFieldResolver = additionalFieldResolver;
		this.catchParameterResolver = catchParameterResolver;
		this.ordinaryParameterResolver = ordinaryParameterResolver;
		this.additionalLocalVariableResolver = additionalLocalVariableResolver;
		this.variableLengthParameterResolver = variableLengthParameterResolver;
		this.localVariableResolver = localVariableResolver;
		this.interfaceMethodResolver = interfaceMethodResolver;
		this.referenceableElementResolver = referenceableElementResolver;
		this.uidManagerImpl = uidManagerImpl;
	}

	@Override
	public void setResourceSet(final ResourceSet set) {
		resourceSet = set;
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module getModule(final IModuleBinding binding) {
		return moduleResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module getModule(final String modName) {
		return moduleResolver.getByName(modName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Package getPackage(final IPackageBinding binding) {
		return packageResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Package getPackage(final String packageName) {
		return packageResolver.getByName(packageName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(final ITypeBinding binding) {
		return annotationResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(final String annotName) {
		return annotationResolver.getByName(annotName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Enumeration getEnumeration(final ITypeBinding binding) {
		return enumerationResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(final ITypeBinding binding) {
		return classResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Interface getInterface(final ITypeBinding binding) {
		return interfaceResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.generics.TypeParameter getTypeParameter(final ITypeBinding binding) {
		return typeParameterResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Classifier getClassifier(final ITypeBinding binding) {
		return classifierResolver.getByBinding(binding);
	}

	public String convertToMethodName(final IMethodBinding binding) {
		return toMethodNameConverter.convert(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(final String methodName) {
		return interfaceMethodResolver.getByName(methodName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(final IMethodBinding binding) {
		return interfaceMethodResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(final String methodName) {
		return classMethodResolver.getByName(methodName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(final IMethodBinding binding) {
		return classMethodResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(final IMethodBinding binding) {
		return constructorResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(final String methName) {
		return constructorResolver.getByName(methName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Method getMethod(final IMethodBinding binding) {
		return methodResolverImpl.getMethod(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(final String typeName) {
		return classResolver.getByName(typeName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(final String typeName) {
		return anonymousClassResolver.getByName(typeName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(final ITypeBinding binding) {
		return anonymousClassResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Field getField(final String name) {
		return fieldResolver.getByName(name);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Field getField(final IVariableBinding binding) {
		return fieldResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(final IVariableBinding binding) {
		return enumConstantResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(final String enumCN) {
		return enumConstantResolver.getByName(enumCN);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(final String name) {
		return additionalFieldResolver.getByName(name);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(final IVariableBinding binding) {
		return additionalFieldResolver.getByBinding(binding);
	}

	@Override
	public LocalVariable getLocalVariable(final IVariableBinding binding) {
		return localVariableResolver.getByBinding(binding);
	}

	@Override
	public LocalVariable getLocalVariable(final String varName) {
		return localVariableResolver.getByName(varName);
	}

	@Override
	public AdditionalLocalVariable getAdditionalLocalVariable(final IVariableBinding binding) {
		return additionalLocalVariableResolver.getByBinding(binding);
	}

	@Override
	public AdditionalLocalVariable getAdditionalLocalVariable(final String varName) {
		return additionalLocalVariableResolver.getByName(varName);
	}

	@Override
	public OrdinaryParameter getOrdinaryParameter(final IVariableBinding binding) {
		return ordinaryParameterResolver.getByBinding(binding);
	}

	@Override
	public OrdinaryParameter getOrdinaryParameter(final String paramName) {
		return ordinaryParameterResolver.getByName(paramName);
	}

	@Override
	public VariableLengthParameter getVariableLengthParameter(final IVariableBinding binding) {
		return variableLengthParameterResolver.getByBinding(binding);
	}

	@Override
	public CatchParameter getCatchParameter(final IVariableBinding binding) {
		return catchParameterResolver.getByBinding(binding);
	}

	@Override
	public CatchParameter getCatchParameter(final String paramName) {
		return catchParameterResolver.getByName(paramName);
	}

	@Override
	public void prepareNextUid() {
		uidManagerImpl.prepareNextUid();
	}

	@Override
	public ReferenceableElement getReferencableElement(final IVariableBinding binding) {
		return referenceableElementResolver.getByBinding(binding);
	}

	@Override
	public ReferenceableElement getReferenceableElementByNameMatching(final String name) {
		return referenceableElementResolver.getByName(name);
	}

	@Override
	public void completeResolution() {
		resolutionCompleterImpl.completeResolution(resourceSet);
	}

}