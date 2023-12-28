package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.parser.jdt.implementation.helper.UtilJdtResolverImpl;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;

public class PureTypeBindingsConverter {

	private final UtilJdtResolverImpl utilJdtResolverImpl;

	private final boolean extractAdditionalInfosFromTypeBindings;

	private final ContainersFactory containersFactory;
	private final Provider<UtilBindingInfoToConcreteClassifierConverter> utilBindingInfoToConcreteClassifierConverter;
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

	@Inject
	public PureTypeBindingsConverter(UtilJdtResolverImpl utilJdtResolverImpl,
			Provider<UtilBindingInfoToConcreteClassifierConverter> utilBindingInfoToConcreteClassifierConverter,
			PackageResolver packageResolver, ModuleResolver moduleResolver, InterfaceResolver interfaceResolver,
			@Named("extractAdditionalInfosFromTypeBindings") boolean extractAdditionalInfosFromTypeBindings,
			EnumerationResolver enumerationResolver, ContainersFactory containersFactory, ClassResolver classResolver,
			Provider<Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package>> bindingToPackageConverter,
			Provider<Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module>> bindingToModuleConverter,
			AnnotationResolver annotationResolver, HashSet<ITypeBinding> typeBindings,
			HashSet<IPackageBinding> packageBindings, HashSet<EObject> objVisited,
			HashSet<IModuleBinding> moduleBindings) {
		this.utilJdtResolverImpl = utilJdtResolverImpl;
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
	}

	@SuppressWarnings("unchecked")
	public void convertPureTypeBindings(ResourceSet resourceSet) {
		int oldSize;
		int newSize = annotationResolver.getBindings().size() + enumerationResolver.getBindings().size()
				+ interfaceResolver.getBindings().size() + classResolver.getBindings().size()
				+ moduleResolver.getBindings().size() + packageResolver.getBindings().size();
		do {
			oldSize = newSize;
			HashMap<String, ? extends tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier> map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Annotation>) annotationResolver
					.getBindings().clone();
			map.forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Enumeration>) enumerationResolver
					.getBindings().clone();
			map.forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Interface>) interfaceResolver.getBindings()
					.clone();
			map.forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			map = (HashMap<String, tools.mdsd.jamopp.model.java.classifiers.Class>) classResolver.getBindings().clone();
			map.forEach((t, u) -> convertPureTypeBinding(t, u, resourceSet));
			HashMap<String, tools.mdsd.jamopp.model.java.containers.Package> mapP = (HashMap<String, tools.mdsd.jamopp.model.java.containers.Package>) packageResolver
					.getBindings().clone();
			mapP.forEach((t, u) -> convertPurePackageBinding(t, u, resourceSet));
			HashMap<String, tools.mdsd.jamopp.model.java.containers.Module> mapM = (HashMap<String, tools.mdsd.jamopp.model.java.containers.Module>) moduleResolver
					.getBindings().clone();
			mapM.forEach((t, u) -> convertPureModuleBinding(t, u, resourceSet));
			newSize = annotationResolver.getBindings().size() + enumerationResolver.getBindings().size()
					+ interfaceResolver.getBindings().size() + classResolver.getBindings().size()
					+ moduleResolver.getBindings().size() + packageResolver.getBindings().size();
		} while (oldSize < newSize);
	}

	private void convertPureTypeBinding(String typeName,
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier classifier, ResourceSet resourceSet) {
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
				.filter(type -> type != null && typeName.equals(utilJdtResolverImpl.convertToTypeName(type)))
				.findFirst().orElse(null);
		if (typeBind == null) {
			classifier.setPackage(utilJdtResolverImpl.getPackage(""));
			if (classifier.eContainer() != null) {
				return;
			}
		} else if (typeBind.isTopLevel()) {
			utilBindingInfoToConcreteClassifierConverter.get().convertToConcreteClassifier(typeBind,
					extractAdditionalInfosFromTypeBindings);
		} else if (typeBind.isNested()) {
			tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier parentClassifier = (tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) utilJdtResolverImpl
					.getClassifier(typeBind.getDeclaringClass());
			convertPureTypeBinding(utilJdtResolverImpl.convertToTypeName(typeBind.getDeclaringClass()),
					parentClassifier, resourceSet);
			classifier.setPackage(utilJdtResolverImpl.getPackage(typeBind.getPackage()));
		} else if (typeBind.isArray()) {
			ITypeBinding elementType = typeBind.getElementType();
			if (!elementType.isPrimitive() && !elementType.isTypeVariable()) {
				convertPureTypeBinding(typeName,
						(tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) utilJdtResolverImpl
								.getClassifier(elementType),
						resourceSet);
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
			pack.setModule(utilJdtResolverImpl.getModule(""));
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
