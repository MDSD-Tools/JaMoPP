package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Field;

public class ResolutionCompleter {

	private final boolean extractAdditionalInfosFromTypeBindings;
	private final Map<IVariableBinding, Integer> varBindToUid;
	private final Map<IBinding, String> nameCache;
	private final Set<IModuleBinding> moduleBindings;
	private final Set<IPackageBinding> packageBindings;
	private final Set<ITypeBinding> typeBindings;
	private final Set<IMethodBinding> methodBindings;
	private final Set<IVariableBinding> variableBindings;
	private final Set<EObject> objVisited;
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
	private final ClassResolverExtensionImpl classResolverExtensionImpl;
	private final ToFieldNameConverter toFieldNameConverter;
	private final ToTypeNameConverter toTypeNameConverter;
	private final ClassifierResolver classifierResolver;

	@Inject
	public ResolutionCompleter(final VariableLengthParameterResolver variableLengthParameterResolver,
			final Set<IVariableBinding> variableBindings, final Map<IVariableBinding, Integer> varBindToUid,
			final TypeParameterResolver typeParameterResolver, final Set<ITypeBinding> typeBindings,
			final PureTypeBindingsConverter pureTypeBindingsConverter, final PackageResolver packageResolver,
			final Set<IPackageBinding> packageBindings, final OrdinaryParameterResolver ordinaryParameterResolver,
			final Set<EObject> objVisited, final ModuleResolver moduleResolver,
			final Set<IModuleBinding> moduleBindings, final MethodCompleter methodCompleter,
			final Set<IMethodBinding> methodBindings, final LocalVariableResolver localVariableResolver,
			final InterfaceResolver interfaceResolver, final InterfaceMethodResolver interfaceMethodResolver,
			final FieldResolver fieldResolver,
			@Named("extractAdditionalInfosFromTypeBindings") final boolean extractAdditionalInfosFromTypeBindings,
			final EnumerationResolver enumerationResolver, final EnumConstantResolver enumConstantResolver,
			final ConstructorResolver constructorResolver, final ClassResolver classResolver,
			final ClassMethodResolver classMethodResolver, final CatchParameterResolver catchParameterResolver,
			final AnonymousClassResolver anonymousClassResolver, final AnnotationResolver annotationResolver,
			final AdditionalLocalVariableResolver additionalLocalVariableResolver,
			final AdditionalFieldResolver additionalFieldResolver, final ClassResolverExtensionImpl classResolverExtensionImpl,
			final ToTypeNameConverter toTypeNameConverter, final ToFieldNameConverter toFieldNameConverter,
			final ClassifierResolver classifierResolver, final Map<IBinding, String> nameCache) {
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
		this.classResolverExtensionImpl = classResolverExtensionImpl;
		this.toFieldNameConverter = toFieldNameConverter;
		this.toTypeNameConverter = toTypeNameConverter;
		this.classifierResolver = classifierResolver;
	}

	public void completeResolution(final ResourceSet resourceSet) {
		enumConstantResolver.forEachBinding(this::handleEnumConstants);
		fieldResolver.forEachBinding(this::handleFields);
		constructorResolver.forEachBinding((t, u) -> methodCompleter.completeMethod(t, u));
		classMethodResolver.forEachBinding((t, u) -> methodCompleter.completeMethod(t, u));
		interfaceMethodResolver.forEachBinding((t, u) -> methodCompleter.completeMethod(t, u));

		pureTypeBindingsConverter.convertPureTypeBindings(resourceSet);

		register();
		escapeAllIdentifiers();
		clear();
	}

	private void handleFields(final String fieldName, final Field field) {
		if (field.eContainer() == null) {
			final IVariableBinding varBind = variableBindings.stream().filter(
					binding -> binding != null && fieldName.equals(toFieldNameConverter.convertToFieldName(binding)))
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
				.getClassifier(varBind.getDeclaringClass());
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
			final IVariableBinding varBind = variableBindings.stream().filter(
					binding -> binding != null && constName.equals(toFieldNameConverter.convertToFieldName(binding)))
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
		enumerationResolver.getBindings()
				.forEach(enume -> JavaClasspath.get().registerConcreteClassifier(enume));
		interfaceResolver.getBindings()
				.forEach(interf -> JavaClasspath.get().registerConcreteClassifier(interf));
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
