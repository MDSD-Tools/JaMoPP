package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.common.notify.Notifier;
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

	private final HashSet<IModuleBinding> moduleBindings = new HashSet<>();
	private final HashSet<IPackageBinding> packageBindings = new HashSet<>();
	private final HashSet<ITypeBinding> typeBindings = new HashSet<>();
	private final HashSet<IMethodBinding> methodBindings = new HashSet<>();
	private final HashSet<IVariableBinding> variableBindings = new HashSet<>();
	private final HashSet<EObject> objVisited = new HashSet<>();

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

	private final MethodCompleter methodCompleter;

	private final PureTypeBindingsConverter pureTypeBindingsConverter;

	@Inject
	UtilJdtResolverImpl(ContainersFactory containersFactory, ClassifiersFactory classifiersFactory,
			TypesFactory typesFactory, StatementsFactory statementsFactory, MembersFactory membersFactory,
			VariablesFactory variablesFactory, ParametersFactory parametersFactory, GenericsFactory genericsFactory,
			Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			Provider<UtilBindingInfoToConcreteClassifierConverter> iUtilBindingInfoToConcreteClassifierConverter) {
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
		ordinaryParameterResolver = new OrdinaryParameterResolver(nameCache, new HashMap<>(), parametersFactory);
		additionalLocalVariableResolver = new AdditionalLocalVariableResolver(nameCache, new HashMap<>(),
				variablesFactory, variableBindings, this);
		variableLengthParameterResolver = new VariableLengthParameterResolver(nameCache, new HashMap<>(),
				variableBindings, this, parametersFactory);
		localVariableResolver = new LocalVariableResolver(nameCache, new HashMap<>(), variablesFactory,
				variableBindings, this);
		interfaceMethodResolver = new InterfaceMethodResolver(nameCache, new HashMap<>(), this, typesFactory,
				statementsFactory, methodBindings, membersFactory);

		methodCompleter = new MethodCompleter(this, methodBindings, EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS,
				anonymousClassResolver);
		pureTypeBindingsConverter = new PureTypeBindingsConverter(this, iUtilBindingInfoToConcreteClassifierConverter,
				packageResolver, moduleResolver, interfaceResolver, EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS,
				enumerationResolver, containersFactory, classResolver, bindingToPackageConverter,
				bindingToModuleConverter, annotationResolver, typeBindings, packageBindings, objVisited,
				moduleBindings);
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

	private String convertToFieldName(IVariableBinding binding) {
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
		variableBindings.add(binding);
		String paramName = convertToParameterName(binding, true);
		return getOrdinaryParameter(paramName);
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
		if (binding.isEnumConstant()) {
			return getEnumConstant(binding);
		}
		if (binding.isField()) {
			String fieldName = convertToFieldName(binding);
			if (fieldResolver.getBindings().containsKey(fieldName)) {
				return fieldResolver.getBindings().get(fieldName);
			}
			if (additionalFieldResolver.getBindings().containsKey(fieldName)) {
				return additionalFieldResolver.getBindings().get(fieldName);
			}
			return getField(binding);
		}
		if (binding.isParameter()) {
			String paramName = convertToParameterName(binding, false);
			if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
				return ordinaryParameterResolver.getBindings().get(paramName);
			}
			if (variableLengthParameterResolver.getBindings().containsKey(paramName)) {
				return variableLengthParameterResolver.getBindings().get(paramName);
			}
			return getOrdinaryParameter(binding);
		}
		String paramName = convertToParameterName(binding, false);
		if (catchParameterResolver.getBindings().containsKey(paramName)) {
			return catchParameterResolver.getBindings().get(paramName);
		}
		if (localVariableResolver.getBindings().containsKey(paramName)) {
			return localVariableResolver.getBindings().get(paramName);
		}
		if (additionalLocalVariableResolver.getBindings().containsKey(paramName)) {
			return additionalLocalVariableResolver.getBindings().get(paramName);
		}
		if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
			return ordinaryParameterResolver.getBindings().get(paramName);
		}
		return getLocalVariable(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferenceableElementByNameMatching(
			String name) {
		IVariableBinding vBinding = variableBindings.stream().filter(var -> var != null && var.getName().equals(name))
				.findFirst().orElse(null);
		if (vBinding != null) {
			return getReferencableElement(vBinding);
		}
		IMethodBinding mBinding = methodBindings.stream()
				.filter(meth -> !meth.isConstructor() && meth.getName().equals(name)).findFirst().orElse(null);
		if (mBinding != null) {
			return getMethod(mBinding);
		}
		ITypeBinding tBinding = typeBindings.stream().filter(type -> type != null && type.getName().equals(name))
				.findFirst().orElse(null);
		if (tBinding != null) {
			return getClassifier(tBinding);
		}
		tools.mdsd.jamopp.model.java.variables.Variable par = catchParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		par = localVariableResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable addLocVar = additionalLocalVariableResolver
				.getBindings().values().stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addLocVar != null) {
			return addLocVar;
		}
		par = variableLengthParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		par = ordinaryParameterResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.members.EnumConstant enumConst = enumConstantResolver.getBindings().values()
				.stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (enumConst != null) {
			return enumConst;
		}
		tools.mdsd.jamopp.model.java.members.Field field = fieldResolver.getBindings().values().stream()
				.filter(param -> param != null && param.getName().equals(name)).findFirst().orElse(null);
		if (field != null) {
			return field;
		}
		tools.mdsd.jamopp.model.java.members.AdditionalField addField = additionalFieldResolver.getBindings().values()
				.stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addField != null) {
			return addField;
		}
		tools.mdsd.jamopp.model.java.members.Method meth = classMethodResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (meth != null) {
			return meth;
		}
		meth = interfaceMethodResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst().orElse(null);
		if (meth != null) {
			return meth;
		}
		tools.mdsd.jamopp.model.java.classifiers.Classifier c = typeParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (c != null) {
			return c;
		}
		c = enumerationResolver.getBindings().values().stream().filter(param -> name.equals(param.getName()))
				.findFirst().orElse(null);
		if (c != null) {
			return c;
		}
		c = annotationResolver.getBindings().values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = classResolver.getBindings().values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = interfaceResolver.getBindings().values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		return getClass(name);
	}

	@Override
	@SuppressWarnings("unused")
	public void completeResolution() {
		enumConstantResolver.getBindings().forEach((constName, enConst) -> {
			if (enConst.eContainer() == null) {
				IVariableBinding varBind = variableBindings.stream()
						.filter(var -> var != null && constName.equals(convertToFieldName(var))).findFirst().get();
				if (!varBind.getDeclaringClass().isAnonymous()) {
					var en = getEnumeration(varBind.getDeclaringClass());
					if (!EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS && !en.getConstants().contains(enConst)) {
						en.getConstants().add(enConst);
					}
				}
			}
		});

		fieldResolver.getBindings().forEach((fieldName, field) -> {
			if (field.eContainer() == null) {
				IVariableBinding varBind = variableBindings.stream()
						.filter(var -> var != null && fieldName.equals(convertToFieldName(var))).findFirst()
						.orElse(null);
				if (varBind == null || varBind.getDeclaringClass() == null) {
					addToSyntheticClass(field);
				} else {
					tools.mdsd.jamopp.model.java.classifiers.Classifier cla = getClassifier(
							varBind.getDeclaringClass());
					if (cla == null) {
						String typeName = convertToTypeName(varBind.getDeclaringClass());
						if (anonymousClassResolver.getBindings().containsKey(typeName)) {
							tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
									.getBindings().get(typeName);
							if (!anonClass.getMembers().contains(field)) {
								anonClass.getMembers().add(field);
							}
						} else {
							addToSyntheticClass(field);
						}
					} else if (!EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS
							&& cla instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier i
							&& !i.getMembers().contains(field)) {
						i.getMembers().add(field);
					}
				}
			}
		});

		constructorResolver.getBindings().forEach((t, u) -> methodCompleter.completeMethod(t, u));
		classMethodResolver.getBindings().forEach((t, u) -> methodCompleter.completeMethod(t, u));
		interfaceMethodResolver.getBindings().forEach((t, u) -> methodCompleter.completeMethod(t, u));

		pureTypeBindingsConverter.convertPureTypeBindings(resourceSet);

		moduleResolver.getBindings().values().forEach(module -> JavaClasspath.get().registerModule(module));
		packageResolver.getBindings().values().forEach(pack -> JavaClasspath.get().registerPackage(pack));
		annotationResolver.getBindings().values().forEach(ann -> JavaClasspath.get().registerConcreteClassifier(ann));
		enumerationResolver.getBindings().values()
				.forEach(enume -> JavaClasspath.get().registerConcreteClassifier(enume));
		interfaceResolver.getBindings().values()
				.forEach(interf -> JavaClasspath.get().registerConcreteClassifier(interf));
		classResolver.getBindings().values().forEach(clazz -> JavaClasspath.get().registerConcreteClassifier(clazz));

		escapeAllIdentifiers();

		moduleResolver.getBindings().clear();
		packageResolver.getBindings().clear();
		annotationResolver.getBindings().clear();
		enumerationResolver.getBindings().clear();
		interfaceResolver.getBindings().clear();
		classResolver.getBindings().clear();
		typeParameterResolver.getBindings().clear();
		classMethodResolver.getBindings().clear();
		constructorResolver.getBindings().clear();
		fieldResolver.getBindings().clear();
		interfaceMethodResolver.getBindings().clear();
		additionalFieldResolver.getBindings().clear();
		localVariableResolver.getBindings().clear();
		additionalLocalVariableResolver.getBindings().clear();
		enumConstantResolver.getBindings().clear();
		variableLengthParameterResolver.getBindings().clear();
		ordinaryParameterResolver.getBindings().clear();
		catchParameterResolver.getBindings().clear();
		moduleBindings.clear();
		packageBindings.clear();
		typeBindings.clear();
		methodBindings.clear();
		variableBindings.clear();
		uid = 0;
		varBindToUid.clear();
		objVisited.clear();
		nameCache.clear();
		anonymousClassResolver.getBindings().clear();
	}

	public void addToSyntheticClass(tools.mdsd.jamopp.model.java.members.Member member) {
		tools.mdsd.jamopp.model.java.classifiers.Class container = getClass(SYNTH_CLASS);
		container.setName(SYNTH_CLASS);
		if (!container.getMembers().contains(member)) {
			container.getMembers().add(member);
		}
	}

	private void escapeAllIdentifiers() {
		moduleResolver.getBindings().values().forEach(this::escapeIdentifiers);
		packageResolver.getBindings().values().forEach(this::escapeIdentifiers);
		annotationResolver.getBindings().values().forEach(this::escapeIdentifiers);
		enumerationResolver.getBindings().values().forEach(this::escapeIdentifiers);
		classResolver.getBindings().values().forEach(this::escapeIdentifiers);
		interfaceResolver.getBindings().values().forEach(this::escapeIdentifiers);
	}

	private void escapeIdentifiers(EObject obj) {
		obj.eAllContents().forEachRemaining(this::escapeIdentifier);
	}

	private void escapeIdentifier(Notifier not) {
		if (not instanceof tools.mdsd.jamopp.model.java.commons.NamedElement ele) {
			StringBuilder builder = new StringBuilder();
			String name = ele.getName();
			name.codePoints().forEach(i -> {
				if (i <= 0x20 || Character.MIN_SURROGATE <= i && i <= Character.MAX_SURROGATE) {
					builder.append("\\u" + String.format("%04x", i));
				} else {
					builder.appendCodePoint(i);
				}
			});
			ele.setName(builder.toString());
		}
	}
}