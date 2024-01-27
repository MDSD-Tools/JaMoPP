package tools.mdsd.jamopp.parser.implementation.resolver;

import com.google.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class UtilJdtResolverImpl implements JdtResolver {

	private ResourceSet resourceSet;

	private final ResolutionCompleter resolutionCompleter;
	private final ToFieldNameConverter toFieldNameConverter;
	private final ToParameterNameConverter toParameterNameConverter;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ToMethodNameConverter toMethodNameConverter;
	private final ClassifierResolver classifierResolver;
	private final MethodResolver methodResolver;
	private final ModuleResolver moduleResolver;
	private final PackageResolver packageResolver;
	private final AnnotationResolver annotationResolver;
	private final EnumerationResolver enumerationResolver;
	private final InterfaceResolver interfaceResolver;
	private final ClassResolver classResolver;
	private final TypeParameterResolver typeParameterResolver;
	private final ClassMethodResolver classMethodResolver;
	private final ConstructorResolver constructorResolver;
	private final FieldResolver fieldResolver;
	private final AnonymousClassResolver anonymousClassResolver;
	private final EnumConstantResolver enumConstantResolver;
	private final AdditionalFieldResolver additionalFieldResolver;
	private final CatchParameterResolver catchParameterResolver;
	private final OrdinaryParameterResolver ordinaryParameterResolver;
	private final AdditionalLocalVariableResolver additionalLocalVariableResolver;
	private final VariableLengthParameterResolver variableLengthParameterResolver;
	private final LocalVariableResolver localVariableResolver;
	private final InterfaceMethodResolver interfaceMethodResolver;
	private final ReferenceableElementResolver referenceableElementResolver;
	private final UidManager uidManager;

	@Inject
	public UtilJdtResolverImpl(final VariableLengthParameterResolver variableLengthParameterResolver,
			final TypeParameterResolver typeParameterResolver, final ToTypeNameConverter toTypeNameConverter,
			final ToParameterNameConverter toParameterNameConverter, final ToMethodNameConverter toMethodNameConverter,
			final ToFieldNameConverter toFieldNameConverter, final ResolutionCompleter resolutionCompleter,
			final ReferenceableElementResolver referenceableElementResolver, final PackageResolver packageResolver,
			final OrdinaryParameterResolver ordinaryParameterResolver, final ModuleResolver moduleResolver,
			final MethodResolver methodResolver, final LocalVariableResolver localVariableResolver,
			final InterfaceResolver interfaceResolver, final InterfaceMethodResolver interfaceMethodResolver,
			final FieldResolver fieldResolver, final EnumerationResolver enumerationResolver,
			final EnumConstantResolver enumConstantResolver, final ConstructorResolver constructorResolver,
			final ClassifierResolver classifierResolver, final ClassResolver classResolver,
			final ClassMethodResolver classMethodResolver, final CatchParameterResolver catchParameterResolver,
			final AnonymousClassResolver anonymousClassResolver, final AnnotationResolver annotationResolver,
			final AdditionalLocalVariableResolver additionalLocalVariableResolver,
			final AdditionalFieldResolver additionalFieldResolver, final UidManager uidManager) {
		this.resolutionCompleter = resolutionCompleter;
		this.toFieldNameConverter = toFieldNameConverter;
		this.toParameterNameConverter = toParameterNameConverter;
		this.toTypeNameConverter = toTypeNameConverter;
		this.toMethodNameConverter = toMethodNameConverter;
		this.classifierResolver = classifierResolver;
		this.methodResolver = methodResolver;
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
		this.uidManager = uidManager;
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

	public String convertToTypeName(final ITypeBinding binding) {
		return toTypeNameConverter.convertToTypeName(binding);
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
		return classifierResolver.getClassifier(binding);
	}

	public String convertToMethodName(final IMethodBinding binding) {
		return toMethodNameConverter.convertToMethodName(binding);
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
		return methodResolver.getMethod(binding);
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

	public String convertToFieldName(final IVariableBinding binding) {
		return toFieldNameConverter.convertToFieldName(binding);
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

	public String convertToParameterName(final IVariableBinding binding, final boolean register) {
		return toParameterNameConverter.convertToParameterName(binding, register);
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
		uidManager.prepareNextUid();
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
		resolutionCompleter.completeResolution(resourceSet);
	}

}