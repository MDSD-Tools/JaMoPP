package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.Name;

import tools.mdsd.jamopp.model.java.modules.ModuleReference;
import tools.mdsd.jamopp.model.java.modules.ModulesFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToModuleReferenceConverter implements Converter<Name, ModuleReference> {

	private final ModulesFactory modulesFactory;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToModuleReferenceConverter(final UtilNamedElement utilNamedElement, final ModulesFactory modulesFactory,
			final JdtResolver jdtResolverUtility) {
		this.modulesFactory = modulesFactory;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public ModuleReference convert(final Name name) {
		final ModuleReference ref = modulesFactory.createModuleReference();
		final tools.mdsd.jamopp.model.java.containers.Module modProxy = jdtResolverUtility
				.getModule((IModuleBinding) name.resolveBinding());
		modProxy.setName("");
		ref.setTarget(modProxy);
		utilNamedElement.addNameToNameSpace(name, modProxy);
		return ref;
	}

}
