package jamopp.parser.jdt.implementation.converter;

import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ExportsModuleDirective;
import org.emftext.language.java.modules.ModuleReference;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.modules.OpensModuleDirective;
import org.emftext.language.java.modules.ProvidesModuleDirective;
import org.emftext.language.java.modules.RequiresModuleDirective;
import org.emftext.language.java.modules.UsesModuleDirective;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

@SuppressWarnings("restriction")
public class BindingToModuleConverter
		implements Converter<IModuleBinding, org.emftext.language.java.containers.Module> {

	private final ModulesFactory modulesFactory;
	private final ModifiersFactory modifiersFactory;
	private final UtilNamedElement utilNamedElement;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	private UtilJdtResolver jdtTResolverUtility;
	private Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	BindingToModuleConverter(Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			ModulesFactory modulesFactory, ModifiersFactory modifiersFactory, UtilJdtResolver jdtTResolverUtility,
			UtilNamedElement utilNamedElement,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.modulesFactory = modulesFactory;
		this.modifiersFactory = modifiersFactory;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.utilNamedElement = utilNamedElement;
	}

	@Override
	public org.emftext.language.java.containers.Module convert(IModuleBinding binding) {
		org.emftext.language.java.containers.Module result = jdtTResolverUtility.getModule(binding);
		if (!result.eContents().isEmpty()) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.isOpen()) {
			result.setOpen(modifiersFactory.createOpen());
		}
		utilNamedElement.convertToNamespacesAndSet(binding.getName(), result);
		result.setName("");
		try {
			for (IPackageBinding packBind : binding.getExportedPackages()) {
				ExportsModuleDirective dir = modulesFactory.createExportsModuleDirective();
				dir.setAccessablePackage(jdtTResolverUtility.getPackage(packBind));
				String[] mods = binding.getExportedTo(packBind);
				for (String modName : mods) {
					ModuleReference ref = modulesFactory.createModuleReference();
					ref.setTarget(jdtTResolverUtility.getModule(modName));
					dir.getModules().add(ref);
				}
				result.getTarget().add(dir);
			}
			for (IPackageBinding packBind : binding.getOpenedPackages()) {
				OpensModuleDirective dir = modulesFactory.createOpensModuleDirective();
				dir.setAccessablePackage(jdtTResolverUtility.getPackage(packBind));
				String[] mods = binding.getOpenedTo(packBind);
				for (String modName : mods) {
					ModuleReference ref = modulesFactory.createModuleReference();
					ref.setTarget(jdtTResolverUtility.getModule(modName));
					dir.getModules().add(ref);
				}
				result.getTarget().add(dir);
			}
			for (IModuleBinding modBind : binding.getRequiredModules()) {
				RequiresModuleDirective dir = modulesFactory.createRequiresModuleDirective();
				org.emftext.language.java.containers.Module reqMod = jdtTResolverUtility.getModule(modBind);
				ModuleReference ref = modulesFactory.createModuleReference();
				ref.setTarget(reqMod);
				dir.setRequiredModule(ref);
				result.getTarget().add(dir);
			}
			for (ITypeBinding typeBind : binding.getUses()) {
				UsesModuleDirective dir = modulesFactory.createUsesModuleDirective();
				dir.setTypeReference(toTypeReferencesConverter.convert(typeBind).get(0));
				result.getTarget().add(dir);
			}
			for (ITypeBinding typeBind : binding.getServices()) {
				ProvidesModuleDirective dir = modulesFactory.createProvidesModuleDirective();
				dir.setTypeReference(toTypeReferencesConverter.convert(typeBind).get(0));
				for (ITypeBinding service : binding.getImplementations(typeBind)) {
					dir.getServiceProviders().addAll(toTypeReferencesConverter.convert(service));
				}
				result.getTarget().add(dir);
			}
		} catch (AbortCompilation e) {
		}
		return result;
	}

	@Inject
	public void setJdtTResolverUtility(UtilJdtResolver jdtTResolverUtility) {
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

	@Inject
	public void setToTypeReferencesConverter(ToTypeReferencesConverter toTypeReferencesConverter) {
		this.toTypeReferencesConverter = toTypeReferencesConverter;
	}

}