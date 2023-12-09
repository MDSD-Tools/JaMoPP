package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.Name;
import org.emftext.language.java.modules.ModuleReference;
import org.emftext.language.java.modules.ModulesFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToModuleReferenceConverter implements Converter<Name, ModuleReference> {

	private final ModulesFactory modulesFactory;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtResolverUtility;

	@Inject
	public ToModuleReferenceConverter(IUtilNamedElement utilNamedElement, ModulesFactory modulesFactory,
			IUtilJdtResolver jdtResolverUtility) {
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
