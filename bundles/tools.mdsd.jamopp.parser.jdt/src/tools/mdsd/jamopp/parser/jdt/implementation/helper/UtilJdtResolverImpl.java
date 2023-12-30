package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AdditionalFieldResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AdditionalLocalVariableResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AnnotationResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AnonymousClassResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.CatchParameterResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassMethodResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassifierResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ConstructorResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.EnumConstantResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.EnumerationResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.FieldResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.InterfaceMethodResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.InterfaceResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.LocalVariableResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.MethodResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ModuleResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.OrdinaryParameterResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.PackageResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ReferenceableElementResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ResolutionCompleter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ToFieldNameConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ToMethodNameConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ToParameterNameConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ToTypeNameConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.TypeParameterResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.VariableLengthParameterResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class UtilJdtResolverImpl implements UtilJdtResolver {

	private ResourceSet resourceSet;
	private int uid;

	private ResolutionCompleter resolutionCompleter;
	private ToFieldNameConverter toFieldNameConverter;
	private ToParameterNameConverter toParameterNameConverter;
	private ToTypeNameConverter toTypeNameConverter;
	private ToMethodNameConverter toMethodNameConverter;
	private ClassifierResolver classifierResolver;
	private MethodResolver methodResolver;
	private ModuleResolver moduleResolver;
	private PackageResolver packageResolver;
	private AnnotationResolver annotationResolver;
	private EnumerationResolver enumerationResolver;
	private InterfaceResolver interfaceResolver;
	private ClassResolver classResolver;
	private TypeParameterResolver typeParameterResolver;
	private ClassMethodResolver classMethodResolver;
	private ConstructorResolver constructorResolver;
	private FieldResolver fieldResolver;
	private AnonymousClassResolver anonymousClassResolver;
	private EnumConstantResolver enumConstantResolver;
	private AdditionalFieldResolver additionalFieldResolver;
	private CatchParameterResolver catchParameterResolver;
	private OrdinaryParameterResolver ordinaryParameterResolver;
	private AdditionalLocalVariableResolver additionalLocalVariableResolver;
	private VariableLengthParameterResolver variableLengthParameterResolver;
	private LocalVariableResolver localVariableResolver;
	private InterfaceMethodResolver interfaceMethodResolver;
	private ReferenceableElementResolver referenceableElementResolver;

	@Inject
	public void UtilJdtResolverImpl(VariableLengthParameterResolver variableLengthParameterResolver,
			TypeParameterResolver typeParameterResolver, ToTypeNameConverter toTypeNameConverter,
			ToParameterNameConverter toParameterNameConverter, ToMethodNameConverter toMethodNameConverter,
			ToFieldNameConverter toFieldNameConverter, ResolutionCompleter resolutionCompleter,
			ReferenceableElementResolver referenceableElementResolver, PackageResolver packageResolver,
			OrdinaryParameterResolver ordinaryParameterResolver, ModuleResolver moduleResolver,
			MethodResolver methodResolver, LocalVariableResolver localVariableResolver,
			InterfaceResolver interfaceResolver, InterfaceMethodResolver interfaceMethodResolver,
			FieldResolver fieldResolver, EnumerationResolver enumerationResolver,
			EnumConstantResolver enumConstantResolver, ConstructorResolver constructorResolver,
			ClassifierResolver classifierResolver, ClassResolver classResolver, ClassMethodResolver classMethodResolver,
			CatchParameterResolver catchParameterResolver, AnonymousClassResolver anonymousClassResolver,
			AnnotationResolver annotationResolver, AdditionalLocalVariableResolver additionalLocalVariableResolver,
			AdditionalFieldResolver additionalFieldResolver) {
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
	}

	@Override
	public void setResourceSet(ResourceSet set) {
		resourceSet = set;
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module getModule(IModuleBinding binding) {
		return moduleResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module getModule(String modName) {
		return moduleResolver.getByName(modName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Package getPackage(IPackageBinding binding) {
		return packageResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Package getPackage(String packageName) {
		return packageResolver.getByName(packageName);
	}

	public String convertToTypeName(ITypeBinding binding) {
		return toTypeNameConverter.convertToTypeName(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(ITypeBinding binding) {
		return annotationResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(String annotName) {
		return annotationResolver.getByName(annotName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Enumeration getEnumeration(ITypeBinding binding) {
		return enumerationResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(ITypeBinding binding) {
		return classResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Interface getInterface(ITypeBinding binding) {
		return interfaceResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.generics.TypeParameter getTypeParameter(ITypeBinding binding) {
		return typeParameterResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Classifier getClassifier(ITypeBinding binding) {
		return classifierResolver.getClassifier(binding);
	}

	public String convertToMethodName(IMethodBinding binding) {
		return toMethodNameConverter.convertToMethodName(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(String methodName) {
		return interfaceMethodResolver.getByName(methodName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(IMethodBinding binding) {
		return interfaceMethodResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(String methodName) {
		return classMethodResolver.getByName(methodName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(IMethodBinding binding) {
		return classMethodResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(IMethodBinding binding) {
		return constructorResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(String methName) {
		return constructorResolver.getByName(methName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Method getMethod(IMethodBinding binding) {
		return methodResolver.getMethod(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(String typeName) {
		return classResolver.getByName(typeName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(String typeName) {
		return anonymousClassResolver.getByName(typeName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(ITypeBinding binding) {
		return anonymousClassResolver.getByBinding(binding);
	}

	public String convertToFieldName(IVariableBinding binding) {
		return toFieldNameConverter.convertToFieldName(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Field getField(String name) {
		return fieldResolver.getByName(name);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Field getField(IVariableBinding binding) {
		return fieldResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(IVariableBinding binding) {
		return enumConstantResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(String enumCN) {
		return enumConstantResolver.getByName(enumCN);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(String name) {
		return additionalFieldResolver.getByName(name);
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(IVariableBinding binding) {
		return additionalFieldResolver.getByBinding(binding);
	}

	public String convertToParameterName(IVariableBinding binding, boolean register) {
		return toParameterNameConverter.convertToParameterName(binding, register, uid);
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(IVariableBinding binding) {
		return localVariableResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(String varName) {
		return localVariableResolver.getByName(varName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(
			IVariableBinding binding) {
		return additionalLocalVariableResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(String varName) {
		return additionalLocalVariableResolver.getByName(varName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(IVariableBinding binding) {
		return ordinaryParameterResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(String paramName) {
		return ordinaryParameterResolver.getByName(paramName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter getVariableLengthParameter(
			IVariableBinding binding) {
		return variableLengthParameterResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(IVariableBinding binding) {
		return catchParameterResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(String paramName) {
		return catchParameterResolver.getByName(paramName);
	}

	@Override
	public void prepareNextUid() {
		uid++;
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferencableElement(
			IVariableBinding binding) {
		return referenceableElementResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferenceableElementByNameMatching(
			String name) {
		return referenceableElementResolver.getByName(name);
	}

	@Override
	public void completeResolution() {
		resolutionCompleter.completeResolution(resourceSet);
	}

}