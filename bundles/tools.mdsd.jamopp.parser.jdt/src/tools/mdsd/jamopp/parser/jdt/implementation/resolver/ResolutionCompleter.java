package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

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
import com.google.inject.name.Named;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Field;

public class ResolutionCompleter {

	private final boolean extractAdditionalInfosFromTypeBindings;
	private final HashMap<IVariableBinding, Integer> varBindToUid;
	private final HashMap<IBinding, String> nameCache;
	private final HashSet<IModuleBinding> moduleBindings;
	private final HashSet<IPackageBinding> packageBindings;
	private final HashSet<ITypeBinding> typeBindings;
	private final HashSet<IMethodBinding> methodBindings;
	private final HashSet<IVariableBinding> variableBindings;
	private final HashSet<EObject> objVisited;
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
	private final ClassResolverSynthetic classResolverSynthetic;
	private final ToFieldNameConverter toFieldNameConverter;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ClassifierResolver classifierResolver;

	@Inject
	public ResolutionCompleter(VariableLengthParameterResolver variableLengthParameterResolver,
			HashSet<IVariableBinding> variableBindings, HashMap<IVariableBinding, Integer> varBindToUid,
			TypeParameterResolver typeParameterResolver, HashSet<ITypeBinding> typeBindings,
			PureTypeBindingsConverter pureTypeBindingsConverter, PackageResolver packageResolver,
			HashSet<IPackageBinding> packageBindings, OrdinaryParameterResolver ordinaryParameterResolver,
			HashSet<EObject> objVisited, ModuleResolver moduleResolver, HashSet<IModuleBinding> moduleBindings,
			MethodCompleter methodCompleter, HashSet<IMethodBinding> methodBindings,
			LocalVariableResolver localVariableResolver, InterfaceResolver interfaceResolver,
			InterfaceMethodResolver interfaceMethodResolver, FieldResolver fieldResolver,
			@Named("extractAdditionalInfosFromTypeBindings") boolean extractAdditionalInfosFromTypeBindings,
			EnumerationResolver enumerationResolver, EnumConstantResolver enumConstantResolver, String cynthClass,
			ConstructorResolver constructorResolver, ClassResolver classResolver,
			ClassMethodResolver classMethodResolver, CatchParameterResolver catchParameterResolver,
			AnonymousClassResolver anonymousClassResolver, AnnotationResolver annotationResolver,
			AdditionalLocalVariableResolver additionalLocalVariableResolver,
			AdditionalFieldResolver additionalFieldResolver, ClassResolverSynthetic classResolverSynthetic,
			ToTypeNameConverter toTypeNameConverter, ToFieldNameConverter toFieldNameConverter,
			ClassifierResolver classifierResolver, HashMap<IBinding, String> nameCache) {
		this.extractAdditionalInfosFromTypeBindings = extractAdditionalInfosFromTypeBindings;
		this.varBindToUid = varBindToUid;
		this.nameCache = nameCache;
		this.moduleBindings = moduleBindings;
		this.packageBindings = packageBindings;
		this.typeBindings = typeBindings;
		this.methodBindings = methodBindings;
		this.variableBindings = variableBindings;
		this.objVisited = objVisited;
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
		this.methodCompleter = methodCompleter;
		this.pureTypeBindingsConverter = pureTypeBindingsConverter;
		this.classResolverSynthetic = classResolverSynthetic;
		this.toFieldNameConverter = toFieldNameConverter;
		this.toTypeNameConverter = toTypeNameConverter;
		this.classifierResolver = classifierResolver;
	}

	public void completeResolution(ResourceSet resourceSet) {
		enumConstantResolver.getBindings().forEach(this::handleEnumConstants);
		fieldResolver.getBindings().forEach(this::handleFields);
		constructorResolver.getBindings().forEach((t, u) -> methodCompleter.completeMethod(t, u));
		classMethodResolver.getBindings().forEach((t, u) -> methodCompleter.completeMethod(t, u));
		interfaceMethodResolver.getBindings().forEach((t, u) -> methodCompleter.completeMethod(t, u));

		pureTypeBindingsConverter.convertPureTypeBindings(resourceSet);

		register();
		escapeAllIdentifiers();
		clear();
	}

	private void handleFields(String fieldName, Field field) {
		if (field.eContainer() == null) {
			IVariableBinding varBind = variableBindings.stream().filter(
					binding -> binding != null && fieldName.equals(toFieldNameConverter.convertToFieldName(binding)))
					.findFirst().orElse(null);
			if (varBind == null || varBind.getDeclaringClass() == null) {
				classResolverSynthetic.addToSyntheticClass(field);
			} else {
				tools.mdsd.jamopp.model.java.classifiers.Classifier cla = classifierResolver
						.getClassifier(varBind.getDeclaringClass());
				if (cla == null) {
					String typeName = toTypeNameConverter.convertToTypeName(varBind.getDeclaringClass());
					if (anonymousClassResolver.getBindings().containsKey(typeName)) {
						tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
								.getBindings().get(typeName);
						if (!anonClass.getMembers().contains(field)) {
							anonClass.getMembers().add(field);
						}
					} else {
						classResolverSynthetic.addToSyntheticClass(field);
					}
				} else if (!extractAdditionalInfosFromTypeBindings
						&& cla instanceof tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier i
						&& !i.getMembers().contains(field)) {
					i.getMembers().add(field);
				}
			}
		}
	}

	private void handleEnumConstants(String constName, EnumConstant enConst) {
		if (enConst.eContainer() == null) {
			IVariableBinding varBind = variableBindings.stream().filter(
					binding -> binding != null && constName.equals(toFieldNameConverter.convertToFieldName(binding)))
					.findFirst().get();
			if (!varBind.getDeclaringClass().isAnonymous()) {
				var en = enumerationResolver.getByBinding(varBind.getDeclaringClass());
				if (!extractAdditionalInfosFromTypeBindings && !en.getConstants().contains(enConst)) {
					en.getConstants().add(enConst);
				}
			}
		}
	}

	private void register() {
		moduleResolver.getBindings().values().forEach(module -> JavaClasspath.get().registerModule(module));
		packageResolver.getBindings().values().forEach(pack -> JavaClasspath.get().registerPackage(pack));
		annotationResolver.getBindings().values().forEach(ann -> JavaClasspath.get().registerConcreteClassifier(ann));
		enumerationResolver.getBindings().values()
				.forEach(enume -> JavaClasspath.get().registerConcreteClassifier(enume));
		interfaceResolver.getBindings().values()
				.forEach(interf -> JavaClasspath.get().registerConcreteClassifier(interf));
		classResolver.getBindings().values().forEach(clazz -> JavaClasspath.get().registerConcreteClassifier(clazz));
	}

	private void clear() {
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
		varBindToUid.clear();
		objVisited.clear();
		nameCache.clear();
		anonymousClassResolver.getBindings().clear();
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
