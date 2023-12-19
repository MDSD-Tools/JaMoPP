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
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.model.java.variables.VariablesFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver.ModuleResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class UtilJdtResolverImpl implements UtilJdtResolver {

	private static final String SYNTH_CLASS = "SyntheticContainerClass";
	private static final boolean EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS = true;

	private final ParametersFactory parametersFactory;
	private final VariablesFactory variablesFactory;
	private final GenericsFactory genericsFactory;
	private final StatementsFactory statementsFactory;
	private final TypesFactory typesFactory;
	private final MembersFactory membersFactory;
	private final ClassifiersFactory classifiersFactory;
	private final ContainersFactory containersFactory;
	private final Provider<UtilBindingInfoToConcreteClassifierConverter> iUtilBindingInfoToConcreteClassifierConverter;
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
	private final HashMap<String, tools.mdsd.jamopp.model.java.containers.Package> nameToPackage = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Annotation> typeBindToAnnot = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Enumeration> typeBindToEnum = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Interface> typeBindToInterface = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Class> typeBindToClass = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.generics.TypeParameter> typeBindToTP = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.members.InterfaceMethod> methBindToInter = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.members.ClassMethod> methBindToCM = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.members.Constructor> methBindToConstr = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.members.Field> nameToField = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.members.AdditionalField> nameToAddField = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.variables.LocalVariable> nameToLocVar = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable> nameToAddLocVar = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.members.EnumConstant> nameToEnumConst = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter> nameToVarLenParam = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter> nameToOrdParam = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.parameters.CatchParameter> nameToCatchParam = new HashMap<>();
	private final HashMap<String, tools.mdsd.jamopp.model.java.classifiers.AnonymousClass> nameToAnonymousClass = new HashMap<>();

	@Inject
	UtilJdtResolverImpl(ContainersFactory containersFactory, ClassifiersFactory classifiersFactory,
			TypesFactory typesFactory, StatementsFactory statementsFactory, MembersFactory membersFactory,
			VariablesFactory variablesFactory, ParametersFactory parametersFactory, GenericsFactory genericsFactory,
			Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			Provider<UtilBindingInfoToConcreteClassifierConverter> iUtilBindingInfoToConcreteClassifierConverter) {
		this.parametersFactory = parametersFactory;
		this.variablesFactory = variablesFactory;
		this.genericsFactory = genericsFactory;
		this.statementsFactory = statementsFactory;
		this.typesFactory = typesFactory;
		this.membersFactory = membersFactory;
		this.classifiersFactory = classifiersFactory;
		this.containersFactory = containersFactory;
		this.bindingToPackageConverter = bindingToPackageConverter;
		this.bindingToModuleConverter = bindingToModuleConverter;
		this.iUtilBindingInfoToConcreteClassifierConverter = iUtilBindingInfoToConcreteClassifierConverter;

		this.moduleResolver = new ModuleResolver(this.moduleBindings, containersFactory);
	}

	@Override
	public void setResourceSet(ResourceSet set) {
		this.resourceSet = set;
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module getModule(IModuleBinding binding) {
		return this.moduleResolver.getByBinding(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module getModule(String modName) {
		return this.moduleResolver.getByName(modName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Package getPackage(IPackageBinding binding) {
		this.packageBindings.add(binding);
		return getPackage(binding.getName());
	}

	private tools.mdsd.jamopp.model.java.containers.Package getPackage(String packageName) {
		if (this.nameToPackage.containsKey(packageName)) {
			return this.nameToPackage.get(packageName);
		}
		tools.mdsd.jamopp.model.java.containers.Package result = JavaClasspath.get().getPackage(packageName);
		if (result == null) {
			result = this.containersFactory.createPackage();
		}
		this.nameToPackage.put(packageName, result);
		return result;
	}

	private String convertToTypeName(ITypeBinding binding) {
		if (binding == null) {
			return "";
		}
		if (binding.isTypeVariable()) {
			return binding.getName();
		}
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
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
			this.nameCache.put(binding, qualifiedName);
			return qualifiedName;
		} else {
			qualifiedName = binding.getQualifiedName();
		}
		if (qualifiedName.contains("<")) {
			qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf("<"));
		}
		this.nameCache.put(binding, qualifiedName);
		return qualifiedName;
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(ITypeBinding binding) {
		this.typeBindings.add(binding);
		return getAnnotation(convertToTypeName(binding));
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(String annotName) {
		if (this.typeBindToAnnot.containsKey(annotName)) {
			return this.typeBindToAnnot.get(annotName);
		}
		tools.mdsd.jamopp.model.java.classifiers.Annotation result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(annotName);
		if (potClass instanceof tools.mdsd.jamopp.model.java.classifiers.Annotation) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Annotation) potClass;
		} else {
			result = this.classifiersFactory.createAnnotation();
		}
		this.typeBindToAnnot.put(annotName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Enumeration getEnumeration(ITypeBinding binding) {
		String enumName = convertToTypeName(binding);
		if (this.typeBindToEnum.containsKey(enumName)) {
			return this.typeBindToEnum.get(enumName);
		}
		this.typeBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
				.getConcreteClassifier(enumName);
		tools.mdsd.jamopp.model.java.classifiers.Enumeration result;
		if (classifier instanceof tools.mdsd.jamopp.model.java.classifiers.Enumeration) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Enumeration) classifier;
		} else {
			result = this.classifiersFactory.createEnumeration();
		}
		this.typeBindToEnum.put(enumName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(ITypeBinding binding) {
		this.typeBindings.add(binding);
		return getClass(convertToTypeName(binding));
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.Interface getInterface(ITypeBinding binding) {
		String interName = convertToTypeName(binding);
		if (this.typeBindToInterface.containsKey(interName)) {
			return this.typeBindToInterface.get(interName);
		}
		this.typeBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.Interface result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = JavaClasspath.get()
				.getConcreteClassifier(interName);
		if (classifier instanceof tools.mdsd.jamopp.model.java.classifiers.Interface) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Interface) classifier;
		} else {
			result = this.classifiersFactory.createInterface();
		}
		this.typeBindToInterface.put(interName, result);
		return result;
	}

	private String convertToTypeParameterName(ITypeBinding binding) {
		if (binding == null) {
			return "";
		}
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
		}
		String name = "";
		if (binding.getDeclaringClass() != null) {
			name += convertToTypeName(binding.getDeclaringClass());
		} else if (binding.getDeclaringMethod() != null) {
			name += convertToMethodName(binding.getDeclaringMethod());
		}
		name += "." + binding.getName();
		this.nameCache.put(binding, name);
		return name;
	}

	@Override
	public tools.mdsd.jamopp.model.java.generics.TypeParameter getTypeParameter(ITypeBinding binding) {
		String paramName = convertToTypeParameterName(binding);
		if (this.typeBindToTP.containsKey(paramName)) {
			return this.typeBindToTP.get(paramName);
		}
		this.typeBindings.add(binding);
		tools.mdsd.jamopp.model.java.generics.TypeParameter result = this.genericsFactory.createTypeParameter();
		this.typeBindToTP.put(paramName, result);
		return result;
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
				|| this.nameToAnonymousClass.containsKey(convertToTypeName(binding))) {
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
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
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
		this.nameCache.put(binding, name);
		return name;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(String methodName) {
		if (this.methBindToInter.containsKey(methodName)) {
			return this.methBindToInter.get(methodName);
		}
		tools.mdsd.jamopp.model.java.members.InterfaceMethod result = createNewInterfaceMethod();
		this.methBindToInter.put(methodName, result);
		return result;
	}

	private tools.mdsd.jamopp.model.java.members.InterfaceMethod createNewInterfaceMethod() {
		tools.mdsd.jamopp.model.java.members.InterfaceMethod result = this.membersFactory.createInterfaceMethod();
		result.setTypeReference(this.typesFactory.createVoid());
		result.setStatement(this.statementsFactory.createEmptyStatement());
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(IMethodBinding binding) {
		binding = binding.getMethodDeclaration();
		this.methodBindings.add(binding);
		String methName = convertToMethodName(binding);
		if (this.methBindToInter.containsKey(methName)) {
			return this.methBindToInter.get(methName);
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
		this.methBindToInter.put(methName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(String methodName) {
		if (this.methBindToCM.containsKey(methodName)) {
			return this.methBindToCM.get(methodName);
		}
		tools.mdsd.jamopp.model.java.members.ClassMethod result = createNewClassMethod();
		this.methBindToCM.put(methodName, result);
		return result;
	}

	private tools.mdsd.jamopp.model.java.members.ClassMethod createNewClassMethod() {
		tools.mdsd.jamopp.model.java.members.ClassMethod result = this.membersFactory.createClassMethod();
		result.setTypeReference(this.typesFactory.createVoid());
		tools.mdsd.jamopp.model.java.statements.Block block = this.statementsFactory.createBlock();
		block.setName("");
		result.setStatement(block);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(IMethodBinding binding) {
		binding = binding.getMethodDeclaration();
		this.methodBindings.add(binding);
		String methName = convertToMethodName(binding);
		if (this.methBindToCM.containsKey(methName)) {
			return this.methBindToCM.get(methName);
		}
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(
				binding.getDeclaringClass());
		tools.mdsd.jamopp.model.java.members.ClassMethod result = null;
		if (classifier != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : classifier.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.ClassMethod) {
					result = checkMethod((tools.mdsd.jamopp.model.java.members.Method) mem, binding);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (result == null) {
			result = createNewClassMethod();
		}
		this.methBindToCM.put(methName, result);
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
	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(IMethodBinding binding) {
		String methName = convertToMethodName(binding);
		if (this.methBindToConstr.containsKey(methName)) {
			return this.methBindToConstr.get(methName);
		}
		this.methodBindings.add(binding);
		tools.mdsd.jamopp.model.java.members.Constructor result = null;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(
				binding.getDeclaringClass());
		if (potClass != null) {
			outerLoop: for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Constructor con
						&& mem.getName().equals(binding.getName())) {
					int receiveOffset = 0;
					if (binding.getDeclaredReceiverType() != null) {
						receiveOffset = 1;
					}
					if (con.getParameters().size() == binding.getParameterTypes().length + receiveOffset) {
						if (receiveOffset == 1 && (!(con.getParameters()
								.get(0) instanceof tools.mdsd.jamopp.model.java.parameters.ReceiverParameter)
								|| !convertToTypeName(binding.getDeclaredReceiverType())
										.equals(convertToTypeName(con.getParameters().get(0).getTypeReference())))) {
							continue outerLoop;
						}
						for (int i = 0; i < binding.getParameterTypes().length; i++) {
							ITypeBinding currentType = binding.getParameterTypes()[i];
							tools.mdsd.jamopp.model.java.parameters.Parameter currentParam = con.getParameters()
									.get(i + receiveOffset);
							if (!convertToTypeName(currentType)
									.equals(convertToTypeName(currentParam.getTypeReference()))
									|| currentType.getDimensions() != currentParam.getArrayDimension()) {
								continue outerLoop;
							}
						}
						result = con;
						break;
					}
				}
			}
		}
		if (result == null) {
			result = this.membersFactory.createConstructor();
			tools.mdsd.jamopp.model.java.statements.Block block = this.statementsFactory.createBlock();
			block.setName("");
			result.setBlock(block);
		}
		this.methBindToConstr.put(methName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(String methName) {
		if (this.methBindToConstr.containsKey(methName)) {
			return this.methBindToConstr.get(methName);
		}
		tools.mdsd.jamopp.model.java.members.Constructor result = this.membersFactory.createConstructor();
		tools.mdsd.jamopp.model.java.statements.Block block = this.statementsFactory.createBlock();
		block.setName("");
		result.setBlock(block);
		this.methBindToConstr.put(methName, result);
		return result;
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
		if (this.typeBindToClass.containsKey(typeName)) {
			return this.typeBindToClass.get(typeName);
		}
		tools.mdsd.jamopp.model.java.classifiers.Class result;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
			result = (tools.mdsd.jamopp.model.java.classifiers.Class) potClass;
		} else {
			result = this.classifiersFactory.createClass();
		}
		this.typeBindToClass.put(typeName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(String typeName) {
		if (this.nameToAnonymousClass.containsKey(typeName)) {
			return this.nameToAnonymousClass.get(typeName);
		}
		tools.mdsd.jamopp.model.java.classifiers.AnonymousClass result = this.classifiersFactory.createAnonymousClass();
		this.nameToAnonymousClass.put(typeName, result);
		return result;
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
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
		}
		String name = convertToTypeName(binding.getDeclaringClass()) + "::" + binding.getName();
		this.nameCache.put(binding, name);
		return name;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Field getField(String name) {
		if (this.nameToField.containsKey(name)) {
			return this.nameToField.get(name);
		}
		tools.mdsd.jamopp.model.java.members.Field result = this.membersFactory.createField();
		this.nameToField.put(name, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.Field getField(IVariableBinding binding) {
		String varName = convertToFieldName(binding);
		if (this.nameToField.containsKey(varName)) {
			return this.nameToField.get(varName);
		}
		this.variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = null;
		if (binding.getDeclaringClass() != null) {
			potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(
					binding.getDeclaringClass());
		}
		tools.mdsd.jamopp.model.java.members.Field result = null;
		if (potClass != null) {
			for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Field
						&& mem.getName().equals(binding.getName())) {
					result = (tools.mdsd.jamopp.model.java.members.Field) mem;
					break;
				}
			}
		}
		if (result == null) {
			result = this.membersFactory.createField();
			result.setTypeReference(this.typesFactory.createInt());
		}
		this.nameToField.put(varName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(IVariableBinding binding) {
		String enumCN = convertToFieldName(binding);
		if (this.nameToEnumConst.containsKey(enumCN)) {
			return this.nameToEnumConst.get(enumCN);
		}
		this.variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.classifiers.Enumeration potPar = getEnumeration(binding.getDeclaringClass());
		tools.mdsd.jamopp.model.java.members.EnumConstant result = null;
		if (potPar != null) {
			for (tools.mdsd.jamopp.model.java.members.EnumConstant con : potPar.getConstants()) {
				if (con.getName().equals(binding.getName())) {
					result = con;
					break;
				}
			}
		}
		if (result == null) {
			result = this.membersFactory.createEnumConstant();
		}
		this.nameToEnumConst.put(enumCN, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(String enumCN) {
		if (this.nameToEnumConst.containsKey(enumCN)) {
			return this.nameToEnumConst.get(enumCN);
		}
		tools.mdsd.jamopp.model.java.members.EnumConstant result = this.membersFactory.createEnumConstant();
		this.nameToEnumConst.put(enumCN, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(String name) {
		if (this.nameToAddField.containsKey(name)) {
			return this.nameToAddField.get(name);
		}
		tools.mdsd.jamopp.model.java.members.AdditionalField result = this.membersFactory.createAdditionalField();
		this.nameToAddField.put(name, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(IVariableBinding binding) {
		String varName = convertToFieldName(binding);
		if (this.nameToAddField.containsKey(varName)) {
			return this.nameToAddField.get(varName);
		}
		this.variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.members.AdditionalField result = null;
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) getClassifier(
				binding.getDeclaringClass());
		if (potClass != null) {
			outerLoop: for (tools.mdsd.jamopp.model.java.members.Member mem : potClass.getMembers()) {
				if (mem instanceof tools.mdsd.jamopp.model.java.members.Field field) {
					for (tools.mdsd.jamopp.model.java.members.AdditionalField af : field.getAdditionalFields()) {
						if (af.getName().equals(binding.getName())) {
							result = af;
							break outerLoop;
						}
					}
				}
			}
		}
		if (result == null) {
			result = this.membersFactory.createAdditionalField();
		}
		this.nameToAddField.put(varName, result);
		return result;
	}

	private String convertToParameterName(IVariableBinding binding, boolean register) {
		if (binding == null) {
			return "";
		}
		if (this.nameCache.containsKey(binding)) {
			return this.nameCache.get(binding);
		}
		String prefix = "";
		if (binding.getDeclaringMethod() != null) {
			prefix = convertToMethodName(binding.getDeclaringMethod());
		} else if (this.varBindToUid.containsKey(binding)) {
			prefix = this.varBindToUid.get(binding) + "";
		} else {
			prefix = this.uid + "";
			if (register) {
				this.varBindToUid.put(binding, this.uid);
			}
		}
		String name = prefix + "::" + binding.getName() + "::" + binding.getVariableId() + binding.hashCode();
		this.nameCache.put(binding, name);
		return name;
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(IVariableBinding binding) {
		this.variableBindings.add(binding);
		return getLocalVariable(convertToParameterName(binding, true));
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(String varName) {
		if (this.nameToLocVar.containsKey(varName)) {
			return this.nameToLocVar.get(varName);
		}
		tools.mdsd.jamopp.model.java.variables.LocalVariable result = this.variablesFactory.createLocalVariable();
		this.nameToLocVar.put(varName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(
			IVariableBinding binding) {
		this.variableBindings.add(binding);
		return getAdditionalLocalVariable(convertToParameterName(binding, true));
	}

	@Override
	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(String varName) {
		if (this.nameToAddLocVar.containsKey(varName)) {
			return this.nameToAddLocVar.get(varName);
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable result = this.variablesFactory
				.createAdditionalLocalVariable();
		this.nameToAddLocVar.put(varName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(IVariableBinding binding) {
		this.variableBindings.add(binding);
		String paramName = convertToParameterName(binding, true);
		return getOrdinaryParameter(paramName);
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(String paramName) {
		if (this.nameToOrdParam.containsKey(paramName)) {
			return this.nameToOrdParam.get(paramName);
		}
		tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter result = this.parametersFactory
				.createOrdinaryParameter();
		this.nameToOrdParam.put(paramName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter getVariableLengthParameter(
			IVariableBinding binding) {
		String paramName = convertToParameterName(binding, true);
		if (this.nameToVarLenParam.containsKey(paramName)) {
			return this.nameToVarLenParam.get(paramName);
		}
		this.variableBindings.add(binding);
		tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter result = this.parametersFactory
				.createVariableLengthParameter();
		this.nameToVarLenParam.put(paramName, result);
		return result;
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(IVariableBinding binding) {
		this.variableBindings.add(binding);
		return getCatchParameter(convertToParameterName(binding, true));
	}

	@Override
	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(String paramName) {
		if (this.nameToCatchParam.containsKey(paramName)) {
			return this.nameToCatchParam.get(paramName);
		}
		tools.mdsd.jamopp.model.java.parameters.CatchParameter result = this.parametersFactory.createCatchParameter();
		this.nameToCatchParam.put(paramName, result);
		return result;
	}

	@Override
	public void prepareNextUid() {
		this.uid++;
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferencableElement(
			IVariableBinding binding) {
		if (binding.isEnumConstant()) {
			return getEnumConstant(binding);
		}
		if (binding.isField()) {
			String fieldName = convertToFieldName(binding);
			if (this.nameToField.containsKey(fieldName)) {
				return this.nameToField.get(fieldName);
			}
			if (this.nameToAddField.containsKey(fieldName)) {
				return this.nameToAddField.get(fieldName);
			}
			return getField(binding);
		}
		if (binding.isParameter()) {
			String paramName = convertToParameterName(binding, false);
			if (this.nameToOrdParam.containsKey(paramName)) {
				return this.nameToOrdParam.get(paramName);
			}
			if (this.nameToVarLenParam.containsKey(paramName)) {
				return this.nameToVarLenParam.get(paramName);
			}
			return getOrdinaryParameter(binding);
		}
		String paramName = convertToParameterName(binding, false);
		if (this.nameToCatchParam.containsKey(paramName)) {
			return this.nameToCatchParam.get(paramName);
		}
		if (this.nameToLocVar.containsKey(paramName)) {
			return this.nameToLocVar.get(paramName);
		}
		if (this.nameToAddLocVar.containsKey(paramName)) {
			return this.nameToAddLocVar.get(paramName);
		}
		if (this.nameToOrdParam.containsKey(paramName)) {
			return this.nameToOrdParam.get(paramName);
		}
		return getLocalVariable(binding);
	}

	@Override
	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferenceableElementByNameMatching(
			String name) {
		IVariableBinding vBinding = this.variableBindings.stream()
				.filter(var -> var != null && var.getName().equals(name)).findFirst().orElse(null);
		if (vBinding != null) {
			return getReferencableElement(vBinding);
		}
		IMethodBinding mBinding = this.methodBindings.stream()
				.filter(meth -> !meth.isConstructor() && meth.getName().equals(name)).findFirst().orElse(null);
		if (mBinding != null) {
			return getMethod(mBinding);
		}
		ITypeBinding tBinding = this.typeBindings.stream().filter(type -> type != null && type.getName().equals(name))
				.findFirst().orElse(null);
		if (tBinding != null) {
			return getClassifier(tBinding);
		}
		tools.mdsd.jamopp.model.java.variables.Variable par = this.nameToCatchParam.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		par = this.nameToLocVar.values().stream().filter(param -> param.getName().equals(name)).findFirst()
				.orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable addLocVar = this.nameToAddLocVar.values()
				.stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addLocVar != null) {
			return addLocVar;
		}
		par = this.nameToVarLenParam.values().stream().filter(param -> param.getName().equals(name)).findFirst()
				.orElse(null);
		if (par != null) {
			return par;
		}
		par = this.nameToOrdParam.values().stream().filter(param -> param.getName().equals(name)).findFirst()
				.orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.members.EnumConstant enumConst = this.nameToEnumConst.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (enumConst != null) {
			return enumConst;
		}
		tools.mdsd.jamopp.model.java.members.Field field = this.nameToField.values().stream()
				.filter(param -> param != null && param.getName().equals(name)).findFirst().orElse(null);
		if (field != null) {
			return field;
		}
		tools.mdsd.jamopp.model.java.members.AdditionalField addField = this.nameToAddField.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addField != null) {
			return addField;
		}
		tools.mdsd.jamopp.model.java.members.Method meth = this.methBindToCM.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (meth != null) {
			return meth;
		}
		meth = this.methBindToInter.values().stream().filter(param -> param.getName().equals(name)).findFirst()
				.orElse(null);
		if (meth != null) {
			return meth;
		}
		tools.mdsd.jamopp.model.java.classifiers.Classifier c = this.typeBindToTP.values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (c != null) {
			return c;
		}
		c = this.typeBindToEnum.values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = this.typeBindToAnnot.values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = this.typeBindToClass.values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = this.typeBindToInterface.values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		return getClass(name);
	}

	@Override
	@SuppressWarnings("unused")
	public void completeResolution() {
		this.nameToEnumConst.forEach((constName, enConst) -> {
			if (enConst.eContainer() == null) {
				IVariableBinding varBind = this.variableBindings.stream()
						.filter(var -> var != null && constName.equals(convertToFieldName(var))).findFirst().get();
				if (!varBind.getDeclaringClass().isAnonymous()) {
					var en = getEnumeration(varBind.getDeclaringClass());
					if (!EXTRACT_ADDITIONAL_INFORMATION_FROM_TYPE_BINDINGS && !en.getConstants().contains(enConst)) {
						en.getConstants().add(enConst);
					}
				}
			}
		});

		this.nameToField.forEach((fieldName, field) -> {
			if (field.eContainer() == null) {
				IVariableBinding varBind = this.variableBindings.stream()
						.filter(var -> var != null && fieldName.equals(convertToFieldName(var))).findFirst()
						.orElse(null);
				if (varBind == null || varBind.getDeclaringClass() == null) {
					addToSyntheticClass(field);
				} else {
					tools.mdsd.jamopp.model.java.classifiers.Classifier cla = getClassifier(
							varBind.getDeclaringClass());
					if (cla == null) {
						String typeName = convertToTypeName(varBind.getDeclaringClass());
						if (this.nameToAnonymousClass.containsKey(typeName)) {
							tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = this.nameToAnonymousClass
									.get(typeName);
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

		this.methBindToConstr.forEach(this::completeMethod);
		this.methBindToInter.forEach(this::completeMethod);
		this.methBindToCM.forEach(this::completeMethod);

		convertPureTypeBindings();

		this.moduleResolver.getBindings().values().forEach(module -> JavaClasspath.get().registerModule(module));
		this.nameToPackage.values().forEach(pack -> JavaClasspath.get().registerPackage(pack));
		this.typeBindToAnnot.values().forEach(ann -> JavaClasspath.get().registerConcreteClassifier(ann));
		this.typeBindToEnum.values().forEach(enume -> JavaClasspath.get().registerConcreteClassifier(enume));
		this.typeBindToInterface.values().forEach(interf -> JavaClasspath.get().registerConcreteClassifier(interf));
		this.typeBindToClass.values().forEach(clazz -> JavaClasspath.get().registerConcreteClassifier(clazz));

		escapeAllIdentifiers();

		this.moduleResolver.clearBindings();
		this.nameToPackage.clear();
		this.typeBindToAnnot.clear();
		this.typeBindToEnum.clear();
		this.typeBindToInterface.clear();
		this.typeBindToClass.clear();
		this.typeBindToTP.clear();
		this.methBindToInter.clear();
		this.methBindToCM.clear();
		this.methBindToConstr.clear();
		this.nameToField.clear();
		this.nameToAddField.clear();
		this.nameToLocVar.clear();
		this.nameToAddLocVar.clear();
		this.nameToEnumConst.clear();
		this.nameToVarLenParam.clear();
		this.nameToOrdParam.clear();
		this.nameToCatchParam.clear();
		this.moduleBindings.clear();
		this.packageBindings.clear();
		this.typeBindings.clear();
		this.methodBindings.clear();
		this.variableBindings.clear();
		this.uid = 0;
		this.varBindToUid.clear();
		this.objVisited.clear();
		this.nameCache.clear();
		this.nameToAnonymousClass.clear();
	}

	@SuppressWarnings("unused")
	private void completeMethod(String methodName, tools.mdsd.jamopp.model.java.members.Member method) {
		if (method.eContainer() == null) {
			IMethodBinding methBind = this.methodBindings.stream()
					.filter(meth -> methodName.equals(convertToMethodName(meth))).findFirst().orElse(null);
			if (methBind != null) {
				tools.mdsd.jamopp.model.java.classifiers.Classifier cla = getClassifier(methBind.getDeclaringClass());
				if (cla == null) {
					String typeName = convertToTypeName(methBind.getDeclaringClass());
					if (this.nameToAnonymousClass.containsKey(typeName)) {
						tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = this.nameToAnonymousClass
								.get(typeName);
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
		int newSize = this.typeBindToAnnot.size() + this.typeBindToEnum.size() + this.typeBindToInterface.size()
				+ this.typeBindToClass.size() + this.moduleResolver.getBindings().size() + this.nameToPackage.size();
		do {
			oldSize = newSize;
			HashMap<String, ? extends tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier> map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Annotation>) this.typeBindToAnnot
					.clone();
			map.forEach(this::convertPureTypeBinding);
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Enumeration>) this.typeBindToEnum.clone();
			map.forEach(this::convertPureTypeBinding);
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Interface>) this.typeBindToInterface
					.clone();
			map.forEach(this::convertPureTypeBinding);
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Class>) this.typeBindToClass.clone();
			map.forEach(this::convertPureTypeBinding);
			HashMap<String, tools.mdsd.jamopp.model.java.containers.Package> mapP = (HashMap<String, tools.mdsd.jamopp.model.java.containers.Package>) this.nameToPackage
					.clone();
			mapP.forEach(this::convertPurePackageBinding);
			HashMap<String, tools.mdsd.jamopp.model.java.containers.Module> mapM = (HashMap<String, tools.mdsd.jamopp.model.java.containers.Module>) this.moduleResolver
					.getBindings().clone();
			mapM.forEach(this::convertPureModuleBinding);
			newSize = this.typeBindToAnnot.size() + this.typeBindToEnum.size() + this.typeBindToInterface.size()
					+ this.typeBindToClass.size() + this.moduleResolver.getBindings().size()
					+ this.nameToPackage.size();
		} while (oldSize < newSize);
	}

	private void convertPureTypeBinding(String typeName,
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier) {
		if (this.objVisited.contains(classifier)) {
			return;
		}
		this.objVisited.add(classifier);
		tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier potClass = JavaClasspath.get()
				.getConcreteClassifier(typeName);
		if (potClass == classifier) {
			return;
		}
		ITypeBinding typeBind = this.typeBindings.stream()
				.filter(type -> type != null && typeName.equals(convertToTypeName(type))).findFirst().orElse(null);
		if (typeBind == null) {
			classifier.setPackage(getPackage(""));
			if (classifier.eContainer() != null) {
				return;
			}
		} else if (typeBind.isTopLevel()) {
			this.iUtilBindingInfoToConcreteClassifierConverter.get().convertToConcreteClassifier(typeBind,
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
			tools.mdsd.jamopp.model.java.containers.CompilationUnit cu = this.containersFactory.createCompilationUnit();
			cu.setName("");
			cu.getClassifiers().add(classifier);
			String[] namespaces = typeName.strip().split("\\.");
			classifier.setName(namespaces[namespaces.length - 1]);
			for (int index = 0; index < namespaces.length - 1; index++) {
				cu.getNamespaces().add(namespaces[index]);
			}
			Resource newResource = this.resourceSet.createResource(URI.createHierarchicalURI("empty",
					"JaMoPP-CompilationUnit", null, new String[] { typeName + ".java" }, null, null));
			newResource.getContents().add(cu);
		}
	}

	private void convertPurePackageBinding(String packageName, tools.mdsd.jamopp.model.java.containers.Package pack) {
		if (this.objVisited.contains(pack)) {
			return;
		}
		this.objVisited.add(pack);
		tools.mdsd.jamopp.model.java.containers.Package potPack = JavaClasspath.get().getPackage(packageName);
		if (potPack == pack) {
			return;
		}
		IPackageBinding binding = this.packageBindings.stream().filter(b -> packageName.equals(b.getName())).findFirst()
				.orElse(null);
		if (binding == null) {
			pack.setName("");
			pack.setModule(getModule(""));
		} else {
			this.bindingToPackageConverter.get().convert(binding);
		}
		if (pack.eResource() != null) {
			return;
		}
		Resource newResource = this.resourceSet.createResource(URI.createHierarchicalURI("empty", "JaMoPP-Package",
				null, new String[] { packageName, "package-info.java" }, null, null));
		newResource.getContents().add(pack);
	}

	private void convertPureModuleBinding(String modName, tools.mdsd.jamopp.model.java.containers.Module module) {
		if (this.objVisited.contains(module)) {
			return;
		}
		this.objVisited.add(module);
		tools.mdsd.jamopp.model.java.containers.Module potMod = JavaClasspath.get().getModule(modName);
		if (potMod == module || module.eResource() != null) {
			return;
		}
		IModuleBinding binding = this.moduleBindings.stream().filter(b -> modName.equals(b.getName())).findFirst()
				.orElse(null);
		if (binding == null) {
			module.getNamespaces().clear();
			String[] parts = modName.split("\\.");
			Collections.addAll(module.getNamespaces(), parts);
			module.setName("");
		} else {
			this.bindingToModuleConverter.get().convert(binding);
		}
		Resource newResource = this.resourceSet.createResource(URI.createHierarchicalURI("empty", "JaMoPP-Module", null,
				new String[] { modName, "module-info.java" }, null, null));
		newResource.getContents().add(module);
	}

	private void escapeAllIdentifiers() {
		this.moduleResolver.getBindings().values().forEach(this::escapeIdentifiers);
		this.nameToPackage.values().forEach(this::escapeIdentifiers);
		this.typeBindToAnnot.values().forEach(this::escapeIdentifiers);
		this.typeBindToEnum.values().forEach(this::escapeIdentifiers);
		this.typeBindToClass.values().forEach(this::escapeIdentifiers);
		this.typeBindToInterface.values().forEach(this::escapeIdentifiers);
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
