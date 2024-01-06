package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToConcreteClassifierConverterWithExtraInfo;

public class PureTypeBindingsConverter {

	private final boolean extractAdditionalInfosFromTypeBindings;

	private final ContainersFactory containersFactory;
	private final Provider<ToConcreteClassifierConverterWithExtraInfo> utilBindingInfoToConcreteClassifierConverter;
	private final Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter;
	private final Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter;

	private final HashSet<IModuleBinding> moduleBindings;
	private final HashSet<IPackageBinding> packageBindings;
	private final HashSet<ITypeBinding> typeBindings;
	private final HashSet<EObject> objVisited;

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
			Provider<ToConcreteClassifierConverterWithExtraInfo> utilBindingInfoToConcreteClassifierConverter,
			PackageResolver packageResolver, ModuleResolver moduleResolver, InterfaceResolver interfaceResolver,
			@Named("extractAdditionalInfosFromTypeBindings") boolean extractAdditionalInfosFromTypeBindings,
			EnumerationResolver enumerationResolver, ContainersFactory containersFactory, ClassResolver classResolver,
			Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			AnnotationResolver annotationResolver, HashSet<ITypeBinding> typeBindings,
			HashSet<IPackageBinding> packageBindings, HashSet<EObject> objVisited,
			HashSet<IModuleBinding> moduleBindings, ToTypeNameConverter toTypeNameConverter,
			ClassifierResolver classifierResolver) {
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

	public void convertPureTypeBindings(ResourceSet resourceSet) {
		int oldSize;
		int newSize = annotationResolver.getBindings().size() + enumerationResolver.getBindings().size()
				+ interfaceResolver.getBindings().size() + classResolver.getBindings().size()
				+ moduleResolver.getBindings().size() + packageResolver.getBindings().size();
		do {
			oldSize = newSize;
			// For concurrent reasons forEach is called on copies
			new HashMap<>(annotationResolver.getBindings())
					.forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			new HashMap<>(enumerationResolver.getBindings())
					.forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			new HashMap<>(interfaceResolver.getBindings()).forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			new HashMap<>(classResolver.getBindings()).forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			new HashMap<>(packageResolver.getBindings())
					.forEach((t, u) -> convertPurePackageBinding(t, u, resourceSet));
			new HashMap<>(moduleResolver.getBindings()).forEach((t, u) -> convertPureModuleBinding(t, u, resourceSet));
			newSize = annotationResolver.getBindings().size() + enumerationResolver.getBindings().size()
					+ interfaceResolver.getBindings().size() + classResolver.getBindings().size()
					+ moduleResolver.getBindings().size() + packageResolver.getBindings().size();
		} while (oldSize < newSize);
	}

	private void convertPureTypeBinding(String typeName, ConcreteClassifier classifier, ResourceSet resourceSet) {
		if (objVisited.contains(classifier)) {
			return;
		}
		objVisited.add(classifier);
		ConcreteClassifier potClass = JavaClasspath.get().getConcreteClassifier(typeName);
		if (potClass == classifier) {
			return;
		}
		ITypeBinding typeBind = typeBindings.stream()
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

	private void handleNullContainer(String typeName, ConcreteClassifier classifier, ResourceSet resourceSet) {
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

	private void handleTopLevel(ITypeBinding typeBind) {
		utilBindingInfoToConcreteClassifierConverter.get().convert(typeBind, extractAdditionalInfosFromTypeBindings);
	}

	private void handleNested(ConcreteClassifier classifier, ResourceSet resourceSet, ITypeBinding typeBind) {
		ConcreteClassifier parentClassifier = (ConcreteClassifier) classifierResolver
				.getClassifier(typeBind.getDeclaringClass());
		convertPureTypeBinding(toTypeNameConverter.convertToTypeName(typeBind.getDeclaringClass()), parentClassifier,
				resourceSet);
		classifier.setPackage(packageResolver.getByBinding(typeBind.getPackage()));
	}

	private void handleArray(String typeName, ResourceSet resourceSet, ITypeBinding typeBind) {
		ITypeBinding elementType = typeBind.getElementType();
		if (!elementType.isPrimitive() && !elementType.isTypeVariable()) {
			convertPureTypeBinding(typeName, (ConcreteClassifier) classifierResolver.getClassifier(elementType),
					resourceSet);
		}
	}

	private void convertPurePackageBinding(String packageName, tools.mdsd.jamopp.model.java.containers.Package pack,
			ResourceSet resourceSet) {
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
			pack.setModule(moduleResolver.getByName(""));
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

	private void convertPureModuleBinding(String modName, tools.mdsd.jamopp.model.java.containers.Module module,
			ResourceSet resourceSet) {
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

}
