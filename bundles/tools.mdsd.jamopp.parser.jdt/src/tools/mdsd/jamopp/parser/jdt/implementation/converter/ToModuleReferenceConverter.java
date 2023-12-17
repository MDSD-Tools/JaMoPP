package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.Name;
import org.emftext.language.java.modules.ModuleReference;
import org.emftext.language.java.modules.ModulesFactory;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToModuleReferenceConverter implements Converter<Name, ModuleReference> {

	private final ModulesFactory modulesFactory;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver jdtResolverUtility;

	@Inject
	public ToModuleReferenceConverter(UtilNamedElement utilNamedElement, ModulesFactory modulesFactory,
			UtilJdtResolver jdtResolverUtility) {
		this.modulesFactory = modulesFactory;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public ModuleReference convert(Name name) {
		ModuleReference ref = modulesFactory.createModuleReference();
		org.emftext.language.java.containers.Module modProxy = jdtResolverUtility
				.getModule((IModuleBinding) name.resolveBinding());
		modProxy.setName("");
		ref.setTarget(modProxy);
		utilNamedElement.addNameToNameSpace(name, modProxy);
		return ref;
	}

}
