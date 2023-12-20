package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
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
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AdditionalFieldResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AnnotationResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.AnonymousClassResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassMethodResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ClassResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ConstructorResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.EnumConstantResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.EnumerationResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.FieldResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.InterfaceResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ModuleResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.PackageResolver;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.TypeParameterResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class UtilJdtResolverImpl implements UtilJdtResolver {

	private static final String SYNTH_CLASS = "SyntheticContainerClass";
	private static final boolean EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS = true;

	private final ParametersFactory parametersFactory;
	private final VariablesFactory variablesFactory;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;

	private final MembersFactory membersFactory;
	private final ContainersFactory containersFactory;
	private final Provider<UtilBindingInfoToConcreteClassifierConverter> utilBindingInfoToConcreteClassifierConverter;
	private final Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter;
	private final Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter;

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
	private final AnonymousClassResolver anyAnonymousClassResolver;
	private final EnumConstantResolver enumConstantResolver;
	private final AdditionalFieldResolver additionalFieldResolver;

	private final HashMap<String, tools.mdsd.jamopp.model.java.members.InterfaceMethod> methBindToInter = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.variables.LocalVariable> nameToLocVar = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable> nameToAddLocVar = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter> nameToVarLenParam = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter> nameToOrdParam = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.parameters.CatchParameter> nameToCatchParam = new HashMap<>();

	@Inject
	UtilJdtResolverImpl(ContainersFactory containersFactory, ClassifiersFactory classifiersFactory,
			TypesFactory typesFactory, StatementsFactory statementsFactory, MembersFactory membersFactory,
			VariablesFactory variablesFactory, ParametersFactory parametersFactory, GenericsFactory genericsFactory,
			Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			Provider<UtilBindingInfoToConcreteClassifierConverter> iUtilBindingInfoToConcreteClassifierConverter) {
		this.parametersFactory = parametersFactory;
		this.variablesFactory = variablesFactory;

		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;

		this.membersFactory = membersFactory;
		this.containersFactory = containersFactory;
		this.bindingToPackageConverter = bindingToPackageConverter;
		this.bindingToModuleConverter = bindingToModuleConverter;
		utilBindingInfoToConcreteClassifierConverter = iUtilBindingInfoToConcreteClassifierConverter;

		moduleResolver = new ModuleResolver(nameCache, new HashMap<>(), moduleBindings, containersFactory);
		packageResolver = new PackageResolver(nameCache, new HashMap<>(), packageBindings, containersFactory);
		annotationResolver = new AnnotationResolver(nameCache, new HashMap<>(), typeBindings, classifiersFactory);
		enumerationResolver = new EnumerationResolver(nameCache, new HashMap<>(), typeBindings, classifiersFactory);
		interfaceResolver = new InterfaceResolver(nameCache, new HashMap<>(), typeBindings, classifiersFactory);
		classResolver = new ClassResolver(nameCache, new HashMap<>(), classifiersFactory);
		typeParameterResolver = new TypeParameterResolver(nameCache, new HashMap<>(), typeBindings, genericsFactory);
		classMethodResolver = new ClassMethodResolver(nameCache, new HashMap<>(), this, typesFactory, statementsFactory,
				methodBindings, membersFactory);
		constructorResolver = new ConstructorResolver(nameCache, new HashMap<>(), statementsFactory, methodBindings,
				membersFactory, this);
		fieldResolver = new FieldResolver(nameCache, new HashMap<>(), variableBindings, this, typesFactory,
				membersFactory);
		anyAnonymousClassResolver = new AnonymousClassResolver(nameCache, new HashMap<>(), classifiersFactory);
		enumConstantResolver = new EnumConstantResolver(nameCache, new HashMap<>(), variableBindings, membersFactory,
				enumerationResolver);
		additionalFieldResolver = new AdditionalFieldResolver(nameCache, new HashMap<String, AdditionalField>(),
				variableBindings, this, membersFactory);

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

	private String convertToTypeName(ITypeBinding binding) {
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
		typeBindings.add(binding);
		return getClass(convertToTypeName(binding));
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
				|| anyAnonymousClassResolver.getBindings().containsKey(convertToTypeName(binding))) {
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

	private String convertToMethodName(IMethodBinding binding) {
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
		if (methBindToInter.containsKey(methodName)) {
			return methBindToInter.get(methodName);
		}
		tools.mdsd.jamopp.model.java.members.InterfaceMethod result = createNewInterfaceMethod();
		methBindToInter.put(methodName, result);
		return result;
	}

	private tools.mdsd.jamopp.model.java.members.InterfaceMethod createNewInterfaceMethod() {
		tools.mdsd.jamopp.model.java.members.InterfaceMethod result = membersFactory.createInterfaceMethod();
		result.setTypeReference(typesFactory.createVoid());
		result.setStatement(statementsFactory.createEmptyStatement());
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(IMethodBinding binding) {
		binding = binding.getMethodDeclaration();
		methodBindings.add(binding);
		String methName = convertToMethodName(binding);
		if (methBindToInter.containsKey(methName)) {
			return methBindToInter.get(methName);
		}
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(
				binding.getDeclaringClass());
		tools.mdsd.jamopp.model.java.members.InterfaceMethod result = null;
		if (classifier != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.InterfaceMethod) {
					result = checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem, binding);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (result == null) {
			result = createNewInterfaceMethod();
		}
		methBindToInter.put(methName, result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private <T extends tools.mdsd.jamopp.model.java.members.Method> T checkMethod(
			tools.mdsd.jamopp.model.java.members.Method mem, IMethodBinding binding) {
		if (mem.getName().equals(binding.getName())) {
			T meth = (T) mem;
			if ("clone".equals(meth.getName())) {
				return meth;
			}
			int receiveOffset = 0;
			if (binding.getDeclaredReceiverType() != null) {
				receiveOffset = 1;
			}
			if (binding.getParameterTypes().length + receiveOffset == meth.getParameters().size()) {
				if (receiveOffset == 1
						&& (!(meth.getParameters()
								.get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
								|| !convertToTypeName(binding.getDeclaredReceiverType())
										.equals(convertToTypeName(meth.getParameters().get(0).getTypeReference())))
						|| !convertToTypeName(binding.getReturnType())
								.equals(convertToTypeName(meth.getTypeReference()))) {
					return null;
				}
				for (int i = 0; i < binding.getParameterTypes().length; i++) {
					ITypeBinding currentParamType = binding.getParameterTypes()[i];
					tools.mdsd.jamopp.model.java.parameters.Parameter currentParam = meth.getParameters()
							.get(i + receiveOffset);
					if (!convertToTypeName(currentParamType).equals(convertToTypeName(currentParam.getTypeReference()))
							|| currentParamType.getDimensions() != currentParam.getArrayDimension()) {
						return null;
					}
				}
				return meth;
			}
		}
		return null;
	}

	private String convertToTypeName(tools.mdsd.jamopp.model.java.types.TypeReference ref) {
		if (ref instanceof tools.mdsd.jamopp.model.java.types.ClassifierReference convRef) {
			if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) {
				return ((tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) convRef.getTarget())
						.getQualifiedName();
			}
			if (convRef.getTarget() instanceof tools.mdsd.jamopp.model.java.types.InferableType) {
				return "var";
			}
			return ((tools.mdsd.jamopp.model.java.generics.TypeParameter) convRef.getTarget()).getName();
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference nRef) {
			if (!nRef.getClassifierReferences().isEmpty()) {
				return convertToTypeName(nRef.getClassifierReferences().get(nRef.getClassifierReferences().size() - 1));
			}
			return nRef.getNamespacesAsString();
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Boolean) {
			return "boolean";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Byte) {
			return "byte";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Char) {
			return "char";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Double) {
			return "double";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Float) {
			return "float";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Int) {
			return "int";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Long) {
			return "long";
		}
		if (ref instanceof tools.mdsd.jamopp.model.java.types.Short) {
			return "short";
		}
		return "void";
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
		return anyAnonymousClassResolver.getByName(typeName);
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

	private String convertToParameterName(IVariableBinding binding, boolean register) {
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
		variableBindings.add(binding);
		return getLocalVariable(convertToParameterName(binding, true));
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(String varName) {
		if (nameToLocVar.containsKey(varName)) {
			return nameToLocVar.get(varName);
		}
		tools.mdsd.jamopp.model.java.variables.LocalVariable result = variablesFactory.createLocalVariable();
		nameToLocVar.put(varName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(
			IVariableBinding binding) {
		variableBindings.add(binding);
		return getAdditionalLocalVariable(convertToParameterName(binding, true));
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(String varName) {
		if (nameToAddLocVar.containsKey(varName)) {
			return nameToAddLocVar.get(varName);
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable result = variablesFactory
				.createAdditionalLocalVariable();
		nameToAddLocVar.put(varName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(IVariableBinding binding) {
		variableBindings.add(binding);
		String paramName = convertToParameterName(binding, true);
		return getOrdinaryParameter(paramName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(String paramName) {
		if (nameToOrdParam.containsKey(paramName)) {
			return nameToOrdParam.get(paramName);
		}
		tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter result = parametersFactory.createOrdinaryParameter();
		nameToOrdParam.put(paramName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter getVariableLengthParameter(
			IVariableBinding binding) {
		String paramName = convertToParameterName(binding, true);
		if (nameToVarLenParam.containsKey(paramName)) {
			return nameToVarLenParam.get(paramName);
		}
		variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter result = parametersFactory
				.createVariableLengthParameter();
		nameToVarLenParam.put(paramName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(IVariableBinding binding) {
		variableBindings.add(binding);
		return getCatchParameter(convertToParameterName(binding, true));
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(String paramName) {
		if (nameToCatchParam.containsKey(paramName)) {
			return nameToCatchParam.get(paramName);
		}
		tools.mdsd.jamopp.model.java.parameters.CatchParameter result = parametersFactory.createCatchParameter();
		nameToCatchParam.put(paramName, result);
		return result;
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
			if (nameToOrdParam.containsKey(paramName)) {
				return nameToOrdParam.get(paramName);
			}
			if (nameToVarLenParam.containsKey(paramName)) {
				return nameToVarLenParam.get(paramName);
			}
			return getOrdinaryParameter(binding);
		}
		String paramName = convertToParameterName(binding, false);
		if (nameToCatchParam.containsKey(paramName)) {
			return nameToCatchParam.get(paramName);
		}
		if (nameToLocVar.containsKey(paramName)) {
			return nameToLocVar.get(paramName);
		}
		if (nameToAddLocVar.containsKey(paramName)) {
			return nameToAddLocVar.get(paramName);
		}
		if (nameToOrdParam.containsKey(paramName)) {
			return nameToOrdParam.get(paramName);
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
		tools.mdsd.jamopp.model.java.variables.Variable par = nameToCatchParam.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		par = nameToLocVar.values().stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable addLocVar = nameToAddLocVar.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addLocVar != null) {
			return addLocVar;
		}
		par = nameToVarLenParam.values().stream().filter(param -> param.getName().equals(name)).findFirst()
				.orElse(null);
		if (par != null) {
			return par;
		}
		par = nameToOrdParam.values().stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
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
		meth = methBindToInter.values().stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
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
						if (anyAnonymousClassResolver.getBindings().containsKey(typeName)) {
							tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anyAnonymousClassResolver
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

		constructorResolver.getBindings().forEach(this::completeMethod);
		classMethodResolver.getBindings().forEach(this::completeMethod);
		methBindToInter.forEach(this::completeMethod);

		convertPureTypeBindings();

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
		methBindToInter.clear();
		additionalFieldResolver.getBindings().clear();
		nameToLocVar.clear();
		nameToAddLocVar.clear();
		enumConstantResolver.getBindings().clear();
		nameToVarLenParam.clear();
		nameToOrdParam.clear();
		nameToCatchParam.clear();
		moduleBindings.clear();
		packageBindings.clear();
		typeBindings.clear();
		methodBindings.clear();
		variableBindings.clear();
		uid = 0;
		varBindToUid.clear();
		objVisited.clear();
		nameCache.clear();
		anyAnonymousClassResolver.getBindings().clear();
	}

	@SuppressWarnings("unused")
	private void completeMethod(String methodName, tools.mdsd.jamopp.model.java.members.Member method) {
		if (method.eContainer() == null) {
			IMethodBinding methBind = methodBindings.stream()
					.filter(meth -> methodName.equals(convertToMethodName(meth))).findFirst().orElse(null);
			if (methBind != null) {
				tools.mdsd.jamopp.model.java.classifiers.Classifier cla = getClassifier(methBind.getDeclaringClass());
				if (cla == null) {
					String typeName = convertToTypeName(methBind.getDeclaringClass());
					if (anyAnonymousClassResolver.getBindings().containsKey(typeName)) {
						tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anyAnonymousClassResolver
								.getBindings().get(typeName);
						if (!anonClass.getMembers().contains(method)) {
							anonClass.getMembers().add(method);
						}
					} else {
						addToSyntheticClass(method);
					}
				} else if (!EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS
						&& cla instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier i
						&& !i.getMembers().contains(method)) {
					i.getMembers().add(method);
				}
			} else {
				addToSyntheticClass(method);
			}
		}
	}

	private void addToSyntheticClass(tools.mdsd.jamopp.model.java.members.Member member) {
		tools.mdsd.jamopp.model.java.classifiers.Class container = getClass(SYNTH_CLASS);
		container.setName(SYNTH_CLASS);
		if (!container.getMembers().contains(member)) {
			container.getMembers().add(member);
		}
	}

	@SuppressWarnings("unchecked")
	private void convertPureTypeBindings() {
		int oldSize;
		int newSize = annotationResolver.getBindings().size() + enumerationResolver.getBindings().size()
				+ interfaceResolver.getBindings().size() + classResolver.getBindings().size()
				+ moduleResolver.getBindings().size() + packageResolver.getBindings().size();
		do {
			oldSize = newSize;
			HashMap<String, ? extends tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier> map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Annotation>) annotationResolver
					.getBindings().clone();
			map.forEach(this::convertPureTypeBinding);
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Enumeration>) enumerationResolver
					.getBindings().clone();
			map.forEach(this::convertPureTypeBinding);
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Interface>) interfaceResolver.getBindings()
					.clone();
			map.forEach(this::convertPureTypeBinding);
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Class>) classResolver.getBindings().clone();
			map.forEach(this::convertPureTypeBinding);
			HashMap<String, tools.mdsd.jamopp.model.java.containers.Package> mapP = (HashMap<String, tools.mdsd.jamopp.model.java.containers.Package>) packageResolver
					.getBindings().clone();
			mapP.forEach(this::convertPurePackageBinding);
			HashMap<String, tools.mdsd.jamopp.model.java.containers.Module> mapM = (HashMap<String, tools.mdsd.jamopp.model.java.containers.Module>) moduleResolver
					.getBindings().clone();
			mapM.forEach(this::convertPureModuleBinding);
			newSize = annotationResolver.getBindings().size() + enumerationResolver.getBindings().size()
					+ interfaceResolver.getBindings().size() + classResolver.getBindings().size()
					+ moduleResolver.getBindings().size() + packageResolver.getBindings().size();
		} while (oldSize < newSize);
	}

	private void convertPureTypeBinding(String typeName,
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier) {
		if (objVisited.contains(classifier)) {
			return;
		}
		objVisited.add(classifier);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass == classifier) {
			return;
		}
		ITypeBinding typeBind = typeBindings.stream()
				.filter(type -> type != null && typeName.equals(convertToTypeName(type))).findFirst().orElse(null);
		if (typeBind == null) {
			classifier.setPackage(getPackage(""));
			if (classifier.eContainer() != null) {
				return;
			}
		} else if (typeBind.isTopLevel()) {
			utilBindingInfoToConcreteClassifierConverter.get().convertToConcreteClassifier(typeBind,
					EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS);
		} else if (typeBind.isNested()) {
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier parentClassifier = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(
					typeBind.getDeclaringClass());
			convertPureTypeBinding(convertToTypeName(typeBind.getDeclaringClass()), parentClassifier);
			classifier.setPackage(getPackage(typeBind.getPackage()));
		} else if (typeBind.isArray()) {
			ITypeBinding elementType = typeBind.getElementType();
			if (!elementType.isPrimitive() && !elementType.isTypeVariable()) {
				convertPureTypeBinding(typeName,
						(tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(elementType));
			}
		}
		if (classifier.eContainer() == null) {
			tools.mdsd.jamopp.model.java.containers.CompilationUnit cu = containersFactory.createCompilationUnit();
			cu.setName("");
			cu.getClassifiers().add(classifier);
			String[] namespaces = typeName.strip().split("\\.");
			classifier.setName(namespaces[namespaces.length - 1]);
			for (int index = 0; index < namespaces.length - 1; index++) {
				cu.getNamespaces().add(namespaces[index]);
			}
			Resource newResource = resourceSet.createResource(URI.createHierarchicalURI("empty",
					"JaMoPP-CompilationUnit", null, new String[] { typeName + ".java" }, null, null));
			newResource.getContents().add(cu);
		}
	}

	private void convertPurePackageBinding(String packageName, tools.mdsd.jamopp.model.java.containers.Package pack) {
		if (objVisited.contains(pack)) {
			return;
		}
		objVisited.add(pack);
		tools.mdsd.jamopp.model.java.containers.Package potPack = JavaClasspath.get().getPackage(packageName);
		if (potPack == pack) {
			return;
		}
		IPackageBinding binding = packageBindings.stream().filter(b -> packageName.equals(b.getName())).findFirst()
				.orElse(null);
		if (binding == null) {
			pack.setName("");
			pack.setModule(getModule(""));
		} else {
			bindingToPackageConverter.get().convert(binding);
		}
		if (pack.eResource() != null) {
			return;
		}
		Resource newResource = resourceSet.createResource(URI.createHierarchicalURI("empty", "JaMoPP-Package", null,
				new String[] { packageName, "package-info.java" }, null, null));
		newResource.getContents().add(pack);
	}

	private void convertPureModuleBinding(String modName, tools.mdsd.jamopp.model.java.containers.Module module) {
		if (objVisited.contains(module)) {
			return;
		}
		objVisited.add(module);
		tools.mdsd.jamopp.model.java.containers.Module potMod = JavaClasspath.get().getModule(modName);
		if (potMod == module || module.eResource() != null) {
			return;
		}
		IModuleBinding binding = moduleBindings.stream().filter(b -> modName.equals(b.getName())).findFirst()
				.orElse(null);
		if (binding == null) {
			module.getNamespaces().clear();
			String[] parts = modName.split("\\.");
			Collections.addAll(module.getNamespaces(), parts);
			module.setName("");
		} else {
			bindingToModuleConverter.get().convert(binding);
		}
		Resource newResource = resourceSet.createResource(URI.createHierarchicalURI("empty", "JaMoPP-Module", null,
				new String[] { modName, "module-info.java" }, null, null));
		newResource.getContents().add(module);
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
