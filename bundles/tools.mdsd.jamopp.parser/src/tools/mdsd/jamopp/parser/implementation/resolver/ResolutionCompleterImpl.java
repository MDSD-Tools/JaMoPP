package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

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
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
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
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.parser.interfaces.resolver.ClassResolverExtension;
import tools.mdsd.jamopp.parser.interfaces.resolver.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.PureTypeBindingsConverter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolutionCompleter;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;

public class ResolutionCompleterImpl implements ResolutionCompleter {

	private final boolean extractAdditionalInfosFromTypeBindings;
	private final Map<IVariableBinding, Integer> varBindToUid;
	private final Map<IBinding, String> nameCache;
	private final Set<IModuleBinding> moduleBindings;
	private final Set<IPackageBinding> packageBindings;
	private final Set<ITypeBinding> typeBindings;
	private final Set<IMethodBinding> methodBindings;
	private final Set<IVariableBinding> variableBindings;
	private final Set<EObject> objVisited;
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
	private final AnonymousClassResolver anonymousClassResolver;
	private final ResolverWithCache<EnumConstant, IVariableBinding> enumConstantResolver;
	private final ResolverWithCache<AdditionalField, IVariableBinding> additionalFieldResolver;
	private final ResolverWithCache<CatchParameter, IVariableBinding> catchParameterResolver;
	private final ResolverWithCache<OrdinaryParameter, IVariableBinding> ordinaryParameterResolver;
	private final ResolverWithCache<AdditionalLocalVariable, IVariableBinding> additionalLocalVariableResolver;
	private final ResolverWithCache<VariableLengthParameter, IVariableBinding> variableLengthParameterResolver;
	private final ResolverWithCache<LocalVariable, IVariableBinding> localVariableResolver;
	private final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver;
	private final MethodCompleter methodCompleterImpl;
	private final PureTypeBindingsConverter pureTypeBindingsConverterImpl;
	private final ClassResolverExtension classResolverExtensionImpl;
	private final Converter<IVariableBinding> toFieldNameConverter;
	private final Converter<ITypeBinding> toTypeNameConverter;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;

	@Inject
	public ResolutionCompleterImpl(
			@Named("extractAdditionalInfosFromTypeBindings") final boolean extractAdditionalInfosFromTypeBindings,
			final Map<IVariableBinding, Integer> varBindToUid, final Map<IBinding, String> nameCache,
			final Set<IModuleBinding> moduleBindings, final Set<IPackageBinding> packageBindings,
			final Set<ITypeBinding> typeBindings, final Set<IMethodBinding> methodBindings,
			final Set<IVariableBinding> variableBindings, final Set<EObject> objVisited,
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
			final AnonymousClassResolver anonymousClassResolver,
			final ResolverWithCache<EnumConstant, IVariableBinding> enumConstantResolver,
			final ResolverWithCache<AdditionalField, IVariableBinding> additionalFieldResolver,
			final ResolverWithCache<CatchParameter, IVariableBinding> catchParameterResolver,
			final ResolverWithCache<OrdinaryParameter, IVariableBinding> ordinaryParameterResolver,
			final ResolverWithCache<AdditionalLocalVariable, IVariableBinding> additionalLocalVariableResolver,
			final ResolverWithCache<VariableLengthParameter, IVariableBinding> variableLengthParameterResolver,
			final ResolverWithCache<LocalVariable, IVariableBinding> localVariableResolver,
			final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver,
			final MethodCompleter methodCompleterImpl, final PureTypeBindingsConverter pureTypeBindingsConverterImpl,
			final ClassResolverExtension classResolverExtensionImpl,
			final Converter<IVariableBinding> toFieldNameConverter,
			@Named("ToTypeNameConverter") final Converter<ITypeBinding> toTypeNameConverter,
			final Resolver<Classifier, ITypeBinding> classifierResolver) {
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
		this.methodCompleterImpl = methodCompleterImpl;
		this.pureTypeBindingsConverterImpl = pureTypeBindingsConverterImpl;
		this.classResolverExtensionImpl = classResolverExtensionImpl;
		this.toFieldNameConverter = toFieldNameConverter;
		this.toTypeNameConverter = toTypeNameConverter;
		this.classifierResolver = classifierResolver;
	}

	@Override
	public void completeResolution(final ResourceSet resourceSet) {
		enumConstantResolver.forEachBinding(this::handleEnumConstants);
		fieldResolver.forEachBinding(this::handleFields);
		constructorResolver.forEachBinding((t, u) -> methodCompleterImpl.completeMethod(t, u));
		classMethodResolver.forEachBinding((t, u) -> methodCompleterImpl.completeMethod(t, u));
		interfaceMethodResolver.forEachBinding((t, u) -> methodCompleterImpl.completeMethod(t, u));

		pureTypeBindingsConverterImpl.convertPureTypeBindings(resourceSet);

		register();
		escapeAllIdentifiers();
		clear();
	}

	private void handleFields(final String fieldName, final Field field) {
		if (field.eContainer() == null) {
			final IVariableBinding varBind = variableBindings.stream()
					.filter(binding -> binding != null && fieldName.equals(toFieldNameConverter.convert(binding)))
					.findFirst().orElse(null);
			if (varBind == null || varBind.getDeclaringClass() == null) {
				classResolverExtensionImpl.addToSyntheticClass(field);
			} else {
				handleFieldsElse(field, varBind);
			}
		}
	}

	private void handleFieldsElse(final Field field, final IVariableBinding varBind) {
		final tools.mdsd.jamopp.model.java.classifiers.Classifier cla = classifierResolver
				.getByBinding(varBind.getDeclaringClass());
		if (cla == null) {
			final String typeName = toTypeNameConverter.convert(varBind.getDeclaringClass());
			if (anonymousClassResolver.containsKey(typeName)) {
				final tools.mdsd.jamopp.model.java.classifiers.AnonymousClass anonClass = anonymousClassResolver
						.get(typeName);
				if (!anonClass.getMembers().contains(field)) {
					anonClass.getMembers().add(field);
				}
			} else {
				classResolverExtensionImpl.addToSyntheticClass(field);
			}
		} else if (!extractAdditionalInfosFromTypeBindings
				&& cla instanceof final tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier
				&& !classifier.getMembers().contains(field)) {
			classifier.getMembers().add(field);
		}
	}

	private void handleEnumConstants(final String constName, final EnumConstant enConst) {
		if (enConst.eContainer() == null) {
			final IVariableBinding varBind = variableBindings.stream()
					.filter(binding -> binding != null && constName.equals(toFieldNameConverter.convert(binding)))
					.findFirst().get();
			if (!varBind.getDeclaringClass().isAnonymous()) {
				final Enumeration enumeration = enumerationResolver.getByBinding(varBind.getDeclaringClass());
				if (!extractAdditionalInfosFromTypeBindings && !enumeration.getConstants().contains(enConst)) {
					enumeration.getConstants().add(enConst);
				}
			}
		}
	}

	private void register() {
		moduleResolver.getBindings().forEach(module -> JavaClasspath.get().registerModule(module));
		packageResolver.getBindings().forEach(pack -> JavaClasspath.get().registerPackage(pack));
		annotationResolver.getBindings().forEach(ann -> JavaClasspath.get().registerConcreteClassifier(ann));
		enumerationResolver.getBindings().forEach(enume -> JavaClasspath.get().registerConcreteClassifier(enume));
		interfaceResolver.getBindings().forEach(interf -> JavaClasspath.get().registerConcreteClassifier(interf));
		classResolver.getBindings().forEach(clazz -> JavaClasspath.get().registerConcreteClassifier(clazz));
	}

	private void clear() {
		moduleResolver.clearBindings();
		packageResolver.clearBindings();
		annotationResolver.clearBindings();
		enumerationResolver.clearBindings();
		interfaceResolver.clearBindings();
		classResolver.clearBindings();
		typeParameterResolver.clearBindings();
		classMethodResolver.clearBindings();
		constructorResolver.clearBindings();
		fieldResolver.clearBindings();
		interfaceMethodResolver.clearBindings();
		additionalFieldResolver.clearBindings();
		localVariableResolver.clearBindings();
		additionalLocalVariableResolver.clearBindings();
		enumConstantResolver.clearBindings();
		variableLengthParameterResolver.clearBindings();
		ordinaryParameterResolver.clearBindings();
		catchParameterResolver.clearBindings();
		moduleBindings.clear();
		packageBindings.clear();
		typeBindings.clear();
		methodBindings.clear();
		variableBindings.clear();
		varBindToUid.clear();
		objVisited.clear();
		nameCache.clear();
		anonymousClassResolver.clearBindings();
	}

	private void escapeAllIdentifiers() {
		moduleResolver.getBindings().forEach(this::escapeIdentifiers);
		packageResolver.getBindings().forEach(this::escapeIdentifiers);
		annotationResolver.getBindings().forEach(this::escapeIdentifiers);
		enumerationResolver.getBindings().forEach(this::escapeIdentifiers);
		classResolver.getBindings().forEach(this::escapeIdentifiers);
		interfaceResolver.getBindings().forEach(this::escapeIdentifiers);
	}

	private void escapeIdentifiers(final EObject obj) {
		obj.eAllContents().forEachRemaining(this::escapeIdentifier);
	}

	private void escapeIdentifier(final Notifier not) {
		if (not instanceof final tools.mdsd.jamopp.model.java.commons.NamedElement ele) {
			final StringBuilder builder = new StringBuilder();
			final String name = ele.getName();
			name.codePoints().forEach(i -> {
				if (i <= 0x20 || Character.MIN_SURROGATE <= i && i <= Character.MAX_SURROGATE) {
					builder.append("\\u").append(String.format("%04x", i));
				} else {
					builder.appendCodePoint(i);
				}
			});
			ele.setName(builder.toString());
		}
	}

}
