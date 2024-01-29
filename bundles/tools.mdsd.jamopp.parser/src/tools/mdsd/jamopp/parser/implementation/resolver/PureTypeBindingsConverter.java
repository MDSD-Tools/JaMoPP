package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToConcreteClassifierConverterWithExtraInfo;

public class PureTypeBindingsConverter {

	private final boolean extractAdditionalInfosFromTypeBindings;

	private final ContainersFactory containersFactory;
	private final Provider<ToConcreteClassifierConverterWithExtraInfo> utilBindingInfoToConcreteClassifierConverter;
	private final Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter;
	private final Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter;

	private final Set<IModuleBinding> moduleBindings;
	private final Set<IPackageBinding> packageBindings;
	private final Set<ITypeBinding> typeBindings;
	private final Set<EObject> objVisited;

	private final ModuleResolver moduleResolver;
	private final PackageResolver packageResolver;
	private final AnnotationResolver annotationResolver;
	private final EnumerationResolver enumerationResolver;
	private final InterfaceResolver interfaceResolver;
	private final ClassResolver classResolver;
	private final ClassifierResolver classifierResolver;

	private final ToTypeNameConverter toTypeNameConverter;

	@Inject
	public PureTypeBindingsConverter(
			final Provider<ToConcreteClassifierConverterWithExtraInfo> utilBindingInfoToConcreteClassifierConverter,
			final PackageResolver packageResolver, final ModuleResolver moduleResolver,
			final InterfaceResolver interfaceResolver,
			@Named("extractAdditionalInfosFromTypeBindings") final boolean extractAdditionalInfosFromTypeBindings,
			final EnumerationResolver enumerationResolver, final ContainersFactory containersFactory,
			final ClassResolver classResolver,
			final Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			final Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			final AnnotationResolver annotationResolver, final Set<ITypeBinding> typeBindings,
			final Set<IPackageBinding> packageBindings, final Set<EObject> objVisited,
			final Set<IModuleBinding> moduleBindings, final ToTypeNameConverter toTypeNameConverter,
			final ClassifierResolver classifierResolver) {
		this.extractAdditionalInfosFromTypeBindings = extractAdditionalInfosFromTypeBindings;
		this.containersFactory = containersFactory;
		this.utilBindingInfoToConcreteClassifierConverter = utilBindingInfoToConcreteClassifierConverter;
		this.bindingToPackageConverter = bindingToPackageConverter;
		this.bindingToModuleConverter = bindingToModuleConverter;
		this.moduleBindings = moduleBindings;
		this.packageBindings = packageBindings;
		this.typeBindings = typeBindings;
		this.objVisited = objVisited;
		this.moduleResolver = moduleResolver;
		this.packageResolver = packageResolver;
		this.annotationResolver = annotationResolver;
		this.enumerationResolver = enumerationResolver;
		this.interfaceResolver = interfaceResolver;
		this.classResolver = classResolver;
		this.classifierResolver = classifierResolver;
		this.toTypeNameConverter = toTypeNameConverter;
	}

	public void convertPureTypeBindings(final ResourceSet resourceSet) {
		int oldSize;
		int newSize = annotationResolver.bindingsSize() + enumerationResolver.bindingsSize() + interfaceResolver.bindingsSize()
				+ classResolver.bindingsSize() + moduleResolver.bindingsSize() + packageResolver.bindingsSize();
		do {
			oldSize = newSize;
			// For concurrent reasons forEach is called on copies
			annotationResolver.forEachBindingOnCopy((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			enumerationResolver.forEachBindingOnCopy((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			interfaceResolver.forEachBindingOnCopy((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			classResolver.forEachBindingOnCopy((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			packageResolver.forEachBindingOnCopy((t, u) -> convertPurePackageBinding(t, u, resourceSet));
			moduleResolver.forEachBindingOnCopy((t, u) -> convertPureModuleBinding(t, u, resourceSet));
			newSize = annotationResolver.bindingsSize() + enumerationResolver.bindingsSize() + interfaceResolver.bindingsSize()
					+ classResolver.bindingsSize() + moduleResolver.bindingsSize() + packageResolver.bindingsSize();
		} while (oldSize < newSize);
	}

	private void convertPureTypeBinding(final String typeName, final ConcreteClassifier classifier,
			final ResourceSet resourceSet) {
		if (!objVisited.contains(classifier)) {
			objVisited.add(classifier);
			final ConcreteClassifier potClass = JavaClasspath.get().getConcreteClassifier(typeName);
			if (!Objects.equals(potClass, classifier)) {
				convert(typeName, classifier, resourceSet);
			}
		}
	}

	private void convert(final String typeName, final ConcreteClassifier classifier, final ResourceSet resourceSet) {
		final ITypeBinding typeBind = typeBindings.stream()
				.filter(type -> type != null && typeName.equals(toTypeNameConverter.convertToTypeName(type)))
				.findFirst().orElse(null);
		if (typeBind == null) {
			classifier.setPackage(packageResolver.getByName(""));
			if (classifier.eContainer() != null) {
				return;
			}
		} else if (typeBind.isTopLevel()) {
			handleTopLevel(typeBind);
		} else if (typeBind.isNested()) {
			handleNested(classifier, resourceSet, typeBind);
		} else if (typeBind.isArray()) {
			handleArray(typeName, resourceSet, typeBind);
		}
		if (classifier.eContainer() == null) {
			handleNullContainer(typeName, classifier, resourceSet);
		}
	}

	private void handleNullContainer(final String typeName, final ConcreteClassifier classifier,
			final ResourceSet resourceSet) {
		final tools.mdsd.jamopp.model.java.containers.CompilationUnit compulationUnit = containersFactory
				.createCompilationUnit();
		compulationUnit.setName("");
		compulationUnit.getClassifiers().add(classifier);
		final String[] namespaces = typeName.strip().split("\\.");
		classifier.setName(namespaces[namespaces.length - 1]);
		for (int index = 0; index < namespaces.length - 1; index++) {
			compulationUnit.getNamespaces().add(namespaces[index]);
		}
		final Resource newResource = resourceSet.createResource(URI.createHierarchicalURI("empty",
				"JaMoPP-CompilationUnit", null, new String[] { typeName + ".java" }, null, null));
		newResource.getContents().add(compulationUnit);
	}

	private void handleTopLevel(final ITypeBinding typeBind) {
		utilBindingInfoToConcreteClassifierConverter.get().convert(typeBind, extractAdditionalInfosFromTypeBindings);
	}

	private void handleNested(final ConcreteClassifier classifier, final ResourceSet resourceSet,
			final ITypeBinding typeBind) {
		final ConcreteClassifier parentClassifier = (ConcreteClassifier) classifierResolver
				.getClassifier(typeBind.getDeclaringClass());
		convertPureTypeBinding(toTypeNameConverter.convertToTypeName(typeBind.getDeclaringClass()), parentClassifier,
				resourceSet);
		classifier.setPackage(packageResolver.getByBinding(typeBind.getPackage()));
	}

	private void handleArray(final String typeName, final ResourceSet resourceSet, final ITypeBinding typeBind) {
		final ITypeBinding elementType = typeBind.getElementType();
		if (!elementType.isPrimitive() && !elementType.isTypeVariable()) {
			convertPureTypeBinding(typeName, (ConcreteClassifier) classifierResolver.getClassifier(elementType),
					resourceSet);
		}
	}

	private void convertPurePackageBinding(final String packageName,
			final tools.mdsd.jamopp.model.java.containers.Package pack, final ResourceSet resourceSet) {
		if (!objVisited.contains(pack)) {
			objVisited.add(pack);
			final tools.mdsd.jamopp.model.java.containers.Package potPack = JavaClasspath.get().getPackage(packageName);
			if (!Objects.equals(potPack, pack)) {
				final IPackageBinding binding = packageBindings.stream().filter(b -> packageName.equals(b.getName()))
						.findFirst().orElse(null);
				if (binding == null) {
					pack.setName("");
					pack.setModule(moduleResolver.getByName(""));
				} else {
					bindingToPackageConverter.get().convert(binding);
				}
				if (pack.eResource() != null) {
					return;
				}
				final Resource newResource = resourceSet.createResource(URI.createHierarchicalURI("empty",
						"JaMoPP-Package", null, new String[] { packageName, "package-info.java" }, null, null));
				newResource.getContents().add(pack);
			}
		}
	}

	private void convertPureModuleBinding(final String modName,
			final tools.mdsd.jamopp.model.java.containers.Module module, final ResourceSet resourceSet) {
		if (!objVisited.contains(module)) {
			objVisited.add(module);
			final tools.mdsd.jamopp.model.java.containers.Module potMod = JavaClasspath.get().getModule(modName);
			if (!Objects.equals(potMod, module) && module.eResource() == null) {
				final IModuleBinding binding = moduleBindings.stream().filter(b -> modName.equals(b.getName()))
						.findFirst().orElse(null);
				if (binding == null) {
					module.getNamespaces().clear();
					final String[] parts = modName.split("\\.");
					Collections.addAll(module.getNamespaces(), parts);
					module.setName("");
				} else {
					bindingToModuleConverter.get().convert(binding);
				}
				final Resource newResource = resourceSet.createResource(URI.createHierarchicalURI("empty",
						"JaMoPP-Module", null, new String[] { modName, "module-info.java" }, null, null));
				newResource.getContents().add(module);
			}
		}
	}
}
