package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.List;

import javax.inject.Provider;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.model.java.modules.ExportsModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ModuleReference;
import tools.mdsd.jamopp.model.java.modules.ModulesFactory;
import tools.mdsd.jamopp.model.java.modules.OpensModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ProvidesModuleDirective;
import tools.mdsd.jamopp.model.java.modules.RequiresModuleDirective;
import tools.mdsd.jamopp.model.java.modules.UsesModuleDirective;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToModuleConverter
		implements Converter<IModuleBinding, tools.mdsd.jamopp.model.java.containers.Module> {

	private final ModulesFactory modulesFactory;
	private final ModifiersFactory modifiersFactory;
	private final UtilNamedElement utilNamedElement;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	private JdtResolver jdtTResolverUtility;
	private final Provider<Converter<ITypeBinding, List<TypeReference>>> toTypeReferencesConverter;

	@Inject
	public BindingToModuleConverter(final ModulesFactory modulesFactory, final ModifiersFactory modifiersFactory,
			final JdtResolver jdtTResolverUtility, final UtilNamedElement utilNamedElement,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter,
			final Provider<Converter<ITypeBinding, List<TypeReference>>> toTypeReferencesConverter) {
		this.modulesFactory = modulesFactory;
		this.modifiersFactory = modifiersFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Module convert(final IModuleBinding binding) {
		final tools.mdsd.jamopp.model.java.containers.Module result = jdtTResolverUtility.getModule(binding);
		if (result.eContents().isEmpty()) {
			addAnnotations(binding, result);
			if (binding.isOpen()) {
				result.setOpen(modifiersFactory.createOpen());
			}
			utilNamedElement.convertToNamespacesAndSet(binding.getName(), result);
			result.setName("");
			try {
				addTargets(binding, result);
			} catch (final AbortCompilation e) {
				// Ignore
			}
		}
		return result;
	}

	private void addTargets(final IModuleBinding binding, final tools.mdsd.jamopp.model.java.containers.Module result) {
		for (final IPackageBinding packBind : binding.getExportedPackages()) {
			final ExportsModuleDirective dir = modulesFactory.createExportsModuleDirective();
			dir.setAccessablePackage(jdtTResolverUtility.getPackage(packBind));
			final String[] mods = binding.getExportedTo(packBind);
			for (final String modName : mods) {
				final ModuleReference ref = modulesFactory.createModuleReference();
				ref.setTarget(jdtTResolverUtility.getModule(modName));
				dir.getModules().add(ref);
			}
			result.getTarget().add(dir);
		}
		for (final IPackageBinding packBind : binding.getOpenedPackages()) {
			final OpensModuleDirective dir = modulesFactory.createOpensModuleDirective();
			dir.setAccessablePackage(jdtTResolverUtility.getPackage(packBind));
			final String[] mods = binding.getOpenedTo(packBind);
			for (final String modName : mods) {
				final ModuleReference ref = modulesFactory.createModuleReference();
				ref.setTarget(jdtTResolverUtility.getModule(modName));
				dir.getModules().add(ref);
			}
			result.getTarget().add(dir);
		}
		for (final IModuleBinding modBind : binding.getRequiredModules()) {
			final RequiresModuleDirective dir = modulesFactory.createRequiresModuleDirective();
			final tools.mdsd.jamopp.model.java.containers.Module reqMod = jdtTResolverUtility.getModule(modBind);
			final ModuleReference ref = modulesFactory.createModuleReference();
			ref.setTarget(reqMod);
			dir.setRequiredModule(ref);
			result.getTarget().add(dir);
		}
		for (final ITypeBinding typeBind : binding.getUses()) {
			final UsesModuleDirective dir = modulesFactory.createUsesModuleDirective();
			dir.setTypeReference(toTypeReferencesConverter.get().convert(typeBind).get(0));
			result.getTarget().add(dir);
		}
		for (final ITypeBinding typeBind : binding.getServices()) {
			final ProvidesModuleDirective dir = modulesFactory.createProvidesModuleDirective();
			dir.setTypeReference(toTypeReferencesConverter.get().convert(typeBind).get(0));
			for (final ITypeBinding service : binding.getImplementations(typeBind)) {
				dir.getServiceProviders().addAll(toTypeReferencesConverter.get().convert(service));
			}
			result.getTarget().add(dir);
		}
	}

	private void addAnnotations(final IModuleBinding binding,
			final tools.mdsd.jamopp.model.java.containers.Module result) {
		try {
			for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (final AbortCompilation e) {
			// Ignore
		}
	}

	@Inject
	public void setJdtTResolverUtility(final JdtResolver jdtTResolverUtility) {
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

}
