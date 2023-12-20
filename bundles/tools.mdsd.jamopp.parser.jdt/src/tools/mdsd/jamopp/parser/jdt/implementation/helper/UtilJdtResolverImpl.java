package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AdditionalFieldResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AdditionalLocalVariableResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AnnotationResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AnonymousClassResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.CatchParameterResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassMethodResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ConstructorResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.EnumConstantResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.EnumerationResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.FieldResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.InterfaceMethodResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.InterfaceResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.LocalVariableResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.MethodCompleter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ModuleResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.OrdinaryParameterResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.PackageResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.PureTypeBindingsConverter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ReferenceableElementResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ResolutionCompleter;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.TypeParameterResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.VariableLengthParameterResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class UtilJdtResolverImpl implements UtilJdtResolver {

	private static final String SYNTH_CLASS = "SyntheticContainerClass";
	private static final boolean EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS = true;

	private ResourceSet resourceSet;
	private int uid;

	private final HashMap<IVariableBinding, Integer> varBindToUid = new HashMap<>();
	private final HashMap<IBinding, String> nameCache = new HashMap<>();

	private final ResolutionCompleter resolutionCompleter;

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

	@Inject
	UtilJdtResolverImpl(ContainersFactory containersFactory, ClassifiersFactory classifiersFactory,
			TypesFactory typesFactory, StatementsFactory statementsFactory, MembersFactory membersFactory,
			VariablesFactory variablesFactory, ParametersFactory parametersFactory, GenericsFactory genericsFactory,
			Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			Provider<UtilBindingInfoToConcreteClassifierConverter> iUtilBindingInfoToConcreteClassifierConverter) {

		HashSet<IModuleBinding> moduleBindings = new HashSet<>();
		HashSet<IPackageBinding> packageBindings = new HashSet<>();
		HashSet<ITypeBinding> typeBindings = new HashSet<>();
		HashSet<IMethodBinding> methodBindings = new HashSet<>();
		HashSet<IVariableBinding> variableBindings = new HashSet<>();
		HashSet<EObject> objVisited = new HashSet<>();

		moduleResolver = new ModuleResolver(nameCache, new HashMap<>(), moduleBindings, containersFactory);
		packageResolver = new PackageResolver(nameCache, new HashMap<>(), packageBindings, containersFactory);
		annotationResolver = new AnnotationResolver(nameCache, new HashMap<>(), typeBindings, classifiersFactory);
		enumerationResolver = new EnumerationResolver(nameCache, new HashMap<>(), typeBindings, classifiersFactory);
		interfaceResolver = new InterfaceResolver(nameCache, new HashMap<>(), typeBindings, classifiersFactory);
		classResolver = new ClassResolver(nameCache, new HashMap<>(), classifiersFactory, this, typeBindings);
		typeParameterResolver = new TypeParameterResolver(nameCache, new HashMap<>(), typeBindings, genericsFactory);
		classMethodResolver = new ClassMethodResolver(nameCache, new HashMap<>(), this, typesFactory, statementsFactory,
				methodBindings, membersFactory);
		constructorResolver = new ConstructorResolver(nameCache, new HashMap<>(), statementsFactory, methodBindings,
				membersFactory, this);
		fieldResolver = new FieldResolver(nameCache, new HashMap<>(), variableBindings, this, typesFactory,
				membersFactory);
		anonymousClassResolver = new AnonymousClassResolver(nameCache, new HashMap<>(), classifiersFactory);
		enumConstantResolver = new EnumConstantResolver(nameCache, new HashMap<>(), variableBindings, membersFactory,
				enumerationResolver);
		additionalFieldResolver = new AdditionalFieldResolver(nameCache, new HashMap<>(), variableBindings, this,
				membersFactory);
		catchParameterResolver = new CatchParameterResolver(nameCache, new HashMap<>(), variableBindings,
				parametersFactory, this);
		ordinaryParameterResolver = new OrdinaryParameterResolver(nameCache, new HashMap<>(), parametersFactory,
				variableBindings, this);
		additionalLocalVariableResolver = new AdditionalLocalVariableResolver(nameCache, new HashMap<>(),
				variablesFactory, variableBindings, this);
		variableLengthParameterResolver = new VariableLengthParameterResolver(nameCache, new HashMap<>(),
				variableBindings, this, parametersFactory);
		localVariableResolver = new LocalVariableResolver(nameCache, new HashMap<>(), variablesFactory,
				variableBindings, this);
		interfaceMethodResolver = new InterfaceMethodResolver(nameCache, new HashMap<>(), this, typesFactory,
				statementsFactory, methodBindings, membersFactory);

		MethodCompleter methodCompleter = new MethodCompleter(this, methodBindings,
				EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS, anonymousClassResolver);
		PureTypeBindingsConverter pureTypeBindingsConverter = new PureTypeBindingsConverter(this,
				iUtilBindingInfoToConcreteClassifierConverter, packageResolver, moduleResolver, interfaceResolver,
				EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS, enumerationResolver, containersFactory,
				classResolver, bindingToPackageConverter, bindingToModuleConverter, annotationResolver, typeBindings,
				packageBindings, objVisited, moduleBindings);
		referenceableElementResolver = new ReferenceableElementResolver(variableLengthParameterResolver,
				variableBindings, this, typeParameterResolver, typeBindings, ordinaryParameterResolver, methodBindings,
				localVariableResolver, interfaceResolver, interfaceMethodResolver, fieldResolver, enumerationResolver,
				enumConstantResolver, classResolver, classMethodResolver, catchParameterResolver, annotationResolver,
				additionalLocalVariableResolver, additionalFieldResolver);
		resolutionCompleter = new ResolutionCompleter(variableLengthParameterResolver, variableBindings, varBindToUid,
				this, typeParameterResolver, typeBindings, pureTypeBindingsConverter, packageResolver, packageBindings,
				ordinaryParameterResolver, objVisited, nameCache, moduleResolver, moduleBindings, methodCompleter,
				methodBindings, localVariableResolver, interfaceResolver, interfaceMethodResolver, fieldResolver,
				EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS, enumerationResolver, enumConstantResolver,
				SYNTH_CLASS, constructorResolver, classResolver, classMethodResolver, catchParameterResolver,
				anonymousClassResolver, annotationResolver, additionalLocalVariableResolver, additionalFieldResolver);
	}

	public void addToSyntheticClass(tools.mdsd.jamopp.model.java.members.Member member) {
		tools.mdsd.jamopp.model.java.classifiers.Class container = getClass(SYNTH_CLASS);
		container.setName(SYNTH_CLASS);
		if (!container.getMembers().contains(member)) {
			container.getMembers().add(member);
		}
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
		if (binding == null) {
			return "";
		}
		if (binding.isTypeVariable()) {
			return binding.getName();
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String qualifiedName;
		if (binding.isMember()) {
			qualifiedName = convertToTypeName(binding.getDeclaringClass()) + "." + binding.getName();
		} else if (binding.isLocal()) {
			IBinding b = binding.getDeclaringMember();
			if (b instanceof IMethodBinding) {
				qualifiedName = convertToMethodName((IMethodBinding) b) + "." + binding.getKey();
			} else if (b instanceof IVariableBinding) {
				qualifiedName = convertToFieldName((IVariableBinding) b) + "." + binding.getKey();
			} else {
				qualifiedName = binding.getKey();
			}
			nameCache.put(binding, qualifiedName);
			return qualifiedName;
		} else {
			qualifiedName = binding.getQualifiedName();
		}
		if (qualifiedName.contains("<")) {
			qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf("<"));
		}
		nameCache.put(binding, qualifiedName);
		return qualifiedName;
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
		String typeName = convertToTypeName(binding);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass != null) {
			return potClass;
		}
		if (binding.isAnonymous() || binding.isLocal() && binding.getDeclaringMember() == null
				|| anonymousClassResolver.getBindings().containsKey(convertToTypeName(binding))) {
			return null;
		}
		if (binding.isAnnotation()) {
			return getAnnotation(binding);
		}
		if (binding.isInterface()) {
			return getInterface(binding);
		}
		if (binding.isEnum()) {
			return getEnumeration(binding);
		}
		if (binding.isClass()) {
			return getClass(binding);
		}
		if (binding.isTypeVariable()) {
			return getTypeParameter(binding);
		}
		if (binding.isArray()) {
			return getClassifier(binding.getElementType());
		}
		return null;
	}

	public String convertToMethodName(IMethodBinding binding) {
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		binding = binding.getMethodDeclaration();
		StringBuilder builder = new StringBuilder();
		builder.append(convertToTypeName(binding.getDeclaringClass()));
		builder.append("::");
		builder.append(binding.getName());
		builder.append("(");
		for (ITypeBinding p : binding.getParameterTypes()) {
			builder.append(convertToTypeName(p));
			for (int i = 0; i < p.getDimensions(); i++) {
				builder.append("[]");
			}
		}
		builder.append(")");
		if ("java.lang.Object::clone()".equals(builder.toString()) && binding.getReturnType().isArray()) {
			builder.append("java.lang.Object");
		} else {
			builder.append(convertToTypeName(binding.getReturnType()));
		}
		String name = builder.toString();
		nameCache.put(binding, name);
		return name;
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
		if (binding.getDeclaringClass().isInterface()) {
			return getInterfaceMethod(binding);
		}
		return getClassMethod(binding);
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
		String typeName = convertToTypeName(binding);
		return getAnonymousClass(typeName);
	}

	public String convertToFieldName(IVariableBinding binding) {
		if (binding == null || !binding.isField()) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String name = convertToTypeName(binding.getDeclaringClass()) + "::" + binding.getName();
		nameCache.put(binding, name);
		return name;
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
		if (binding == null) {
			return "";
		}
		if (nameCache.containsKey(binding)) {
			return nameCache.get(binding);
		}
		String prefix = "";
		if (binding.getDeclaringMethod() != null) {
			prefix = convertToMethodName(binding.getDeclaringMethod());
		} else if (varBindToUid.containsKey(binding)) {
			prefix = varBindToUid.get(binding) + "";
		} else {
			prefix = uid + "";
			if (register) {
				varBindToUid.put(binding, uid);
			}
		}
		String name = prefix + "::" + binding.getName() + "::" + binding.getVariableId() + binding.hashCode();
		nameCache.put(binding, name);
		return name;
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